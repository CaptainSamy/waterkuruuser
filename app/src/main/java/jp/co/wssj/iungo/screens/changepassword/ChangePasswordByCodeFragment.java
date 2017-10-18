package jp.co.wssj.iungo.screens.changepassword;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.auth.InfoUserResponse;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;

/**
 * Created by Nguyen Huu Ta on 11/5/2017.
 */

public class ChangePasswordByCodeFragment extends BaseFragment<IChangePasswordView, ChangePasswordPresenter> implements IChangePasswordView {

    private static final String TAG = "ChangePasswordByCodeFragment";

    private EditText mInputCode, mInputNewPassword, mInputConfirmPassword;

    private TextView mButtonChangePassword;

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_CHANGE_PASSWORD_CODE;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_confirm_reset_password;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_changer_password);
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
    public boolean isDisplayExtraNavigationButton() {
        return false;
    }

    @Override
    public boolean isEnableDrawableLayout() {
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
        mInputCode = (EditText) rootView.findViewById(R.id.inputCode);
        mInputNewPassword = (EditText) rootView.findViewById(R.id.inputNewPassword);
        mInputConfirmPassword = (EditText) rootView.findViewById(R.id.inputConfirmPassword);
        mButtonChangePassword = (TextView) rootView.findViewById(R.id.buttonChangePassword);
    }

    @Override
    protected void initAction() {
        mButtonChangePassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String code = mInputCode.getText().toString().trim();
                String newPassword = mInputNewPassword.getText().toString().trim();
                String confirmPassword = mInputConfirmPassword.getText().toString().trim();
                getPresenter().onChangePasswordByCodeClicked(code, newPassword, confirmPassword);
            }
        });
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
                getActivityCallback().displayScreen(IMainView.FRAGMENT_TIMELINE, true, false);
            }
        }, 1000);
    }

    /*=================================================================*/
    @Override
    public void onChangePasswordFailure(String message) {
        showToast(message);
    }

    @Override
    public void onGetInfoUserFailure(String message) {

    }

    @Override
    public void onGetInfoUserSuccess(InfoUserResponse.InfoUser infoUser) {

    }

    @Override
    public void showPhotoDialog(Drawable drawable, int requestCode) {

    }

    @Override
    public void showDialogChoose(int requestCodePicker, int requestCodeCamera) {

    }

    @Override
    public void openCamera(int requestCode) {

    }

    @Override
    public void requestCameraAndWriteStoragePermission(int requestCode) {

    }

    @Override
    public void openChooseImageScreen(int requestCode) {

    }

    @Override
    public void requestStoragePermission(int requestCode) {

    }

    @Override
    public void onOnUpdateInfoUserSuccess() {

    }

    @Override
    public void onOnUpdateInfoUserFailure(String message) {

    }
    /*================================================================*/
}
