package jp.co.wssj.iungo.screens.registeraccount;

import android.os.Handler;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.auth.AuthModel;
import jp.co.wssj.iungo.model.auth.RegisterData;
import jp.co.wssj.iungo.model.firebase.FirebaseModel;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Utils;

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

    private void onRegisterAccount(String userName, String password, String name, String email, int age, int sex) {
        getView().showProgress();
        getAuthModel().registerAccount(userName, password, name, email, age, sex, Constants.Introduction.TYPE_DEFAULT, Constants.EMPTY_STRING, new AuthModel.IRegisterCallback() {

            @Override
            public void onRegisterSuccess(final RegisterData data, final String message) {

                getView().hideProgress();
                getModel(SharedPreferencesModel.class).putToken(data.getToken());
                getModel(SharedPreferencesModel.class).putExpireDate(data.getExpireDate());
                getModel(SharedPreferencesModel.class).putUserName(data.getUserName());
                getModel(SharedPreferencesModel.class).putEmail(data.getEmail());
                getModel(SharedPreferencesModel.class).putStatusLogin(true);
                getModel(FirebaseModel.class).uploadDeviceToken(data.getToken(), null);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        getView().onRegisterSuccess(data, message);
                    }
                }, 1000);
            }

            @Override
            public void onRegisterFailure(ErrorMessage errorMessage) {
                getView().hideProgress();
                getView().onRegisterFailure(errorMessage);
            }
        });
    }

}
