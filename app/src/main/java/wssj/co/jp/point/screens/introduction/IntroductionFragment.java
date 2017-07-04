package wssj.co.jp.point.screens.introduction;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import wssj.co.jp.point.R;
import wssj.co.jp.point.screens.IMainView;
import wssj.co.jp.point.screens.base.BaseFragment;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

public class IntroductionFragment extends BaseFragment<IIntroductionView, IntroductionPresenter> implements IIntroductionView, View.OnClickListener {

    private static final String TAG = "IntroductionFragment";

    private TextView mButtonIntroduction, mButtonLogin, mButtonRegister, mButtonHowToUsed;

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_INTRODUCTION_SCREEN;
    }

    @Override
    public String getAppBarTitle() {
        return "";
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
        mButtonHowToUsed = (TextView) rootView.findViewById(R.id.tvHowToUsed);
    }

    @Override
    protected void initAction() {
        mButtonLogin.setOnClickListener(this);
        mButtonRegister.setOnClickListener(this);
        mButtonHowToUsed.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();

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
            case R.id.tvHowToUsed:
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
}
