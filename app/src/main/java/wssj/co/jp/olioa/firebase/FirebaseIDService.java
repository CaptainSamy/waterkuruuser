package wssj.co.jp.olioa.firebase;

import android.text.TextUtils;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.model.firebase.FirebaseModel;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.utils.Logger;

/**
 * Created by tuanle on 5/30/17.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {

    private static final String TAG = FirebaseIDService.class.getSimpleName();

    private SharedPreferencesModel mSharedPreferencesModel;
    private FirebaseModel mFirebaseModel;

    @Override
    public void onCreate() {
        super.onCreate();
        mSharedPreferencesModel = new SharedPreferencesModel(this);
        mFirebaseModel = new FirebaseModel(this);
    }

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Logger.d(TAG, "#onTokenRefresh " + refreshedToken);

        String loginToken = mSharedPreferencesModel.getToken();
        if (!TextUtils.isEmpty(loginToken)) {
            mFirebaseModel.uploadDeviceToken(loginToken, new FirebaseModel.IUploadDeviceTokenCallback() {
                @Override
                public void onSuccess() {
                    Logger.d(TAG, "#uploadDeviceToken success");
                }

                @Override
                public void onFailure(ErrorMessage errorMessage) {
                    Logger.d(TAG, "#UploadDeviceToken failure " + errorMessage);
                }
            });
        }

    }
}
