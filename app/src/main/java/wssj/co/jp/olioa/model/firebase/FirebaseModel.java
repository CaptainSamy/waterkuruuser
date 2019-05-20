package wssj.co.jp.olioa.model.firebase;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Map;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.BaseModel;
import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.utils.VolleySequence;

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
        String actionPush = data.get("type");
        int storeId;
        try {
            storeId = Integer.parseInt(data.get("store_id"));
        }catch (Exception e){
            storeId = -1;
        }

        int groupId;
        try {
            groupId = Integer.parseInt(data.get("group_id"));
        }catch (Exception e){
            groupId = -1;
        }

        long pushId = 1;
        if (!TextUtils.isEmpty(pushIdText)) {
            pushId = Long.parseLong(pushIdText);
        }
        if (TextUtils.isEmpty(title)) {
            callback.onFailure(new ErrorMessage(getStringResource(R.string.msg_parse_message_failure)));
        } else {
            callback.onSuccess(new NotificationMessage(pushId, title, message, actionPush, storeId, groupId));
        }
    }

}
