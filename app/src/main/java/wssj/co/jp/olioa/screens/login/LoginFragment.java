package wssj.co.jp.olioa.screens.login;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

public class LoginFragment extends BaseFragment<ILoginView, LoginPresenter> implements ILoginView, View.OnClickListener {

    private static final String TAG = "LoginFragment";

    private EditText mInputUserId, mInputPassword;

    private TextView mButtonLogin, mTextResetPassword;

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_LOGIN;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_login;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.button_login);
    }

    @Override
    protected LoginPresenter onCreatePresenter(ILoginView view) {
        return new LoginPresenter(view);
    }

    @Override
    public boolean isDisplayActionBar() {
        return true;
    }

    @Override
    public boolean isDisplayExtraNavigationButton() {
        return false;
    }

    @Override
    public boolean isEnableDrawableLayout() {
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
    protected ILoginView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        mInputUserId = (EditText) rootView.findViewById(R.id.user_name_edit_text);
        mInputPassword = (EditText) rootView.findViewById(R.id.password_edit_text);
        mButtonLogin = (TextView) rootView.findViewById(R.id.tvLogin);
        mTextResetPassword = (TextView) rootView.findViewById(R.id.tvResetPassword);
//        mInputUserId.setText("rakasama1");
//        mInputPassword.setText("mandem123");
    }

    @Override
    protected void initAction() {
        mButtonLogin.setOnClickListener(this);
        mTextResetPassword.setOnClickListener(this);

    }

    @Override
    public void showListMessageValidate(ErrorMessage errorMessage) {
        Toast.makeText(getActivityContext(), errorMessage.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayHomeScreen() {
        getActivityCallback().clearBackStack();
        getActivityCallback().displayScreen(IMainView.FRAGMENT_TIMELINE, false, false);
    }

    @Override
    public void displayResetPasswordScreen() {
        getActivityCallback().displayScreen(IMainView.FRAGMENT_RESET_PASSWORD, true, true);
    }

    @Override
    public void displayChangePasswordScreen() {
        getActivityCallback().displayScreen(IMainView.FRAGMENT_CHANGE_PASSWORD_CODE, true, true);
    }

    @Override
    public void showLoginFailureMessage(String message) {
        showToast(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvLogin:
                String userId = mInputUserId.getText().toString();
                String password = mInputPassword.getText().toString();
                getPresenter().onLoginButtonClicked(userId, password);
                break;
            case R.id.tvResetPassword:
                getPresenter().onResetPasswordClicked();
                break;
        }
    }
}
