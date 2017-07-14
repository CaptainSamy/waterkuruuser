package wssj.co.jp.point.model.pushnotification;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import wssj.co.jp.point.model.ResponseData;
import wssj.co.jp.point.model.volleyrequest.GsonJsonRequest;
import wssj.co.jp.point.model.volleyrequest.GsonRequest;
import wssj.co.jp.point.utils.Constants;
import wssj.co.jp.point.utils.Logger;

/**
 * Created by tuanle on 6/7/17.
 */

public class APICreator {

    private static final String TAG = "Notification.ApiCreator";

    private static final String API_GET_LIST_NOTIFICATION = Constants.BASE_URL_AWS + "/api/client/users/get-notification-list-user";

    private static final String API_GET_LIST_PUSH_UN_READ = Constants.BASE_URL_AWS + "/api/client/users/get-notification-list-user-unread";

    private static final String API_SET_LIST_NOTIFICATION = Constants.BASE_URL_AWS + "/api/client/users/update-view-status-notification";

    private static final String API_UPDATE_ACTION_NOTIFICATION = Constants.BASE_URL_AWS + "/api/client/users/update-push-notification";

    static GsonRequest<ListNotificationResponse> getUploadDeviceTokenRequest(final String token, int page, int limit,
                                                                             final Response.Listener<ListNotificationResponse> listener,
                                                                             final Response.ErrorListener errorListener) {
        String url = API_GET_LIST_NOTIFICATION + "?page=" + page + "&limit=" + limit;
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        return new GsonRequest<ListNotificationResponse>(Request.Method.GET,
                url,
                ListNotificationResponse.class,
                headers,
                new Response.Listener<ListNotificationResponse>() {

                    @Override
                    public void onResponse(ListNotificationResponse response) {
                        Logger.d(TAG, "#getUploadDeviceTokenRequest -> onResponse ");
                        if (listener != null) {
                            listener.onResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#getUploadDeviceTokenRequest -> onErrorResponse");
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                }) {

        };
    }

    static GsonRequest<ListNotificationResponse> getListPushUnRead(final String token, int page, int limit,
                                                                   final Response.Listener<ListNotificationResponse> listener,
                                                                   final Response.ErrorListener errorListener) {
        String url = API_GET_LIST_PUSH_UN_READ + "?page=" + page + "&limit=" + limit;
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        return new GsonRequest<ListNotificationResponse>(Request.Method.GET,
                url,
                ListNotificationResponse.class,
                headers,
                new Response.Listener<ListNotificationResponse>() {

                    @Override
                    public void onResponse(ListNotificationResponse response) {
                        Logger.d(TAG, "#getUploadDeviceTokenRequest -> onResponse ");
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#getUploadDeviceTokenRequest -> onErrorResponse");
                        errorListener.onErrorResponse(error);
                    }
                }) {

        };
    }

    static GsonRequest<ResponseData> setListNotificationUnRead(final String token, final long pushId,
                                                               final Response.Listener<ResponseData> listener,
                                                               final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        return new GsonJsonRequest<ResponseData>(Request.Method.POST,
                API_SET_LIST_NOTIFICATION,
                ResponseData.class,
                headers,
                new Response.Listener<ResponseData>() {

                    @Override
                    public void onResponse(ResponseData response) {
                        Logger.d(TAG, "#setListNotificationUnRead -> onResponse ");
                        if (listener != null) {
                            listener.onResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#setListNotificationUnRead -> onErrorResponse");
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> map = new HashMap<>();
                map.put("push_id_list", String.valueOf(pushId));
                return map;
            }
        };
    }

    static GsonJsonRequest<ResponseData> updateActionPush(final String token, final long pushId,
                                                          final Response.Listener<ResponseData> listener,
                                                          final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        return new GsonJsonRequest<ResponseData>(Request.Method.POST,
                API_UPDATE_ACTION_NOTIFICATION,
                ResponseData.class,
                headers,
                new Response.Listener<ResponseData>() {

                    @Override
                    public void onResponse(ResponseData response) {
                        Logger.d(TAG, "#updateActionPush -> onResponse ");
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#updateActionPush -> onErrorResponse");
                        errorListener.onErrorResponse(error);
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> map = new HashMap<>();
                map.put("push_id", String.valueOf(pushId));
                return map;
            }
        };
    }
}
