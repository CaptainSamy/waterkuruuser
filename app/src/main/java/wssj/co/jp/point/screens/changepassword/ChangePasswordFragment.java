package wssj.co.jp.point.screens.changepassword;

import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import wssj.co.jp.point.R;
import wssj.co.jp.point.screens.IMainView;
import wssj.co.jp.point.screens.base.BaseFragment;

/**
 * Created by Nguyen Huu Ta on 5/7/2017.
 */

public class ChangePasswordFragment extends BaseFragment<IChangePasswordView, ChangePasswordPresenter> implements IChangePasswordView {

    private static final String TAG = "ChangePasswordFragment";

    private EditText mInputCurrentPassword, mInputNewPassword, mInputConfirmPassword;

    private TextView mButtonChangePassword;

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_CHANGE_PASSWORD;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_change_password;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_screen_change_password);
    }

    @Override
    protected ChangePasswordPresenter onCreatePresenter(IChangePasswordView view) {
        return new ChangePasswordPresenter(view);
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
    public boolean isDisplayExtraNavigationButton() {
        return false;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected IChangePasswordView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        super.initViews(rootView);
        mInputCurrentPassword = (EditText) rootView.findViewById(R.id.inputCurrentPassword);
        mInputNewPassword = (EditText) rootView.findViewById(R.id.inputNewPassword);
        mInputConfirmPassword = (EditText) rootView.findViewById(R.id.inputConfirmPassword);
        mButtonChangePassword = (TextView) rootView.findViewById(R.id.buttonChangePassword);
    }

    @Override
    protected void initAction() {
        super.initAction();
        mButtonChangePassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String currentPassword = mInputCurrentPassword.getText().toString().trim();
                String newPassword = mInputNewPassword.getText().toString().trim();
                String confirmPassword = mInputConfirmPassword.getText().toString().trim();
                getPresenter().onChangePasswordClicked(currentPassword, newPassword, confirmPassword);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onValidateFailure(String message) {
        showToast(message);
    }

    @Override
    public void onChangePasswordSuccess(String message) {
        showToast(message);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                getActivityCallback().displayScreen(IMainView.FRAGMENT_HOME, true, false);
            }
        }, 1000);
    }

    @Override
    public void onChangePasswordFailure(String message) {
        showToast(message);
    }
}
