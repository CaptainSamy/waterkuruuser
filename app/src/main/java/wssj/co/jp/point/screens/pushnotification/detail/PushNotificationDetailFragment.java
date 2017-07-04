package wssj.co.jp.point.screens.pushnotification.detail;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

import wssj.co.jp.point.R;
import wssj.co.jp.point.model.firebase.NotificationMessage;
import wssj.co.jp.point.screens.IMainView;
import wssj.co.jp.point.screens.base.BaseFragment;
import wssj.co.jp.point.utils.Utils;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationDetailFragment extends BaseFragment<IPushNotificationDetailView, PushNotificationDetailPresenter> implements IPushNotificationDetailView {

    public static final String NOTIFICATION_ARG = "notification";

    private TextView mTitle, mBody, mDate, mTime;

    public static PushNotificationDetailFragment newInstance(Bundle bundle) {
        PushNotificationDetailFragment fragment = new PushNotificationDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getLogTag() {
        return "PushNotificationDetailFragment";
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_PUSH_NOTIFICATION_DETAIL;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_notification_detail;
    }

    @Override
    public boolean isDisplayIconNotification() {
        return false;
    }

    @Override
    protected PushNotificationDetailPresenter onCreatePresenter(IPushNotificationDetailView view) {
        return new PushNotificationDetailPresenter(view);
    }

    @Override
    protected IPushNotificationDetailView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        mTitle = (TextView) rootView.findViewById(R.id.title_notification);
        mBody = (TextView) rootView.findViewById(R.id.body_notification);
        mDate = (TextView) rootView.findViewById(R.id.date_notification);
        mTime = (TextView) rootView.findViewById(R.id.time_notification);
    }

    @Override
    protected void initData() {
        NotificationMessage notificationMessage = (NotificationMessage) getArguments().getSerializable(NOTIFICATION_ARG);
        mTitle.setText(notificationMessage.getTitle());
        mBody.setText(notificationMessage.getMessage());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(notificationMessage.getPushTime());
        String date = Utils.get2NumbericString(calendar.get(Calendar.DAY_OF_MONTH)) + "/" + Utils.get2NumbericString(calendar.get(Calendar.MONTH) + 1);
        String time = Utils.get2NumbericString(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + Utils.get2NumbericString(calendar.get(Calendar.MINUTE));
        mDate.setText(getString(R.string.notification_date, date));
        mTime.setText(time);
    }
}
