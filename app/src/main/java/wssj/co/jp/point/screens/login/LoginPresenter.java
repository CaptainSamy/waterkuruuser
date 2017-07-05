package wssj.co.jp.point.screens.login;

import wssj.co.jp.point.model.ErrorMessage;
import wssj.co.jp.point.model.auth.AuthModel;
import wssj.co.jp.point.model.auth.LoginResponse;
import wssj.co.jp.point.model.checkin.CheckInModel;
import wssj.co.jp.point.model.firebase.FirebaseModel;
import wssj.co.jp.point.model.preference.SharedPreferencesModel;
import wssj.co.jp.point.screens.base.FragmentPresenter;

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
    public void validateSuccess(String userId, String password) {
        getView().showProgress();
        getAuthModel().loginAWS(userId, password, new AuthModel.ILoginCallback() {

            @Override
            public void onLoginSuccess(LoginResponse.LoginData data) {
                getModel(SharedPreferencesModel.class).putToken(data.getToken());
                getModel(SharedPreferencesModel.class).putExpireDate(data.getExpireDate());
                getModel(SharedPreferencesModel.class).putUserName(data.getUserName());
                getModel(SharedPreferencesModel.class).putEmail(data.getEmail());
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
