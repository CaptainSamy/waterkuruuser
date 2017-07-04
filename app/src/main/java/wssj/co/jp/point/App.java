package wssj.co.jp.point;

import android.app.Application;

import com.google.firebase.iid.FirebaseInstanceId;

import wssj.co.jp.point.utils.Logger;

/**
 * Created by HieuPT on 5/9/2017.
 */

public class App extends Application {

    private static final String TAG = "Application";

    private static App sInstance;

    public static App getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        logToken();
    }

    private void logToken(){
        //TODO: remove this log when release
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Logger.d(TAG, "#logToken " + refreshedToken);
    }

    private void setInstance(App app) {
        sInstance = app;
    }
}
