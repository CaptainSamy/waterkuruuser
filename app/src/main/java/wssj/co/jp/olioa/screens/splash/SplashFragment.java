package wssj.co.jp.olioa.screens.splash;

import android.os.Handler;
import android.view.View;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.widget.dialog.ProgressLayout;
import wssj.co.jp.olioa.widget.dialog.SpotsDialog;

/**
 * Created by Nguyen Huu Ta on 5/6/2017.
 */

public class SplashFragment extends BaseFragment<ISplashView, SplashPresenter> implements ISplashView {

    private static final String TAG = "SplashFragment";

    private SpotsDialog mDialog;

    @Override
    protected String getLogTag() {
        return TAG;
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
    public boolean isDisplayBottomNavigationMenu() {
        return false;
    }

    @Override
    public boolean isDisplayActionBar() {
        return false;
    }

    @Override
    public boolean isDisplayIconNotification() {
        return false;
    }

    @Override
    public boolean isEnableDrawableLayout() {
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
        ProgressLayout progressLayout = (ProgressLayout) rootView.findViewById(R.id.dmax_spots_progress);
        mDialog = new SpotsDialog(getActivity(), progressLayout);
    }

    @Override
    protected void initData() {
        getPresenter().onCreate();
    }

    @Override
    public void displayScreen(final int fragmentId) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                getActivityCallback().displayScreen(fragmentId, true, false);
            }
        }, Constants.TIME_WAITING_SPLASH);
    }

    @Override
    public void onDestroyView() {
        mDialog.onStop();
        super.onDestroyView();
    }
}