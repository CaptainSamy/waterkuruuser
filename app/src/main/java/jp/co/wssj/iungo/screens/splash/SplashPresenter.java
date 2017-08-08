package jp.co.wssj.iungo.screens.splash;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import jp.co.wssj.iungo.App;
import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.auth.AuthModel;
import jp.co.wssj.iungo.model.auth.LoginResponse;
import jp.co.wssj.iungo.model.firebase.FirebaseModel;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.MainActivity;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;
import jp.co.wssj.iungo.utils.Constants;

/**
 * Created by Nguyen Huu Ta on 5/6/2017.
 */

public class SplashPresenter extends FragmentPresenter<ISplashView> {

    protected SplashPresenter(ISplashView view) {
        super(view);
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new AuthModel(view.getViewContext()));
        registerModel(new FirebaseModel(view.getViewContext()));
    }

    private SharedPreferencesModel getShareModel() {
        return getModel(SharedPreferencesModel.class);
    }

    public void onCreate(final int versionCode) {
        long expireDate = getModel(SharedPreferencesModel.class).getExpireDate();
        if (expireDate <= System.currentTimeMillis()) {
            String userId = getModel(SharedPreferencesModel.class).getUserId();
            String password = getModel(SharedPreferencesModel.class).getPassword();
            getModel(AuthModel.class).loginAWS(userId, password, new AuthModel.ILoginCallback() {

                @Override
                public void onLoginSuccess(LoginResponse.LoginData data) {
                    getShareModel().putToken(data.getToken());
                    getShareModel().putExpireDate(data.getExpireDate());
                    getModel(FirebaseModel.class).uploadDeviceToken(data.getToken(), null);
                    switchScreen(versionCode);
                }

                @Override
                public void onLoginFailure(ErrorMessage errorMessage) {
                    Intent intent = new Intent(MainActivity.ACTION_LOGOUT);
                    LocalBroadcastManager.getInstance(App.getInstance()).sendBroadcast(intent);
                }
            });
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    switchScreen(versionCode);
                }
            }, Constants.TIME_WAITING_SPLASH);
        }
    }

    public void switchScreen(final int versionCode) {
        final String token = getModel(SharedPreferencesModel.class).getToken();
        if (!TextUtils.isEmpty(token)) {
//            getModel(AuthModel.class).checkVersionApp(token, versionCode, new AuthModel.IOnCheckVersionAppCallback() {
//
//                @Override
//                public void onCheckVersionAppSuccess(CheckVersionAppResponse.CheckVersionAppData response) {
//                    if (response.isHasUpdate()) {
//                        getView().showDialog(response);
//                    } else {
//                        getView().displayScreen(IMainView.FRAGMENT_HOME);
//                    }
//                }
//
//                @Override
//                public void onCheckVersionAppFailure() {
//                    getView().displayScreen(IMainView.FRAGMENT_HOME);
//                }
//            });
            getView().displayScreen(IMainView.FRAGMENT_HOME);
        } else {
            getView().displayScreen(IMainView.FRAGMENT_INTRODUCTION_SCREEN);
        }

    }
}
