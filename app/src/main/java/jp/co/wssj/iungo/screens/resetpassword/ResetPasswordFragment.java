package jp.co.wssj.iungo.screens.resetpassword;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

public class ResetPasswordFragment extends BaseFragment<IResetPasswordView, ResetPasswordPresenter> implements IResetPasswordView, View.OnClickListener {

    private static final String TAG = "ResetPasswordFragment";

    private EditText mInputUserId;

    private TextView mButtonReset;

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_RESET_PASSWORD;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_reset_password;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_screen_reset_password);
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
    public boolean isDisplayExtraNavigationButton() {
        return false;
    }

    @Override
    protected ResetPasswordPresenter onCreatePresenter(IResetPasswordView view) {
        return new ResetPasswordPresenter(view);
    }

    @Override
    protected IResetPasswordView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        mInputUserId = (EditText) rootView.findViewById(R.id.etUserId);
        mButtonReset = (TextView) rootView.findViewById(R.id.tvResetPassword);
    }

    @Override
    protected void initAction() {
        mButtonReset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvResetPassword:
                getPresenter().onResetPasswordButtonClicked(mInputUserId.getText().toString().trim());
                break;
        }
    }

    @Override
    public void onValidateFailure(String message) {
        showToast(message);
    }

    @Override
    public void onResetSuccess(String message) {
        showToast(message);
        getActivityCallback().displayScreen(IMainView.FRAGMENT_CHANGE_PASSWORD_CODE, true, false);

    }

    @Override
    public void onResetFailure(String message) {
        showToast(message);
    }
}
