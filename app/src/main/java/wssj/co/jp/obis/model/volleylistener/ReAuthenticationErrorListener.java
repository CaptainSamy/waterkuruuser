package wssj.co.jp.obis.model.volleylistener;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

import wssj.co.jp.obis.App;
import wssj.co.jp.obis.model.ErrorMessage;
import wssj.co.jp.obis.model.auth.AuthModel;
import wssj.co.jp.obis.model.auth.LoginResponse;
import wssj.co.jp.obis.model.firebase.FirebaseModel;
import wssj.co.jp.obis.model.preference.SharedPreferencesModel;
import wssj.co.jp.obis.screens.MainActivity;
import wssj.co.jp.obis.utils.Logger;
import wssj.co.jp.obis.utils.Utils;
import wssj.co.jp.obis.utils.VolleySequence;

/**
 * Created by Nguyen Huu Ta on 24/7/2017.
 */

public class ReAuthenticationErrorListener implements Response.ErrorListener {

    private static final String TAG = "ReAuthenticationErrorListener";

    private static final String USER_ID_OR_PASSWORD_IS_NOT_CORRECT_MESSAGE = "ログインIDまたはパスワードに誤りがあります。";

    private final Response.ErrorListener mExternalErrorListener;

    private final Request<?> mReplayRequest;

    private VolleyError mOriginError;

    public ReAuthenticationErrorListener(@NonNull Request replayRequest, Response.ErrorListener errorListener) {
        Utils.requireNonNull(replayRequest, "Request must not be null");
        mReplayRequest = replayRequest;
        mExternalErrorListener = errorListener;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mOriginError = error;
//        if (error != null) {
//            NetworkResponse networkResponse = error.networkResponse;
//            if (networkResponse != null) {
//                int statusCode = networkResponse.statusCode;
//                if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
//                    reAuthenticate();
//                    return;
//                }
//            }
//        } else {
//            reAuthenticate();
//            return;
//        }
        if (mExternalErrorListener != null) {
            mExternalErrorListener.onErrorResponse(mOriginError);
        }
    }

    private void reAuthenticate() {
        Logger.d(TAG, "#reAuthenticate");
        AuthModel authModel = new AuthModel(App.getInstance());
        final SharedPreferencesModel sharedPreferencesModel = new SharedPreferencesModel(App.getInstance());

        final FirebaseModel firebaseModel = new FirebaseModel(App.getInstance());
        final String userId = sharedPreferencesModel.getUserId();
        String password = sharedPreferencesModel.getPassword();
        authModel.loginAWS(userId, password, new AuthModel.ILoginCallback() {

            @Override
            public void onLoginSuccess(LoginResponse.LoginData data) {
                Logger.d(TAG, "#reAuthenticate -> onLoginSuccess");
                sharedPreferencesModel.putToken(data.getToken());
                sharedPreferencesModel.putExpireDate(data.getExpireDate());
                sharedPreferencesModel.putUserName(data.getUserName());
                sharedPreferencesModel.putEmail(data.getEmail());
                firebaseModel.uploadDeviceToken(data.getToken(), null);
                Utils.overrideRequestHeader(mReplayRequest, "Authorization", data.getToken());
                List<Request> requests = VolleySequence.getInstance().getRequests();
                for (Request<?> request : requests) {
                    if (request != null) {
                        Utils.overrideRequestHeader(request, "Authorization", data.getToken());
                    }
                }
                VolleySequence.getInstance().addRequestToFrontQueue(mReplayRequest);
            }

            @Override
            public void onLoginFailure(ErrorMessage errorMessage) {
                Logger.d(TAG, "#reAuthenticate -> onLoginFailure");
                if (USER_ID_OR_PASSWORD_IS_NOT_CORRECT_MESSAGE.equals(errorMessage.getMessage())) {
                    Intent intent = new Intent(MainActivity.ACTION_LOGOUT);
                    LocalBroadcastManager.getInstance(App.getInstance()).sendBroadcast(intent);
                } else if (mExternalErrorListener != null) {
                    mExternalErrorListener.onErrorResponse(mOriginError);
                }
            }
        });
    }
}
