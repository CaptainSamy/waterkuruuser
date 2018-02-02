package jp.co.wssj.iungo.model.chatrealtime;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import jp.co.wssj.iungo.model.ResponseData;
import jp.co.wssj.iungo.model.volleyrequest.GsonJsonRequest;
import jp.co.wssj.iungo.model.volleyrequest.GsonRequest;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;

/**
 * Created by thang on 1/26/2018.
 */

public class APICreator {
        public  static final String API_USER_INF_URL = Constants.BASE_URL+"/api/client/users/get-info-user";
    private static final String TAG = "ChatRealtime API";

    static GsonRequest getUserProfile(final String token, final Response.Listener<UserProfileResponse> listener, final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        return new GsonJsonRequest<UserProfileResponse>(Request.Method.GET,
                API_USER_INF_URL,
                UserProfileResponse.class,
                headers,
                Constants.TIME_OUT_CUSTOM,
                new Response.Listener<UserProfileResponse>() {
                    @Override
                    public void onResponse(UserProfileResponse response) {
                       Logger.d(TAG, "#UserProfileResponse -> onResponse ");
                        if (listener != null) {
                            listener.onResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#UserProfileResponse -> onErrorResponse");
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                }) {
            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> params = new HashMap<>();
                return params;
            }
        };
    }
}
