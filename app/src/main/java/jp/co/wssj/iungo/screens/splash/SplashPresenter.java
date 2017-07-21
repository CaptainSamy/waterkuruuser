package jp.co.wssj.iungo.screens.splash;

import android.os.Handler;
import android.text.TextUtils;

import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;
import jp.co.wssj.iungo.utils.Constants;

/**
 * Created by Nguyen Huu Ta on 5/6/2017.
 */

public class SplashPresenter extends FragmentPresenter<ISplashView> {

    protected SplashPresenter(ISplashView view) {
        super(view);
        registerModel(new SharedPreferencesModel(view.getViewContext()));
    }

    public void onCreate() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                switchScreen();
            }
        }, Constants.TIME_WAITING_SPLASH);
    }

    public void switchScreen() {
        String token = getModel(SharedPreferencesModel.class).getToken();
        if (!TextUtils.isEmpty(token)) {
            getView().displayScreen(IMainView.FRAGMENT_HOME);
        } else {
            getView().displayScreen(IMainView.FRAGMENT_INTRODUCTION_SCREEN);
        }
    }
}
