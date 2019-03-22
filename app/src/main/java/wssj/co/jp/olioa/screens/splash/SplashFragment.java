package wssj.co.jp.olioa.screens.splash;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.auth.CheckVersionAppResponse;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.widget.dialog.ProgressLayout;
import wssj.co.jp.olioa.widget.dialog.SpotsDialog;
import wssj.co.jp.olioa.screens.splash.dialog.DialogAskUpdate;

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
        TwitterConfig config = new TwitterConfig.Builder(getActivityContext())
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(Constants.Introduction.TWITTER_KEY, Constants.Introduction.TWITTER_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);
        getPresenter().onCreate();
    }

    @Override
    public void showDialog(CheckVersionAppResponse.CheckVersionAppData response) {
        if (response != null && response.getServerInfo() != null) {
            String status = response.getServerInfo().getStatus();
            boolean isShowDialog = status.equals(DialogAskUpdate.STATUS_RUNNING) && !response.isHasUpdate();
            if (!isShowDialog) {
                DialogAskUpdate dialogAskUpdate = new DialogAskUpdate(getActivityContext(), response, getActivityCallback());
                dialogAskUpdate.showDialog();
            } else {
                displayScreen(IMainView.FRAGMENT_TIMELINE);
            }
        }
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
    public void displayScreen(int fragmentId, Bundle bundle) {
        getActivityCallback().displayScreen(fragmentId, true, false, bundle);
    }

    @Override
    public void onDestroyView() {
        mDialog.onStop();
        super.onDestroyView();
    }
}
