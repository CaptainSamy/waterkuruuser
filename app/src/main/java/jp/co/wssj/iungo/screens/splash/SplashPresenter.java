package jp.co.wssj.iungo.screens.splash;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.auth.AuthModel;
import jp.co.wssj.iungo.model.auth.CheckVersionAppResponse;
import jp.co.wssj.iungo.model.auth.InitUserResponse;
import jp.co.wssj.iungo.model.auth.LoginResponse;
import jp.co.wssj.iungo.model.chat.ChatModel;
import jp.co.wssj.iungo.model.firebase.FirebaseModel;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.model.stamp.StampModel;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Utils;

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
    final Handler handler = new Handler();

    public void getStatusUser(final int versionCode,final String token){
        getModel(AuthModel.class).checkInitUser( token,new AuthModel.IOnGetInitUserCallback() {

            @Override
            public void onGetInitUserSuccess(InitUserResponse.InitUserData initUser) {
                if (initUser != null && initUser.getStatus().equals("done")){
                    Log.d("Splash","init done");

                    switchScreen(versionCode,token);
                }else {
                    Log.d("Splash","Loading");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           getStatusUser(versionCode,token);
                        }
                    }, 1000);
                }
            }

            @Override
            public void onGetInitUserFailure(String message) {
                Log.d("Splash","failure");
                switchScreen(versionCode,token);
            }
        });
    }

    public void onCreate(final int versionCode) {
        final String token = getShareModel().getToken();
        if (TextUtils.isEmpty(token)) {
            getModel(AuthModel.class).autoLogin(new AuthModel.ILoginCallback() {

                @Override
                public void onLoginSuccess(LoginResponse.LoginData data) {
                    getModel(SharedPreferencesModel.class).putName(data.getName());
                    getModel(SharedPreferencesModel.class).putPassword(Utils.toMD5(data.getPassword()));
                    getModel(SharedPreferencesModel.class).putToken(data.getToken());
                    getModel(SharedPreferencesModel.class).putExpireDate(data.getExpireDate());
                    getModel(SharedPreferencesModel.class).putUserName(data.getUserName());
                    getModel(SharedPreferencesModel.class).putEmail(data.getEmail());
                    getModel(SharedPreferencesModel.class).putStatusLogin(true);
                    getModel(SharedPreferencesModel.class).putImageUser(data.getImageUser());
                    getModel(FirebaseModel.class).uploadDeviceToken(data.getToken(), null);
                    

                    getStatusUser(versionCode,data.getToken());
                    //switchScreen(versionCode, token);
                }

                @Override
                public void onLoginFailure(ErrorMessage errorMessage) {
                    //TODO handle error
                }
            });
        } else {
            switchScreen(versionCode, token);
        }
    }

    public void switchScreen(final int versionCode, String token) {
        getModel(AuthModel.class).checkVersionApp(token, versionCode, new AuthModel.IOnCheckVersionAppCallback() {

            @Override
            public void onCheckVersionAppSuccess(CheckVersionAppResponse.CheckVersionAppData response) {
                getView().showDialog(response);
            }

            @Override
            public void onCheckVersionAppFailure() {
                getView().displayScreen(IMainView.FRAGMENT_TIMELINE);
            }
        });
    }

    //    public void onCreate(final int versionCode) {
//        long expireDate = getModel(SharedPreferencesModel.class).getExpireDate();
//        if (expireDate <= System.currentTimeMillis()) {
//            String userId = getModel(SharedPreferencesModel.class).getUserId();
//            String password = getModel(SharedPreferencesModel.class).getPassword();
//            getModel(AuthModel.class).loginAWS(userId, password, new AuthModel.ILoginCallback() {
//
//                @Override
//                public void onLoginSuccess(LoginResponse.LoginData data) {
//                    getShareModel().putToken(data.getToken());
//                    getShareModel().putExpireDate(data.getExpireDate());
//                    getModel(FirebaseModel.class).uploadDeviceToken(data.getToken(), null);
//                    switchScreen(versionCode);
//                }
//
//                @Override
//                public void onLoginFailure(ErrorMessage errorMessage) {
//                    Intent intent = new Intent(MainActivity.ACTION_LOGOUT);
//                    LocalBroadcastManager.getInstance(App.getInstance()).sendBroadcast(intent);
//                }
//            });
//        } else {
//            switchScreen(versionCode);
//        }
//
//    }
//
}
