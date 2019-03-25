package wssj.co.jp.olioa.screens.login;

import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.model.auth.AuthModel;
import wssj.co.jp.olioa.model.baseapi.APICallback;
import wssj.co.jp.olioa.model.baseapi.APIService;
import wssj.co.jp.olioa.model.checkin.CheckInModel;
import wssj.co.jp.olioa.model.entities.AccessToken;
import wssj.co.jp.olioa.model.firebase.FirebaseModel;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.screens.base.FragmentPresenter;
import wssj.co.jp.olioa.utils.Utils;

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
        getAuthModel().loginUser(userId, password, new APICallback<AccessToken>() {

            @Override
            public void onSuccess(AccessToken accessToken) {
                getView().hideProgress();
                String token = "Bearer " + accessToken.getAccessToken();
                APIService.getInstance().addAuthorizationHeader(token);
                getModel(SharedPreferencesModel.class).putToken(token);
                getModel(SharedPreferencesModel.class).putExpireDate(accessToken.getExpired());
                getModel(FirebaseModel.class).uploadDeviceToken(token, null);
                getView().displayHomeScreen();
            }

            @Override
            public void onFailure(String errorMessage) {
                getView().hideProgress();
                getView().showLoginFailureMessage(errorMessage);
            }
        });

    }

    @Override
    public void validateFailure(ErrorMessage errorMessage) {
        getView().showListMessageValidate(errorMessage);
    }
}
