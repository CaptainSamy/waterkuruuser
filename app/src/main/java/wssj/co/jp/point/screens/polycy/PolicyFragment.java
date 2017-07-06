package wssj.co.jp.point.screens.polycy;

import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;

import wssj.co.jp.point.R;
import wssj.co.jp.point.screens.IMainView;
import wssj.co.jp.point.screens.base.BaseFragment;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class PolicyFragment extends BaseFragment<IPolicyView, PolicyPresenter> implements IPolicyView {

    private static String TAG = "PolicyFragment";

    private WebView mPolicy;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_POLICY;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_policy;
    }

    @Override
    protected PolicyPresenter onCreatePresenter(IPolicyView view) {
        return new PolicyPresenter(view);
    }

    @Override
    protected IPolicyView onCreateView() {
        return this;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.privacy_policy);
    }

    @Override
    public int getNavigationMenuID() {
        return R.id.menu_privacy_policy;
    }

    @Override
    protected void initViews(View rootView) {
        mPolicy = (WebView) rootView.findViewById(R.id.policy);
    }

    @Override
    protected void initData() {
        getPresenter().getPolicy();
    }

    @Override
    public void onPolicySuccess(String html) {
        if (!TextUtils.isEmpty(html)) {
            mPolicy.getSettings().setJavaScriptEnabled(true);
            mPolicy.getSettings().setBuiltInZoomControls(true);
            mPolicy.getSettings().setDisplayZoomControls(false);
            mPolicy.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
        }
    }

    @Override
    public void onPolicyFailure(String message) {
        showToast(message);
    }


}
