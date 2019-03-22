package wssj.co.jp.olioa.screens.resetpassword;

import wssj.co.jp.olioa.model.auth.AuthModel;
import wssj.co.jp.olioa.screens.base.FragmentPresenter;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

class ResetPasswordPresenter extends FragmentPresenter<IResetPasswordView> {

    ResetPasswordPresenter(IResetPasswordView view) {
        super(view);
        registerModel(new AuthModel(view.getViewContext()));

    }

    private AuthModel getAuthModel() {
        return getModel(AuthModel.class);
    }

    void onResetPasswordButtonClicked(String userId) {
        getView().showProgress();
        getAuthModel().validateResetPassword(userId, new AuthModel.IOnResetPasswordCallback() {

            @Override
            public void onValidateFailure(String message) {
                getView().onValidateFailure(message);
                getView().hideProgress();
            }

            @Override
            public void onResetSuccess(String message) {
                getView().hideProgress();
                getView().onResetSuccess(message);
            }

            @Override
            public void onResetFailure(String message) {
                getView().hideProgress();
                getView().onResetFailure(message);
            }
        });
    }


}
