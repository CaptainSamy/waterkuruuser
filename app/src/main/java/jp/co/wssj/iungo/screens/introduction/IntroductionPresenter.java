package jp.co.wssj.iungo.screens.introduction;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.auth.AuthModel;
import jp.co.wssj.iungo.model.auth.RegisterData;
import jp.co.wssj.iungo.model.firebase.FirebaseModel;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;
import jp.co.wssj.iungo.utils.Constants;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

class IntroductionPresenter extends FragmentPresenter<IIntroductionView> {

    IntroductionPresenter(IIntroductionView view) {
        super(view);
        registerModel(new AuthModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new FirebaseModel(view.getViewContext()));
    }

    void onLoginButtonClicked() {
        getView().displayLoginScreen();
    }

    void onRegisterButtonClicked() {
        getView().displayRegisterScreen();
    }

    void onLoginSocialNetwork(final int typeLogin, String token) {
        getView().showProgress();
        getModel(AuthModel.class).registerAccount(Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.Register.MIN_AGE, 1, typeLogin, token, new AuthModel.IRegisterCallback() {

            @Override
            public void onRegisterSuccess(RegisterData data, String message) {
                getView().hideProgress();
                getModel(SharedPreferencesModel.class).putToken(data.getToken());
                getModel(SharedPreferencesModel.class).putExpireDate(data.getExpireDate());
                getModel(SharedPreferencesModel.class).putUserName(data.getUserName());
                getModel(SharedPreferencesModel.class).putEmail(data.getEmail());
                getModel(SharedPreferencesModel.class).putTypeLogin(typeLogin);
                getModel(SharedPreferencesModel.class).putStatusLogin(true);
                getModel(FirebaseModel.class).uploadDeviceToken(data.getToken(), null);
                getView().displayHomeScreen();
            }

            @Override
            public void onRegisterFailure(ErrorMessage errorMessage) {
                getView().hideProgress();
            }
        });
    }

    public void savePhotoUrl(String photoUrl) {
        getModel(SharedPreferencesModel.class).putPhotoUrl(photoUrl);
    }
}
