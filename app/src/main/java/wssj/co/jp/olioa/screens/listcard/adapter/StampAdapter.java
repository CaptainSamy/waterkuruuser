package wssj.co.jp.olioa.screens.listcard.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.stamp.ListCardResponse.ListCardData.CardData.StampData;
import wssj.co.jp.olioa.screens.dialograting.DialogRating;
import wssj.co.jp.olioa.utils.Utils;

/**
 * Created by HieuPT on 6/1/2017.
 */

public class StampAdapter extends ArrayAdapter<StampData> {

    private final int mCount;

    private final LayoutInflater mInflater;

    private final List<StampData> mObjects;

    private DialogRating mDialogRating;

    public StampAdapter(@NonNull Context context, @NonNull List<StampData> objects, int count) {
        super(context, 0, objects);
        mCount = count;
        mInflater = LayoutInflater.from(context);
        mObjects = objects;
    }

    public StampAdapter(@NonNull Context context, int count) {
        super(context, 0);
        mCount = count;
        mInflater = LayoutInflater.from(context);
        mObjects = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Nullable
    @Override
    public StampData getItem(int position) {
        if (position < mObjects.size()) {
            return mObjects.get(position);
        } else {
            return null;
        }
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder vh;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_stamp_adapter, parent, false);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        vh.bindData(getContext(), getItem(position), position);
        vh.mButtonRating.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mDialogRating == null) {
                    mDialogRating = new DialogRating(getContext(), new DialogRating.IOnClickListenerRating() {

                        @Override
                        public void onButtonRating(float rating, String note, int currentPosition) {
                            mObjects.get(currentPosition).setRating(rating);
                            mObjects.get(currentPosition).setContentRating(note);
                            notifyDataSetChanged();
                        }
                    });
                }
                mDialogRating.show(position, mObjects.get(position).getStampId());
            }
        });
        vh.mStampChecked.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StampData stampData = mObjects.get(position);
                if (stampData != null && (stampData.getRating() > -1 || !TextUtils.isEmpty(stampData.getContentRating()))) {
                    if (mDialogRating == null) {
                        mDialogRating = new DialogRating(getContext(), new DialogRating.IOnClickListenerRating() {

                            @Override
                            public void onButtonRating(float rating, String note, int currentPosition) {
                                mObjects.get(currentPosition).setRating(rating);
                                mObjects.get(currentPosition).setContentRating(note);
                                notifyDataSetChanged();
                            }
                        });
                    }

                    mDialogRating.show(position, stampData.getStampId(), stampData.getRating(), stampData.getContentRating());
                }
            }
        });
        return view;
    }

    private static class ViewHolder {

        private TextView mTextIndex, mStampChecked, mButtonRating;

        private RelativeLayout mLayoutRating;

        SimpleRatingBar mRating;

        ViewHolder(View view) {
            mTextIndex = (TextView) view.findViewById(R.id.tvIndex);
            mStampChecked = (TextView) view.findViewById(R.id.tvStampChecked);
            mButtonRating = (TextView) view.findViewById(R.id.tvRating);
            mRating = (SimpleRatingBar) view.findViewById(R.id.rating);
            mLayoutRating = (RelativeLayout) view.findViewById(R.id.rlRating);
        }

        private void bindData(Context context, StampData data, int position) {
            int widthStamp = calculateStampWidth(context);
            if (widthStamp != 0) {
                mTextIndex.setLayoutParams(new RelativeLayout.LayoutParams(widthStamp, widthStamp));
            } else {
                mTextIndex.setLayoutParams(new RelativeLayout.LayoutParams(Utils.convertDpToPixel(context, 70), Utils.convertDpToPixel(context, 70)));
            }
            mTextIndex.setText(String.format(Locale.getDefault(), "%02d", position + 1));
            if (data != null) {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                calendar.setTimeInMillis(data.getCreated());
                mStampChecked.setVisibility(View.VISIBLE);
                mTextIndex.setVisibility(View.INVISIBLE);
                mStampChecked.setText(context.getString(R.string.stamp_date,
                        String.format(Locale.getDefault(), "%02d", calendar.get(Calendar.MONTH) + 1),
                        String.format(Locale.getDefault(), "%02d", calendar.get(Calendar.DAY_OF_MONTH))
                ));
                mLayoutRating.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(data.getContentRating()) && data.getRating() == -1) {
                    mRating.setVisibility(View.INVISIBLE);
                    mButtonRating.setVisibility(View.VISIBLE);
                } else {
                    mRating.setVisibility(View.VISIBLE);
                    mRating.setRating(data.getRating());
                    mButtonRating.setVisibility(View.INVISIBLE);
                }

            } else {
                mLayoutRating.setVisibility(View.GONE);
                mStampChecked.setVisibility(View.GONE);
            }
        }

        private int calculateStampWidth(Context context) {
            int marginLR = 2 * Utils.convertDpToPixel(context, 5);
            int widthScreen = context.getResources().getDisplayMetrics().widthPixels;
            int widthLine = 6 * Utils.convertDpToPixel(context, 1);
            int widthStamp = 0;
            if (marginLR != 0 && widthScreen != 0 && widthLine != 0) {
                widthStamp = (widthScreen - marginLR - widthLine) / 5;
            }
            return widthStamp;
        }
    }
}
