package wssj.co.jp.point.screens.pushnotification.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import wssj.co.jp.point.R;
import wssj.co.jp.point.model.firebase.NotificationMessage;

/**
 * Created by Nguyen Huu Ta on 28/6/2017.
 */

public class NotificationAdapter extends ArrayAdapter<NotificationMessage> {

    public NotificationAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<NotificationMessage> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_notification, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.bind(getItem(position));
        return convertView;
    }

    public class ViewHolder {

        public ViewHolder(View view) {

        }

        public void bind(NotificationMessage notificationMessage) {

        }
    }
}
