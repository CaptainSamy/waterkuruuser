package wssj.co.jp.point.screens.changepassword;

import wssj.co.jp.point.model.auth.AuthModel;
import wssj.co.jp.point.model.auth.RegisterData;
import wssj.co.jp.point.model.firebase.FirebaseModel;
import wssj.co.jp.point.model.preference.SharedPreferencesModel;
import wssj.co.jp.point.screens.base.FragmentPresenter;

/**
 * Created by HieuPT on 5/15/2017.
 */

class ChangePasswordPresenter extends FragmentPresenter<IChangePasswordView> {

    ChangePasswordPresenter(IChangePasswordView view) {
        super(view);
        registerModel(new AuthModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new FirebaseModel(view.getViewContext()));
    }

    private AuthModel getAuth() {
        return getModel(AuthModel.class);
    }

    void onChangePasswordByCodeClicked(String code, String password, String confirmPassword) {
        getView().showProgress();
        getAuth().changePasswordByCode(code, password, confirmPassword, new AuthModel.IOnChangePasswordCallback() {

            @Override
            public void onValidateFailure(String message) {
                getView().hideProgress();
                getView().onValidateFailure(message);
            }

            @Override
            public void onChangePasswordSuccess(RegisterData data, String message) {
                getView().hideProgress();
                if (data != null) {
                    getModel(SharedPreferencesModel.class).putToken(data.getToken());
                    getModel(SharedPreferencesModel.class).putExpireDate(data.getExpireDate());
                    getModel(FirebaseModel.class).uploadDeviceToken(data.getToken(), null);
                }
                getView().onChangePasswordSuccess(message);
            }

            @Override
            public void onChangePasswordFailure(String message) {
                getView().hideProgress();
                getView().onChangePasswordFailure(message);
            }
        });
    }

    void onChangePasswordClicked(String currentPassword, String password, String confirmPassword) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getView().showProgress();
        getAuth().changePassword(token, currentPassword, password, confirmPassword, new AuthModel.IOnChangePasswordCallback() {

            @Override
            public void onValidateFailure(String message) {
                getView().hideProgress();
                getView().onValidateFailure(message);
            }

            @Override
            public void onChangePasswordSuccess(RegisterData data, String message) {
                getView().hideProgress();
                if (data != null) {
                    getModel(SharedPreferencesModel.class).putToken(data.getToken());
                    getModel(SharedPreferencesModel.class).putExpireDate(data.getExpireDate());
                    getModel(FirebaseModel.class).uploadDeviceToken(data.getToken(), null);
                }
                getView().onChangePasswordSuccess(message);
            }

            @Override
            public void onChangePasswordFailure(String message) {
                getView().hideProgress();
                getView().onChangePasswordFailure(message);
            }
        });
    }

}
