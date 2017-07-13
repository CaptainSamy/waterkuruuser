package wssj.co.jp.point.screens.pushnotification;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wssj.co.jp.point.R;
import wssj.co.jp.point.model.firebase.NotificationMessage;
import wssj.co.jp.point.screens.dialograting.DialogRating;
import wssj.co.jp.point.utils.Constants;
import wssj.co.jp.point.utils.Utils;

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

        ImageView mImageCompany;

        ViewHolder(View root) {
            mTitle = (TextView) root.findViewById(R.id.title_notification);
            mBody = (TextView) root.findViewById(R.id.body_notification);
            mTime = (TextView) root.findViewById(R.id.time_notification);
            mButtonRating = (TextView) root.findViewById(R.id.buttonRating);
            mImageCompany = (ImageView) root.findViewById(R.id.iconNotification);
        }

        void fillDataToView(final Context context, NotificationMessage notificationMessage) {
            mTitle.setText(notificationMessage.getTitle());

            if (notificationMessage.getMessage().length() > 100) {
                String text = notificationMessage.getMessage().substring(0, 100).trim() + "...";
                mBody.setText(text);
            } else {
                mBody.setText(notificationMessage.getMessage());
            }
            Utils.fillImage(context, notificationMessage.getLogo(), mImageCompany);

            String time = Utils.distanceTimes(notificationMessage.getPushTime());
            if (!TextUtils.isEmpty(time)) {
                mTime.setText(time);
            }
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
    }

    public interface IEndOfListView {

        void onEndOfListView();
    }

    public void setListenerEndOfListView(IEndOfListView mCallback) {
        this.mCallback = mCallback;
    }
}
