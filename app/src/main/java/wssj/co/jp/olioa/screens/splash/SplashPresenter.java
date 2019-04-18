package wssj.co.jp.olioa.screens.splash;

import android.os.Bundle;
import android.text.TextUtils;

import wssj.co.jp.olioa.model.auth.AuthModel;
import wssj.co.jp.olioa.model.baseapi.APIService;
import wssj.co.jp.olioa.model.chat.ChatModel;
import wssj.co.jp.olioa.model.firebase.FirebaseModel;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.model.stamp.StampModel;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.FragmentPresenter;
import wssj.co.jp.olioa.screens.changepassword.ChangeUserInfoFragment;

/**
 * Created by Nguyen Huu Ta on 5/6/2017.
 */

public class SplashPresenter extends FragmentPresenter<ISplashView> {

    protected SplashPresenter(ISplashView view) {
        super(view);
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new AuthModel(view.getViewContext()));
        registerModel(new FirebaseModel(view.getViewContext()));
        registerModel(new StampModel(view.getViewContext()));
        registerModel(new ChatModel(view.getViewContext()));
    }

    private SharedPreferencesModel getShareModel() {
        return getModel(SharedPreferencesModel.class);
    }

    public void onCreate(int fragmentId) {
        final String token = getShareModel().getToken();
        if (TextUtils.isEmpty(token)) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(ChangeUserInfoFragment.ARG_FROM_MENU, false);
            getView().displayScreen(IMainView.FRAGMENT_CHANGE_PASSWORD, bundle);
        } else {
            APIService.getInstance().addAuthorizationHeader(token);
            getView().displayScreen(fragmentId, null);
        }
    }

}
