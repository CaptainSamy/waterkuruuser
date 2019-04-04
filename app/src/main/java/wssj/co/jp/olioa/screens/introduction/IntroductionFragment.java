package wssj.co.jp.olioa.screens.introduction;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;
import wssj.co.jp.olioa.utils.Constants;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

public class IntroductionFragment extends BaseFragment<IIntroductionView, IntroductionPresenter>
        implements IIntroductionView, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "IntroductionFragment";

    private TextView mButtonLogin, mButtonRegister;

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_INTRODUCTION_SCREEN;
    }

    @Override
    public String getAppBarTitle() {
        return Constants.EMPTY_STRING;
    }

    @Override
    protected String getLogTag() {
        return TAG;
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
    protected boolean isRetainState() {
        return true;
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return false;
    }

    @Override
    public int getActionBarColor() {
        return Color.TRANSPARENT;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.introduction_fragment;
    }

    @Override
    protected IntroductionPresenter onCreatePresenter(IIntroductionView view) {
        return new IntroductionPresenter(view);
    }

    @Override
    protected IIntroductionView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        mButtonLogin = (TextView) rootView.findViewById(R.id.tvLogin);
        mButtonRegister = (TextView) rootView.findViewById(R.id.tvRegisterAccount);

    }

    @Override
    protected void initAction() {
        mButtonLogin.setOnClickListener(this);
        mButtonRegister.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        String text = "スマホでかんたん<font color='#D9594C'>ツナグ</font>アプリ";

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvLogin:
                getPresenter().onLoginButtonClicked();
                break;
            case R.id.tvRegisterAccount:
                getPresenter().onRegisterButtonClicked();
                break;
        }
    }

    @Override
    public void displayLoginScreen() {
        getActivityCallback().displayScreen(IMainView.FRAGMENT_LOGIN, true, true);
    }

    @Override
    public void displayRegisterScreen() {
        getActivityCallback().displayScreen(IMainView.FRAGMENT_REGISTER_ACCOUNT, true, true);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void displayHomeScreen() {
        getActivityCallback().clearBackStack();
        getActivityCallback().displayScreen(IMainView.FRAGMENT_LIST_STORE_PUSH, false, false);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
