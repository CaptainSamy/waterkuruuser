package wssj.co.jp.olioa.screens.pushnotification.detail;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.entities.PushNotification;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;
import wssj.co.jp.olioa.utils.DateConvert;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationDetailFragment extends BaseFragment<IPushNotificationDetailView, PushNotificationDetailPresenter> implements IPushNotificationDetailView {

    public static final String TAG = "PushNotificationDetailServiceCompanyFragment";

    public static final String NOTIFICATION_ARG = "notification";

    private TextView mTitle, mTime;

    private WebView mBody;

    private PushNotification pushNotification;

    public static PushNotificationDetailFragment newInstance(Bundle bundle) {
        PushNotificationDetailFragment fragment = new PushNotificationDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return true;
    }

    @Override
    protected String getLogTag() {
        return "PushNotificationDetailServiceCompanyFragment";
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
    public String getAppBarTitle() {
        return getString(R.string.title_push_detail);
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
        mTime = (TextView) rootView.findViewById(R.id.time_notification);
        mBody = (WebView) rootView.findViewById(R.id.body_notification);

    }

    @Override
    protected void initAction() {
        super.initAction();
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            pushNotification = bundle.getParcelable(NOTIFICATION_ARG);
            if (pushNotification != null) {
                mTitle.setText(pushNotification.getTitle());
                mTime.setText(DateConvert.formatToString(DateConvert.DATE_FULL_FORMAT, pushNotification.getSendTime()));
                mBody.loadDataWithBaseURL(null, pushNotification.getBody(), "text/html", "UTF-8", null);
            }
        }
    }


}
