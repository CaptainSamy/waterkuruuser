package wssj.co.jp.point.screens.splash;

import android.os.Handler;
import android.text.TextUtils;

import wssj.co.jp.point.model.preference.SharedPreferencesModel;
import wssj.co.jp.point.screens.IMainView;
import wssj.co.jp.point.screens.base.FragmentPresenter;
import wssj.co.jp.point.utils.Constants;

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
