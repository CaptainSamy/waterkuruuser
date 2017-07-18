package jp.co.wssj.iungo.screens.splash;

import android.view.View;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;

/**
 * Created by Nguyen Huu Ta on 5/6/2017.
 */

public class SplashFragment extends BaseFragment<ISplashView, SplashPresenter> implements ISplashView {

    @Override
    protected String getLogTag() {
        return null;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_SPLASH_SCREEN;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.splash_fragment;
    }

    @Override
    public boolean isDisplayActionBar() {
        return false;
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return false;
    }

    @Override
    public boolean isDisplayIconNotification() {
        return false;
    }

    @Override
    protected SplashPresenter onCreatePresenter(ISplashView view) {
        return new SplashPresenter(view);
    }

    @Override
    protected ISplashView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        super.initViews(rootView);
    }

    @Override
    protected void initAction() {
        super.initAction();
    }

    @Override
    protected void initData() {
        super.initData();
        getPresenter().onCreate();
    }

    @Override
    public void displayScreen(int fragmentId) {
        getActivityCallback().displayScreen(fragmentId, false, false, null);
        getActivityCallback().clearBackStack();
    }
}
