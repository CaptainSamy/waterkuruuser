package wssj.co.jp.obis.screens.termofservice.fragment;

import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;

import wssj.co.jp.obis.R;
import wssj.co.jp.obis.screens.termofservice.ITermOfServicesView;
import wssj.co.jp.obis.screens.termofservice.TermOfServicePresenter;
import wssj.co.jp.obis.screens.IMainView;
import wssj.co.jp.obis.screens.base.BaseFragment;

/**
 * Created by Nguyen Huu Ta on 10/7/2017.
 */

public class TermOfServiceNoMenuBottom extends BaseFragment<ITermOfServicesView, TermOfServicePresenter> implements ITermOfServicesView {

    private static final String TAG = "TermOfServiceFragment";

    private WebView mContentTermOfService;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_TERM_OF_SERVICE_N0_BOTTOM;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_term_of_service);
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_term_of_service;
    }

    @Override
    protected TermOfServicePresenter onCreatePresenter(ITermOfServicesView view) {
        return new TermOfServicePresenter(view);
    }

    @Override
    public boolean isDisplayExtraNavigationButton() {
        return false;
    }

    @Override
    public boolean isEnableDrawableLayout() {
        return false;
    }

    @Override
    public boolean isDisplayIconNotification() {
        return false;
    }

    @Override
    protected ITermOfServicesView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        super.initViews(rootView);
        mContentTermOfService = (WebView) rootView.findViewById(R.id.termOfService);
    }

    @Override
    protected void initAction() {
        super.initAction();
    }

    @Override
    protected void initData() {
        super.initData();
        getPresenter().getContentTermOfService();
    }

    @Override
    public void onGetTermOfServiceSuccess(String html) {
        if (!TextUtils.isEmpty(html)) {
            mContentTermOfService.getSettings().setJavaScriptEnabled(true);
            mContentTermOfService.getSettings().setBuiltInZoomControls(true);
            mContentTermOfService.getSettings().setDisplayZoomControls(false);
            mContentTermOfService.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
        }
    }

    @Override
    public void onGetTermOfServiceFailure(String message) {
        showToast(message);
    }
}

