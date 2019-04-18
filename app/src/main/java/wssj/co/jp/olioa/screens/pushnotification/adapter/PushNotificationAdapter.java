package wssj.co.jp.olioa.screens.pushnotification.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.entities.PushNotification;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.utils.Utils;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationAdapter extends BaseAdapter {

    private List<PushNotification> mListPush, mListPushTemp;

    private Context mContext;

    private IEndOfListView mCallback;

    private boolean mIsAllowOnLoadMore = true;

    private boolean mIsEndOfPage;


    public PushNotificationAdapter(Context context, List<PushNotification> objects) {
        mContext = context;
        mListPush = objects;
        mListPushTemp = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mListPush == null ? 0 : mListPush.size();
    }

    @Nullable
    @Override
    public PushNotification getItem(int position) {
        return mListPush.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_push_notification, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.fillDataToView(mContext, getItem(position));
        if (position == (getCount() - 1) && mCallback != null && mIsAllowOnLoadMore && !mIsEndOfPage && getCount() >= Constants.LIMIT) {
            mCallback.onEndOfListView();
        }
        return convertView;
    }

    public static class ViewHolder {

        private TextView mTitle, mTime;

        private CircleImageView mImageCompany;

        ViewHolder(View root) {
            mTitle = (TextView) root.findViewById(R.id.title_notification);
            mTime = (TextView) root.findViewById(R.id.time_notification);
            mImageCompany = (CircleImageView) root.findViewById(R.id.iconNotification);
        }

        void fillDataToView(final Context context, final PushNotification notificationMessage) {
            mTitle.setText(notificationMessage.getTitle());
            Utils.fillImage(context, notificationMessage.getLogo(), mImageCompany, R.drawable.logo_app);
            long timeSendPush = notificationMessage.getSendTime();
            if (timeSendPush != 0) {
                String time = Utils.distanceTimes(timeSendPush);
                if (!TextUtils.isEmpty(time)) {
                    mTime.setText(time);
                }
            }

        }
    }


    public void setListPushTemp(List<PushNotification> objects) {
        mListPushTemp.clear();
        mListPushTemp.addAll(objects);
    }

    public interface IEndOfListView {

        void onEndOfListView();
    }

    public void setIsAllowOnLoadMore(boolean isAllowOnLoadMore) {
        this.mIsAllowOnLoadMore = isAllowOnLoadMore;
    }

    public void showListPushSearch() {

    }

    public void setListenerEndOfListView(IEndOfListView mCallback) {
        this.mCallback = mCallback;
    }

    public void setIsEndOfPage(boolean mIsEndOfPage) {
        this.mIsEndOfPage = mIsEndOfPage;
    }
}
