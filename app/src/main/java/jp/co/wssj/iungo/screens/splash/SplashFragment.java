package jp.co.wssj.iungo.screens.splash;

import android.util.Log;
import android.view.View;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import jp.co.wssj.iungo.BuildConfig;
import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.auth.CheckVersionAppResponse;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.splash.dialog.DialogAskUpdate;
import jp.co.wssj.iungo.utils.Constants;

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
        TwitterConfig config = new TwitterConfig.Builder(getActivityContext())
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(Constants.Introduction.TWITTER_KEY, Constants.Introduction.TWITTER_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);
        getPresenter().onCreate(BuildConfig.VERSION_CODE);
    }

    @Override
    public void showDialog(CheckVersionAppResponse.CheckVersionAppData response) {
        DialogAskUpdate dialogAskUpdate = new DialogAskUpdate(getActivityContext(), response.getVersionInfo().isRequired(), getActivityCallback());
        dialogAskUpdate.showDialog();
    }

    @Override
    public void displayScreen(int fragmentId) {
        getActivityCallback().displayScreen(fragmentId, false, false, null);
        getActivityCallback().clearBackStack();
    }
}
