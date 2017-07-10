package wssj.co.jp.point.screens.termofservice.fragment;

import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;

import wssj.co.jp.point.R;
import wssj.co.jp.point.screens.IMainView;
import wssj.co.jp.point.screens.base.BaseFragment;
import wssj.co.jp.point.screens.termofservice.ITermOfServicesView;
import wssj.co.jp.point.screens.termofservice.TermOfServicePresenter;

/**
 * Created by Nguyen Huu Ta on 5/6/2017.
 */

public class TermOfServiceFragment extends BaseFragment<ITermOfServicesView, TermOfServicePresenter> implements ITermOfServicesView {

    private static final String TAG = "TermOfServiceFragment";

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
    public int getNavigationMenuID() {
        return R.id.menu_term_of_service;
    }

    @Override
    protected void initViews(View rootView) {
        super.initViews(rootView);
        mContentTermOfService = (WebView) rootView.findViewById(R.id.termOfService);
    }

    private WebView mContentTermOfService;

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
