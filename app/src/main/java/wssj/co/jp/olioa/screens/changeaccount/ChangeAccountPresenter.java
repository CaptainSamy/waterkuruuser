package wssj.co.jp.olioa.screens.changeaccount;

import android.text.TextUtils;

import wssj.co.jp.olioa.model.auth.AuthModel;
import wssj.co.jp.olioa.model.auth.RegisterData;
import wssj.co.jp.olioa.model.baseapi.APICallback;
import wssj.co.jp.olioa.model.baseapi.APIService;
import wssj.co.jp.olioa.model.entities.AccessToken;
import wssj.co.jp.olioa.model.entities.UserResponse;
import wssj.co.jp.olioa.model.firebase.FirebaseModel;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.model.util.UtilsModel;
import wssj.co.jp.olioa.screens.base.FragmentPresenter;

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
