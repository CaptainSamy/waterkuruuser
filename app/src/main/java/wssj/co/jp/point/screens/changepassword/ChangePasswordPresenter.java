package wssj.co.jp.point.screens.changepassword;

import wssj.co.jp.point.model.auth.AuthModel;
import wssj.co.jp.point.screens.base.FragmentPresenter;

/**
 * Created by HieuPT on 5/15/2017.
 */

class ChangePasswordPresenter extends FragmentPresenter<IChangePasswordView> {

    ChangePasswordPresenter(IChangePasswordView view) {
        super(view);
        registerModel(new AuthModel(view.getViewContext()));
    }

    private AuthModel getAuth() {
        return getModel(AuthModel.class);
    }

    void onButtonChangePasswordClicked(String code, String password, String confirmPassword) {
        getAuth().changePassword(code, password, confirmPassword, new AuthModel.IOnChangePasswordCallback() {

            @Override
            public void onValidateFailure(String message) {
                getView().onValidateFailure(message);
            }

            @Override
            public void onChangePasswordSuccess(String message) {
                getView().onChangePasswordSuccess(message);
            }
        });
    }

}
