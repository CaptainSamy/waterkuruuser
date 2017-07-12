package wssj.co.jp.point.model.memo;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import wssj.co.jp.point.model.ResponseData;
import wssj.co.jp.point.model.entities.StatusMemoData;
import wssj.co.jp.point.model.volleylistener.ResponseListener;
import wssj.co.jp.point.model.volleyrequest.GsonJsonRequest;
import wssj.co.jp.point.model.volleyrequest.GsonRequest;
import wssj.co.jp.point.utils.Constants;
import wssj.co.jp.point.utils.Logger;

/**
 * Created by Nguyen Huu Ta on 19/5/2017.
 */

final class APICreator {

    private static final String TAG = "Note.APICreator";

    private static final String GET_LIST_SERVICES_URL = Constants.BASE_URL_AWS + "/api/client/users/get-service-list";

    private static final String GET_USER_MEMO_URL = Constants.BASE_URL_AWS + "/api/client/users/get-memo-user";

    private static final String UPDATE_USER_MEMO_URL = Constants.BASE_URL_AWS + "/api/client/users/set-memo-user";

    static GsonRequest<MemoDynamicResponse> getUserMemoConfigRequest(String token, final int servicesId, final Response.Listener<MemoDynamicResponse> responseListener, final Response.ErrorListener errorListener) {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", token);
        header.put("Accept", "application/json");
        ResponseListener<MemoDynamicResponse> listener = new ResponseListener<>(TAG, "#getUserMemoConfigRequest", responseListener, errorListener);
        return new GsonJsonRequest<MemoDynamicResponse>(Request.Method.POST, GET_USER_MEMO_URL, MemoDynamicResponse.class, header, listener, listener) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> map = new HashMap<>();
                map.put("service_id", String.valueOf(servicesId));
                return map;
            }
        };
    }

    static GsonRequest<ListServiceResponse> getListService(String token, final Response.Listener<ListServiceResponse> listener, final Response.ErrorListener errorListener) {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", token);
        header.put("Accept", "application/json");
        return new GsonRequest<>(
                Request.Method.GET, GET_LIST_SERVICES_URL,
                ListServiceResponse.class,
                header,
                new Response.Listener<ListServiceResponse>() {

                    @Override
                    public void onResponse(ListServiceResponse response) {
                        Logger.i(TAG, "#getListService => onResponse");
                        listener.onResponse(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.i(TAG, "#getListService => onErrorResponse");
                errorListener.onErrorResponse(error);
            }
        });
    }

    static GsonRequest<UserMemoResponse> getUserMemoRequest(String token, final int servicesId, final Response.Listener<UserMemoResponse> responseListener, final Response.ErrorListener errorListener) {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", token);
        header.put("Accept", "application/json");
        ResponseListener<UserMemoResponse> listener = new ResponseListener<>(TAG, "#updateUserMemo", responseListener, errorListener);
        return new GsonJsonRequest<UserMemoResponse>(Request.Method.POST, GET_USER_MEMO_URL, UserMemoResponse.class, header, listener, listener) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> map = new HashMap<>();
                map.put("service_id", String.valueOf(servicesId));
                return map;
            }
        };
    }

    static GsonRequest<ResponseData> updateUserMemo(String token, final int serviceId, final String note, final StatusMemoData[] listStatusImage, final Response.Listener<ResponseData> responseListener, final Response.ErrorListener errorListener) {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", token);
        header.put("Accept", "application/json");
        ResponseListener<ResponseData> listener = new ResponseListener<>(TAG, "#updateUserMemo", responseListener, errorListener);
        return new GsonJsonRequest<ResponseData>(
                Request.Method.POST,
                UPDATE_USER_MEMO_URL,
                ResponseData.class,
                header,
                listener,
                listener) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> map = new HashMap<>();
                map.put("service_id", String.valueOf(serviceId));
                map.put("note", note);
                if (listStatusImage != null) {
                    int position = 1;
                    for (StatusMemoData statusMemoData : listStatusImage) {
                        if (statusMemoData.getStatus() != 1 && statusMemoData.isUploadAWSSuccess()) {
                            map.put("photo_" + position, statusMemoData.getPathNewImage());
                        } else {
                            map.put("photo_" + position, statusMemoData.getUrlOriginImage());
                        }
                        position++;
                    }
                }
                return map;
            }
        };
    }
}
