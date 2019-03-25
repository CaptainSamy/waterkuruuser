package wssj.co.jp.olioa.screens.registeraccount;

import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.model.auth.AuthModel;
import wssj.co.jp.olioa.model.baseapi.APICallback;
import wssj.co.jp.olioa.model.baseapi.APIService;
import wssj.co.jp.olioa.model.entities.AccessToken;
import wssj.co.jp.olioa.model.firebase.FirebaseModel;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.screens.base.FragmentPresenter;
import wssj.co.jp.olioa.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

class RegisterAccountPresenter extends FragmentPresenter<IRegisterAccountView> {

    RegisterAccountPresenter(IRegisterAccountView view) {
        super(view);
        registerModel(new AuthModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new FirebaseModel(view.getViewContext()));
    }

    private AuthModel getAuthModel() {
        return getModel(AuthModel.class);
    }

    void onValidateInfoRegister(String userName, String userId, String password, String confirmPassword, String email, int age, int sex) {
        getAuthModel().validateRegisterAccount(userName, userId, password, confirmPassword, email, age, sex, new AuthModel.IValidateRegisterCallback() {

            @Override
            public void validateSuccess(String userName, String password, String name, String email, int age, int sex) {
                String md5Password = Utils.toMD5(password);
                onRegisterAccount(userName, md5Password, name, email, age, sex);

            }

            @Override
            public void validateFailure(ErrorMessage errorMessage, int code) {
                getView().onValidateFailure(errorMessage, code);
            }
        });
    }

    private void onRegisterAccount(final String userName, final String password, String name, final String email, int age, int sex) {
        getView().showProgress();
        getAuthModel().registerAccount(userName, password, name, email, sex, new APICallback<AccessToken>() {

            @Override
            public void onSuccess(AccessToken accessToken) {
                getView().hideProgress();
                String token = "Bearer " + accessToken.getAccessToken();
                APIService.getInstance().addAuthorizationHeader(token);
                getModel(SharedPreferencesModel.class).putToken(token);
                getModel(SharedPreferencesModel.class).putExpireDate(accessToken.getExpired());
                getModel(SharedPreferencesModel.class).putUserName(userName);
                getModel(SharedPreferencesModel.class).putPassword(password);
                getModel(SharedPreferencesModel.class).putEmail(email);
                getModel(FirebaseModel.class).uploadDeviceToken(token, null);
                getView().onRegisterSuccess();
            }

            @Override
            public void onFailure(String errorMessage) {
                getView().hideProgress();
                getView().showToast(errorMessage);
            }
        });
    }

}
