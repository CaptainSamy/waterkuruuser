package jp.co.wssj.iungo.screens.pushnotification;

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

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.utils.Utils;

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
        if (position == (getCount() - 1) && mCallback != null) {
            mCallback.onEndOfListView();
        }
        return convertView;
    }

    private static class ViewHolder {

        TextView mTitle, mTime;

        ImageView mImageCompany;

        ViewHolder(View root) {
            mTitle = (TextView) root.findViewById(R.id.title_notification);
            mTime = (TextView) root.findViewById(R.id.time_notification);
            mImageCompany = (ImageView) root.findViewById(R.id.iconNotification);
        }

        void fillDataToView(final Context context, final NotificationMessage notificationMessage) {
            mTitle.setText(notificationMessage.getTitle());
            Utils.fillImage(context, notificationMessage.getLogo(), mImageCompany);
            long timeSendPush = notificationMessage.getPushTime();
            if (timeSendPush != 0) {
                String time = Utils.distanceTimes(timeSendPush);
                if (!TextUtils.isEmpty(time)) {
                    mTime.setText(time);
                }
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
