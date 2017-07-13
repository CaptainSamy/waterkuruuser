package wssj.co.jp.point.model.firebase;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Map;

import wssj.co.jp.point.R;
import wssj.co.jp.point.model.BaseModel;
import wssj.co.jp.point.model.ErrorMessage;
import wssj.co.jp.point.utils.Constants;
import wssj.co.jp.point.utils.VolleySequence;

/**
 * Created by tuanle on 6/1/17.
 */

public class FirebaseModel extends BaseModel {

    public interface IUploadDeviceTokenCallback {

        void onSuccess();

        void onFailure(ErrorMessage errorMessage);
    }

    public interface IParseNotificationCallback {

        void onSuccess(NotificationMessage notificationMessage);

        void onFailure(ErrorMessage errorMessage);
    }

    public FirebaseModel(Context context) {
        super(context);
    }

    public void uploadDeviceToken(String loginToken, final IUploadDeviceTokenCallback callback) {
        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        String androidId = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Request request = APICreator.getUploadDeviceTokenRequest(loginToken, deviceToken, androidId, new Response.Listener<UploadTokenReponse>() {

            @Override
            public void onResponse(UploadTokenReponse response) {
                if (callback != null) {
                    if (response.isSuccess()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure(new ErrorMessage(response.getMessage()));
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (callback != null) {
                    callback.onFailure(new ErrorMessage(getStringResource(R.string.network_error)));
                }
            }
        });
        VolleySequence.getInstance().addRequest(request);
    }

    public void parseNotificationData(Map<String, String> data, IParseNotificationCallback callback) {
        String title = data.get("title");
        String message = data.get("body");
        String pushIdText = data.get("push_id");
        String actionPush = Constants.PushNotification.TYPE_NOTIFICATION;
        int stampId = 0;
        String[] splitAction = data.get("click_action").split(Constants.SPLIT);
        if (splitAction != null) {
            actionPush = splitAction[0];
            if (splitAction.length == 2) {
                stampId = Integer.parseInt(splitAction[1]);
            }
        }

        long pushId = 1;
        if (!TextUtils.isEmpty(pushIdText)) {
            pushId = Long.parseLong(pushIdText);
        }
        if (TextUtils.isEmpty(message)) {
            callback.onFailure(new ErrorMessage(getStringResource(R.string.msg_parse_message_failure)));
        } else {
            callback.onSuccess(new NotificationMessage(pushId, title, message, actionPush, stampId));
        }
    }

}
