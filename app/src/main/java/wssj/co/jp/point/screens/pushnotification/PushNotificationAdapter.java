package wssj.co.jp.point.screens.pushnotification;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import wssj.co.jp.point.R;
import wssj.co.jp.point.model.firebase.NotificationMessage;
import wssj.co.jp.point.screens.dialograting.DialogRating;
import wssj.co.jp.point.utils.Constants;
import wssj.co.jp.point.utils.Logger;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationAdapter extends ArrayAdapter<NotificationMessage> {

    public PushNotificationAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<NotificationMessage> objects) {
        super(context, resource, objects);
    }

    private IEndOfListView mCallback;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_push_notification, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.fillDataToView(getContext(), getItem(position));
        if (position == getCount() - 1) {
            mCallback.onEndOfListView();
        }
        return convertView;
    }

    private static class ViewHolder {

        TextView mTitle, mBody, mTime, mButtonRating;

        ViewHolder(View root) {
            mTitle = (TextView) root.findViewById(R.id.title_notification);
            mBody = (TextView) root.findViewById(R.id.body_notification);
            mTime = (TextView) root.findViewById(R.id.time_notification);
            mButtonRating = (TextView) root.findViewById(R.id.buttonRating);
        }

        void fillDataToView(final Context context, NotificationMessage notificationMessage) {
            mTitle.setText(notificationMessage.getTitle());
            mBody.setText(notificationMessage.getMessage());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(notificationMessage.getPushTime());

            calculatorTime(notificationMessage.getPushTime());
            String time = String.format(Locale.getDefault(), "%02d", calendar.get(Calendar.HOUR_OF_DAY))
                    + ":" + String.format(Locale.getDefault(), "%02d", calendar.get(Calendar.MINUTE))
                    + "   " + String.format(Locale.getDefault(), "%04d", calendar.get(Calendar.YEAR))
                    + "-" + String.format(Locale.getDefault(), "%02d", calendar.get(Calendar.MONTH) + 1)
                    + "-" + String.format(Locale.getDefault(), "%02d", calendar.get(Calendar.DAY_OF_MONTH));
            mTime.setText(time);
            switch (notificationMessage.getAction()) {
                case Constants.PushNotification.TYPE_NOTIFICATION:
                    mButtonRating.setVisibility(View.GONE);
                    break;
                case Constants.PushNotification.TYPE_REMIND:
                    mButtonRating.setVisibility(View.GONE);
                    break;
                case Constants.PushNotification.TYPE_REQUEST_REVIEW:
                    mButtonRating.setVisibility(View.VISIBLE);
                    mButtonRating.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            DialogRating dialogRating = new DialogRating(context, new DialogRating.IOnClickListenerRating() {

                                @Override
                                public void onButtonRating(float rating, String note, int currentPosition) {

                                }
                            });
                            dialogRating.show(0, 0);
                        }
                    });

                    break;
                default:
                    break;
            }
        }

        private void calculatorTime(long time) {

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Japan"));
            calendar.setTimeInMillis(time);
            Calendar calendarCurrent = Calendar.getInstance(TimeZone.getTimeZone("Asia/Japan"));
            calendar.setTimeInMillis(System.currentTimeMillis());

            CharSequence result = DateUtils.getRelativeTimeSpanString(calendar.getTimeInMillis(), calendarCurrent.getTimeInMillis(), 0);
            Logger.d("PushNotificationAdapter", result.toString());
//            Calendar calendarCurrent = Calendar.getInstance();
//            calendarCurrent.setTimeInMillis(System.currentTimeMillis());
//            long distanceTime = Math.abs(System.currentTimeMillis() - time);
//            Calendar calendarDistance = Calendar.getInstance();
//            calendarDistance.setTimeInMillis(distanceTime);
//
//            int year = calendarDistance.get(Calendar.YEAR);
//            int month = calendarDistance.get(Calendar.MONTH) + 1;
//            int day = calendarDistance.get(Calendar.DAY_OF_MONTH);
//            int hour = calendarDistance.get(Calendar.HOUR_OF_DAY);
//            int minute = calendarDistance.get(Calendar.MINUTE);
//            int second = calendarDistance.get(Calendar.SECOND);

//            Logger.d("PushNotificationAdapter", year + "-" + month + "-" + day + "  " + hour + "-" + minute + "-" + second);
        }
    }

    public interface IEndOfListView {

        void onEndOfListView();
    }

    public void setListenerEndOfListView(IEndOfListView mCallback) {
        this.mCallback = mCallback;
    }
}
