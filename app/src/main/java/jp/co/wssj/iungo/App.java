package jp.co.wssj.iungo;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.google.firebase.iid.FirebaseInstanceId;
import com.twitter.sdk.android.core.Twitter;

import jp.co.wssj.iungo.utils.Logger;

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
        Twitter.initialize(this);
        FacebookSdk.sdkInitialize(this);
    }

    private void logToken() {
        //TODO: remove this log when release
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Logger.d(TAG, "#logToken " + refreshedToken);
    }

    private void setInstance(App app) {
        sInstance = app;
    }
}
