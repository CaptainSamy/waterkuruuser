package jp.co.wssj.iungo.screens.termofservice.fragment;

import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.termofservice.ITermOfServicesView;
import jp.co.wssj.iungo.screens.termofservice.TermOfServicePresenter;

/**
 * Created by Nguyen Huu Ta on 5/6/2017.
 */

public class TermOfServiceFragment extends BaseFragment<ITermOfServicesView, TermOfServicePresenter> implements ITermOfServicesView {

    private static final String TAG = "TermOfServiceFragment";

    private WebView mContentTermOfService;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_TERM_OF_SERVICE;
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
    protected ITermOfServicesView onCreateView() {
        return this;
    }

    @Override
    public int getNavigationMenuId() {
        return R.id.menu_term_of_service;
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
            WebSettings webSettings = mContentTermOfService.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDisplayZoomControls(false);
            mContentTermOfService.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
        }
    }

    @Override
    public void onGetTermOfServiceFailure(String message) {
        showToast(message);
    }
}
