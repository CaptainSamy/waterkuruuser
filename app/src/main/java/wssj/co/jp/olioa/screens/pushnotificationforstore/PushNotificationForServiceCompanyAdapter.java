package wssj.co.jp.olioa.screens.pushnotificationforstore;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.firebase.NotificationMessage;
import wssj.co.jp.olioa.utils.Utils;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationForServiceCompanyAdapter extends ArrayAdapter<NotificationMessage> {

    public PushNotificationForServiceCompanyAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<NotificationMessage> objects) {
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

        CircleImageView mImageCompany;

        ViewHolder(View root) {
            mTitle = (TextView) root.findViewById(R.id.title_notification);
            mTime = (TextView) root.findViewById(R.id.time_notification);
            mImageCompany = (CircleImageView) root.findViewById(R.id.iconNotification);
        }

        void fillDataToView(final Context context, final NotificationMessage notificationMessage) {
            mTitle.setText(notificationMessage.getTitle());
            String time = Utils.distanceTimes(notificationMessage.getPushTime());
            Utils.fillImage(context, notificationMessage.getLogo(), mImageCompany,R.drawable.logo_app);
            if (!TextUtils.isEmpty(time)) {
                mTime.setText(time);
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
