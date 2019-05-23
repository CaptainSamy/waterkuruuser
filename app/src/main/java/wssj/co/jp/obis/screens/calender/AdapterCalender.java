package wssj.co.jp.obis.screens.calender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import wssj.co.jp.obis.R;
import wssj.co.jp.obis.model.firebase.NotificationMessage;
import wssj.co.jp.obis.utils.Logger;
import wssj.co.jp.obis.utils.Utils;

/**
 * Created by Thanghn on 12/20/2017.
 */

public class AdapterCalender extends BaseAdapter {
    Context mContext;
    int myLayout;
    List<NotificationMessage> arrayNotificationMessages;

    public AdapterCalender(Context context, int layout, List<NotificationMessage> notificationMessageList) {
        mContext = context;
        myLayout = layout;
        arrayNotificationMessages = new ArrayList<>();
        arrayNotificationMessages.addAll(notificationMessageList);
    }
    public void updateList(List<NotificationMessage> notificationMessageList){
        if (notificationMessageList == null){
            arrayNotificationMessages.clear();
        }else {
            arrayNotificationMessages.clear();
            arrayNotificationMessages.addAll(notificationMessageList);
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        Logger.d("count", arrayNotificationMessages.size() + "");
        return arrayNotificationMessages.size();
    }

    @Override
    public NotificationMessage getItem(int position) {
        if (arrayNotificationMessages != null){
            return arrayNotificationMessages.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        CircleImageView mImageCompany;
        TextView txtTitle;
        TextView txtTime;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(myLayout, null);
            holder = new ViewHolder();

            holder.txtTitle = (TextView) convertView.findViewById(R.id.title_notification);
            holder.txtTime = (TextView) convertView.findViewById(R.id.time_notification);
            holder.mImageCompany = (CircleImageView) convertView.findViewById(R.id.iconNotification);

            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        NotificationMessage notificationMessage = getItem(position);
        if (notificationMessage != null) {
            holder.txtTitle.setText(notificationMessage.getTitle());
            String time = Utils.distanceTimes(notificationMessage.getPushTime());
            holder.txtTime.setText(time);
            Utils.fillImage(mContext, notificationMessage.getLogo(), holder.mImageCompany, R.drawable.logo_app);
        }

        return convertView;
    }
}
