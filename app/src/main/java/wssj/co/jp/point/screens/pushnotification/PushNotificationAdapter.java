package wssj.co.jp.point.screens.pushnotification;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import wssj.co.jp.point.R;
import wssj.co.jp.point.model.firebase.NotificationMessage;
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

        TextView mTitle, mBody, mDate, mTime, mRating;

        ViewHolder(View root) {
            mTitle = (TextView) root.findViewById(R.id.title_notification);
            mBody = (TextView) root.findViewById(R.id.body_notification);
            mDate = (TextView) root.findViewById(R.id.date_notification);
            mTime = (TextView) root.findViewById(R.id.time_notification);
            mRating = (TextView) root.findViewById(R.id.buttonRating);
        }

        void fillDataToView(Context context, NotificationMessage notificationMessage) {
            mTitle.setText(notificationMessage.getTitle());
            mBody.setText(notificationMessage.getMessage());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(notificationMessage.getPushTime());
            String date = Utils.get2NumbericString(calendar.get(Calendar.DAY_OF_MONTH)) + "/" + Utils.get2NumbericString(calendar.get(Calendar.MONTH) + 1);
            String time = Utils.get2NumbericString(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + Utils.get2NumbericString(calendar.get(Calendar.MINUTE));
            mDate.setText(context.getString(R.string.notification_date, date));
            mTime.setText(time);
        }
    }

    public interface IEndOfListView {

        void onEndOfListView();
    }

    public void setListenerEndOfListView(IEndOfListView mCallback) {
        this.mCallback = mCallback;
    }
}
