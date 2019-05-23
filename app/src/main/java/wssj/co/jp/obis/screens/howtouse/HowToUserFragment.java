package wssj.co.jp.obis.screens.howtouse;

import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;

import wssj.co.jp.obis.R;
import wssj.co.jp.obis.screens.IMainView;
import wssj.co.jp.obis.screens.base.BaseFragment;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class HowToUserFragment extends BaseFragment<IHowToUseView, HowToUsePresenter> implements IHowToUseView {

    private static String TAG = "HowToUserFragment";

    private WebView mWebHowUseAPp;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_HOW_TO_USE;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_how_to_use;
    }

    @Override
    protected HowToUsePresenter onCreatePresenter(IHowToUseView view) {
        return new HowToUsePresenter(view);
    }

    @Override
    protected IHowToUseView onCreateView() {
        return this;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_screen_how_to_use);
    }

    @Override
    public int getNavigationMenuId() {
        return R.id.menu_how_to_use;
    }

    @Override
    protected void initViews(View rootView) {
        super.initViews(rootView);
        mWebHowUseAPp = (WebView) rootView.findViewById(R.id.howUseApp);
    }

    @Override
    protected void initData() {
        super.initData();
        getPresenter().getHowUseApp();
    }

    @Override
    protected void initAction() {
        super.initAction();
    }

    @Override
    public void onHowUseAppSuccess(String html) {
        if (!TextUtils.isEmpty(html)) {
            mWebHowUseAPp.getSettings().setJavaScriptEnabled(true);
            mWebHowUseAPp.getSettings().setBuiltInZoomControls(true);
            mWebHowUseAPp.getSettings().setDisplayZoomControls(false);
            mWebHowUseAPp.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
        }
    }

    @Override
    public void onHowUseAppFailure(String message) {
        showToast(message);
    }
}
