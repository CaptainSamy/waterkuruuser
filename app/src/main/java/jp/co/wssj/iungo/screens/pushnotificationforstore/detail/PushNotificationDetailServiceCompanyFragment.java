package jp.co.wssj.iungo.screens.pushnotificationforstore.detail;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.pushnotification.ListPushForServiceCompanyResponse;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.utils.Utils;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationDetailServiceCompanyFragment extends BaseFragment<IPushNotificationDetailServiceCompanyView, PushNotificationDetailServiceCompanyPresenter> implements IPushNotificationDetailServiceCompanyView {

    public static final String TAG = "PushNotificationDetailServiceCompanyFragment";

    public static final String NOTIFICATION_ARG = "notification";

    private TextView mTitle, mTime;

    private WebView mBody;

    private ListPushForServiceCompanyResponse.ListPushForServiceCompany.PushNotification mNotificationMessage;

    public static PushNotificationDetailServiceCompanyFragment newInstance(Bundle bundle) {
        PushNotificationDetailServiceCompanyFragment fragment = new PushNotificationDetailServiceCompanyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getLogTag() {
        return "PushNotificationDetailServiceCompanyFragment";
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_NOTIFICATION_DETAIL_FOR_SERVICE_COMPANY;
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
    public boolean isDisplayIconNotification() {
        return false;
    }

    @Override
    protected PushNotificationDetailServiceCompanyPresenter onCreatePresenter(IPushNotificationDetailServiceCompanyView view) {
        return new PushNotificationDetailServiceCompanyPresenter(view);
    }

    @Override
    protected IPushNotificationDetailServiceCompanyView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        mTitle = (TextView) rootView.findViewById(R.id.title_notification);
        mBody = (WebView) rootView.findViewById(R.id.body_notification);
        mTime = (TextView) rootView.findViewById(R.id.time_notification);
    }

    @Override
    protected void initAction() {
        super.initAction();
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mNotificationMessage = (ListPushForServiceCompanyResponse.ListPushForServiceCompany.PushNotification) bundle.getSerializable(NOTIFICATION_ARG);
            if (mNotificationMessage != null) {
                mTitle.setText(mNotificationMessage.getTitle().trim());
                mBody.getSettings().setJavaScriptEnabled(true);
                mBody.getSettings().setBuiltInZoomControls(true);
                mBody.getSettings().setDisplayZoomControls(false);
                mBody.loadDataWithBaseURL("", mNotificationMessage.getBody(), "text/html", "UTF-8", "");
            }
        }
        String time = Utils.convertLongToTime(mNotificationMessage.getSendTime(), false);
        mTime.setText(time);

    }
}
