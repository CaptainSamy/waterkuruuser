package wssj.co.jp.obis.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import wssj.co.jp.obis.R;
import wssj.co.jp.obis.model.ErrorResponse;
import wssj.co.jp.obis.screens.MainActivity;

/**
 * Created by Nguyen Huu Ta on 11/5/2017.
 */

public final class Utils {

    private static final String TAG = "Utils";

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

//    @NonNull
//    public static List<ListCardResponse.ListCardData.CardData> getListCardByType(List<ListCardResponse.ListCardData.CardData> cards, String cardType) {
//        List<ListCardResponse.ListCardData.CardData> list = new ArrayList<>();
//        for (ListCardResponse.ListCardData.CardData card : cards) {
//            if (TextUtils.equals(card.getCardStatus(), cardType)) {
//                list.add(card);
//            }
//        }
//        return list;
//    }

    public static int convertDpToPixel(Context context, float dp) {
        if (context != null && context.getResources() != null) {
            Resources resources = context.getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            return (int) (dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        }
        return (int) dp;
    }

    public static ErrorResponse parseErrorResponse(VolleyError errorVolley) {
        ErrorResponse error = null;
        //get response body and parse with appropriate encoding
        if (errorVolley != null) {
            NetworkResponse networkResponse = errorVolley.networkResponse;
            if (networkResponse != null) {
                String json = new String(networkResponse.data);
                if (!TextUtils.isEmpty(json)) {
                    try {
                        error = new Gson().fromJson(json, ErrorResponse.class);
                    } catch (JsonSyntaxException e) {
                        Logger.e(TAG, "JsonSyntaxException: " + e.getMessage());
                    }
                }
            }
        }
        return error;
    }

    public static byte[] getByteArrayFromFile(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            int size = (int) file.length();
            byte[] bytes = new byte[size];
            BufferedInputStream buf = null;
            try {
                buf = new BufferedInputStream(new FileInputStream(file));
                buf.read(bytes, 0, bytes.length);
            } catch (FileNotFoundException e) {
                Logger.e(TAG, "FileNotFoundException: " + e.getMessage());
            } catch (IOException e) {
                Logger.e(TAG, "IOException: " + e.getMessage());
            } finally {
                if (buf != null) {
                    try {
                        buf.close();
                    } catch (IOException e) {
                        Logger.e(TAG, "IOException: " + e.getMessage());
                    }
                }
            }
            return bytes;
        }
        return null;
    }

    public static String convertLongToDate(long millis) {

        return String.format("%02d:%02d",
                TimeUnit.SECONDS.toHours(millis),
                TimeUnit.SECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(millis))
        );
    }

    public static String formatDate(long time, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GTM"));
        return simpleDateFormat.format(new Date(time));
    }

    public static String get2NumbericString(int number) {
        if (number < 10) {
            return "0" + number;
        } else {
            return number + "";
        }
    }

    public static Uri createImageFile(Context context, String imageName) {
        String imagePathStr = Environment.getExternalStorageDirectory() + "/"
                + context.getPackageName();
        File path = new File(imagePathStr);
        if (!path.exists()) {
            path.mkdir();
        }
        File photo = new File(path, imageName + ".jpg");
        return Uri.fromFile(photo);
    }

    public static File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    public static void hideTool(View view, final Activity context) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(context);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView, context);
            }
        }
    }

    public static void setupUI(View view, final Activity context) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(context);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView, context);
            }
        }
    }

    public static void setupUI(View view, final Dialog context) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(context);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView, context);
            }
        }
    }

    public static void hideSoftKeyboard(Dialog activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getContext().getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) return Constants.EMPTY_STRING;
        if (!TextUtils.isEmpty(filePath)) {
            String fileNameJPG = filePath.substring(filePath.lastIndexOf("/") + 1);
            String[] listFileName = fileNameJPG.split(Constants.SPLIT);
            String fileName = null;
            if (listFileName.length > 1) {
                fileName = listFileName[0];
            }
            return fileName + System.currentTimeMillis() + ".png";
        }
        return Constants.EMPTY_STRING;
    }

    public static String distanceTimes(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        PrettyTime prettyTime = new PrettyTime(new Locale("JA"));//if only setting Japan language, change string =JA
        return prettyTime.format(calendar.getTime());
    }

    public static String distanceTimesViet(long time) {
        Calendar calendar = Calendar.getInstance();

        PrettyTime prettyTime = new PrettyTime(new Locale("VN"));//if only setting Japan language, change string =JA
        return prettyTime.format(calendar.getTime());
    }

    public static String convertLongToTime(long time, boolean isConvertGMT) {
        Calendar calendar;
        if (isConvertGMT) {
            calendar = Calendar.getInstance();
        } else {
            calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        }
        calendar.setTimeInMillis(time);

        return String.format(Locale.getDefault(), "%02d", calendar.get(Calendar.HOUR_OF_DAY))
                + ":" + String.format(Locale.getDefault(), "%02d", calendar.get(Calendar.MINUTE))
                + "   " + String.format(Locale.getDefault(), "%04d", calendar.get(Calendar.YEAR))
                + "-" + String.format(Locale.getDefault(), "%02d", calendar.get(Calendar.MONTH) + 1)
                + "-" + String.format(Locale.getDefault(), "%02d", calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static void fillImage(final Context context, final String imgPath, final ImageView imageView, final int resIdError) {
        Logger.d(TAG, "image " + imgPath);
        if (!TextUtils.isEmpty(imgPath)) {
            Glide.with(context)
                    .load(imgPath)
                    .fitCenter()
                    .placeholder(R.drawable.loading)
                    .override(convertDpToPixel(context, 50), convertDpToPixel(context, 50))
                    .into(new SimpleTarget<GlideDrawable>() {

                        @Override
                        public void onLoadStarted(Drawable placeholder) {
                            imageView.setImageDrawable(placeholder);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            Logger.d(TAG, "onLoadFailed " + imgPath);
                            imageView.setImageResource(resIdError);
                        }

                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            imageView.setImageDrawable(resource);
                        }
                    });
        } else {
            imageView.setImageResource(resIdError);
        }
    }


    public static void fillImage(final Context context, final String imgPath, final ImageView imageView, final int placeHolder,int quality) {
        Logger.d(TAG, "image " + imgPath);
        if (!TextUtils.isEmpty(imgPath)) {
            Glide.with(context)
                    .load(imgPath)
                    .fitCenter()
                    .placeholder(placeHolder)
                    .override(convertDpToPixel(context, quality), convertDpToPixel(context, quality))
                    .into(new SimpleTarget<GlideDrawable>() {

                        @Override
                        public void onLoadStarted(Drawable placeholder) {
                            imageView.setImageDrawable(placeholder);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            Logger.d(TAG, "onLoadFailed " + imgPath);
                            imageView.setImageResource(placeHolder);
                        }

                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            imageView.setImageDrawable(resource);
                        }
                    });
        } else {
            imageView.setImageResource(placeHolder);
        }
    }


    public static void fillImageQuantityOrigin(final Context context, String imgPath, final ImageView imageView, final int resIdError) {
        if (!TextUtils.isEmpty(imgPath)) {
            Glide.with(context)
                    .load(imgPath)
                    .placeholder(R.drawable.loading)
                    .into(new SimpleTarget<GlideDrawable>() {

                        @Override
                        public void onLoadStarted(Drawable placeholder) {
                            imageView.setImageDrawable(placeholder);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            imageView.setImageResource(resIdError);
                        }

                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            imageView.setImageDrawable(resource);
                        }
                    });
        } else {
            imageView.setImageResource(resIdError);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T findViewById(View rootView, @IdRes int id) {
        if (rootView != null) {
            return (T) rootView.findViewById(id);
        }
        return null;
    }

    public static Bitmap cropImage(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static <T> void requireNonNull(T object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(String.valueOf(message));
        }
    }


    public static void overrideRequestHeader(Request<?> request, String key, String value) {
        if (request != null) {
            try {
                Map<String, String> baseHeaders = request.getHeaders();
                if (baseHeaders != null && !TextUtils.isEmpty(key) && baseHeaders.containsKey(key)) {
                    baseHeaders.put(key, value);
                }
            } catch (AuthFailureError authFailureError) {
                Logger.e(TAG, "AuthFailureError: " + authFailureError.getMessage());
            }
        }
    }


    private static void formatHtml(final TextView textView, final String html, final Drawable result) {
        final Context context = textView.getContext();
        Spanned spanned = Html.fromHtml(html, new Html.ImageGetter() {

            @Override
            public Drawable getDrawable(String source) {
                if (result != null) {
                    result.setBounds(0, 0, result.getIntrinsicWidth(), result.getIntrinsicHeight());
                } else {
                    Glide.with(context)
                            .load(source)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(new SimpleTarget<GlideDrawable>() {

                                @Override
                                public void onResourceReady(final GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                    textView.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            formatHtml(textView, html, resource);
                                        }
                                    });
                                }
                            });
                }
                return result;
            }
        }, null);
        textView.setText(spanned);
    }

    public static String convertDuration(int duration) {
        return String.format("%2d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );
    }

    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    public static boolean isNotEmpty(List list) {
        return list != null && list.size() > 0;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }


    public static int getWidthDevice(MainActivity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return width;
    }

}
