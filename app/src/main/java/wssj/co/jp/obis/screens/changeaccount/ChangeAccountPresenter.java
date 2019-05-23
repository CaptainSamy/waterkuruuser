package wssj.co.jp.obis.screens.changeaccount;

import wssj.co.jp.obis.model.auth.AuthModel;
import wssj.co.jp.obis.model.baseapi.APICallback;
import wssj.co.jp.obis.model.entities.AccessToken;
import wssj.co.jp.obis.screens.base.FragmentPresenter;

/**
 * Created by HieuPT on 5/15/2017.
 */

class ChangeAccountPresenter extends FragmentPresenter<IChangeAccountView> {

    ChangeAccountPresenter(IChangeAccountView view) {
        super(view);
        registerModel(new AuthModel(view.getViewContext()));
    }

    private AuthModel getAuth() {
        return getModel(AuthModel.class);
    }


    void changeAccount(String account, String password, String confirmPassword) {
        final String message = getAuth().validateChangeAccount(account, password, confirmPassword);
        if (message.isEmpty()) {
            getView().showProgress();
            getAuth().changeAccount(account, password, new APICallback<AccessToken>() {
                @Override
                public void onSuccess(AccessToken accessToken) {
                    getView().hideProgress();
                    getView().backToPreviousScreen();
                }

                @Override
                public void onFailure(String errorMessage) {
                    getView().hideProgress();
                    getView().showDialog(errorMessage);
                }
            });
        } else {
            getView().showDialog(message);
        }
    }

}
