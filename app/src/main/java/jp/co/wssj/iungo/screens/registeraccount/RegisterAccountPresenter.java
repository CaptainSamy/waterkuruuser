package jp.co.wssj.iungo.screens.registeraccount;

import android.os.Handler;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.auth.AuthModel;
import jp.co.wssj.iungo.model.auth.RegisterData;
import jp.co.wssj.iungo.model.firebase.FirebaseModel;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

class RegisterAccountPresenter extends FragmentPresenter<IRegisterAccountView> implements AuthModel.IRegisterCallback {

    RegisterAccountPresenter(IRegisterAccountView view) {
        super(view);
        registerModel(new AuthModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new FirebaseModel(view.getViewContext()));
    }

    private AuthModel getAuthModel() {
        return getModel(AuthModel.class);
    }

    void onRegisterButtonClicked(String userName, String userId, String password, String confirmPassword, String email) {
        getAuthModel().validateRegisterAccount(userName, userId, password, confirmPassword, email, this);
    }

    @Override
    public void validateSuccess(String userName, String password, String name, String email) {
        getView().showProgress();
        getAuthModel().registerAWS(userName, password, name, email, this);
    }

    @Override
    public void validateFailure(ErrorMessage errorMessage, int code) {
        getView().onValidateFailure(errorMessage, code);
    }

    @Override
    public void onRegisterSuccess(final RegisterData data) {
        getModel(SharedPreferencesModel.class).putToken(data.getToken());
        getModel(SharedPreferencesModel.class).putExpireDate(data.getExpireDate());
        getModel(SharedPreferencesModel.class).putUserName(data.getUserName());
        getModel(SharedPreferencesModel.class).putEmail(data.getEmail());
        getModel(FirebaseModel.class).uploadDeviceToken(data.getToken(), null);
        getView().hideProgress();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                getView().onRegisterSuccess(data);
            }
        }, 1000);

    }

    @Override
    public void onRegisterFailure(ErrorMessage errorMessage) {
        getView().hideProgress();
        getView().onRegisterFailure(errorMessage);
    }
}
