package jp.co.wssj.iungo.screens.login;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.auth.AuthModel;
import jp.co.wssj.iungo.model.auth.LoginResponse;
import jp.co.wssj.iungo.model.checkin.CheckInModel;
import jp.co.wssj.iungo.model.firebase.FirebaseModel;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;
import jp.co.wssj.iungo.utils.Logger;
import jp.co.wssj.iungo.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

class LoginPresenter extends FragmentPresenter<ILoginView> implements AuthModel.IValidateLoginCallback {

    LoginPresenter(ILoginView view) {
        super(view);
        registerModel(new AuthModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new FirebaseModel(view.getViewContext()));
        registerModel(new CheckInModel(view.getViewContext()));
    }

    private AuthModel getAuthModel() {
        return getModel(AuthModel.class);
    }

    void onLoginButtonClicked(String userId, String password) {
        getAuthModel().validateLogin(userId, password, this);
    }

    void onResetPasswordClicked() {
        getView().displayResetPasswordScreen();
    }

    @Override
    public void validateSuccess(final String userId, final String password) {
        getView().showProgress();
        final String encryptPassword = Utils.toMD5(password);
        getAuthModel().loginAWS(userId, encryptPassword, new AuthModel.ILoginCallback() {
            @Override
            public void onLoginSuccess(LoginResponse.LoginData data) {
                getModel(SharedPreferencesModel.class).putUserId(userId);
                Logger.d("userId",userId);
                getModel(SharedPreferencesModel.class).putPassword(encryptPassword);
                getModel(SharedPreferencesModel.class).putToken(data.getToken());
                getModel(SharedPreferencesModel.class).putExpireDate(data.getExpireDate());
                getModel(SharedPreferencesModel.class).putUserName(data.getUserName());
                getModel(SharedPreferencesModel.class).putEmail(data.getEmail());
                getModel(SharedPreferencesModel.class).putStatusLogin(true);
                getModel(SharedPreferencesModel.class).putImageUser(data.getImageUser());
                getModel(FirebaseModel.class).uploadDeviceToken(data.getToken(), null);
                if (data.isRequireResetPassword()) {
                    getView().displayChangePasswordScreen();
                } else {
                    getView().displayHomeScreen();
                }
            }

            @Override
            public void onLoginFailure(ErrorMessage errorMessage) {
                getView().hideProgress();
                getView().showLoginFailureMessage(errorMessage.getMessage());
            }
        });

    }

    @Override
    public void validateFailure(ErrorMessage errorMessage) {
        getView().showListMessageValidate(errorMessage);
    }
}
