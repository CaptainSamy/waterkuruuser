package wssj.co.jp.obis.model.firebase;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import wssj.co.jp.obis.model.volleyrequest.GsonJsonRequest;
import wssj.co.jp.obis.model.volleyrequest.GsonRequest;
import wssj.co.jp.obis.utils.Constants;
import wssj.co.jp.obis.utils.Logger;

/**
 * Created by tuanle on 6/1/17.
 */

public class APICreator {

    private static final String TAG = "Firebase.ApiCreator";

    private static final String API_UPLOAD_DEVICE_TOKEN = Constants.BASE_URL + "api/add-firebase-token";

    static GsonRequest<UploadTokenReponse> getUploadDeviceTokenRequest(final String token,
                                                                       final String deviceToken, final String deviceId, final Response.Listener<UploadTokenReponse> listener,
                                                                       final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        return new GsonJsonRequest<UploadTokenReponse>(Request.Method.POST,
                API_UPLOAD_DEVICE_TOKEN,
                UploadTokenReponse.class,
                headers,
                new Response.Listener<UploadTokenReponse>() {

                    @Override
                    public void onResponse(UploadTokenReponse response) {
                        Logger.d(TAG, "#uploadDeviceToken -> onResponse " + response.isSuccess());
                        if (listener != null) {
                            listener.onResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#uploadDeviceToken -> onErrorResponse");
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> params = new HashMap<>();
                params.put("token", deviceToken);
                params.put("uuid", deviceId);
                params.put("device_info", "Android");
                return params;
            }
        };
    }
}
