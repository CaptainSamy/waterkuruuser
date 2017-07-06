package wssj.co.jp.point.model.memo;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import wssj.co.jp.point.model.ResponseData;
import wssj.co.jp.point.model.entities.UpdateMemoPhotoData;
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

    private static final String GET_LIST_COMPANY_URL = Constants.BASE_URL_AWS + "/api/client/users/get-list-company";

    private static final String GET_LIST_SERVICES_URL = Constants.BASE_URL_AWS + "/api/client/users/get-service-list";

    private static final String GET_USER_MEMO_URL = Constants.BASE_URL_AWS + "/api/client/users/get-memo-user";

    private static final String UPDATE_USER_MEMO_URL = Constants.BASE_URL_AWS + "/api/client/users/set-memo-user";

    static GsonRequest<ListCompanyResponse> getListCompany(String token, final Response.Listener<ListCompanyResponse> listener, final Response.ErrorListener errorListener) {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", token);
        header.put("Accept", "application/json");
        return new GsonRequest<>(
                Request.Method.GET, GET_LIST_COMPANY_URL,
                ListCompanyResponse.class,
                header,
                new Response.Listener<ListCompanyResponse>() {

                    @Override
                    public void onResponse(ListCompanyResponse response) {
                        Logger.i(TAG, "#getListCompany => onResponse");
                        listener.onResponse(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.i(TAG, "#getListCompany => onErrorResponse");
                errorListener.onErrorResponse(error);
            }
        });
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

    static GsonRequest<ResponseData> updateUserMemo(String token, final int serviceId, final String note, final UpdateMemoPhotoData[] images, final Response.Listener<ResponseData> responseListener, final Response.ErrorListener errorListener) {
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
                map.put("photo_1", Constants.EMPTY_STRING);
                map.put("photo_2", Constants.EMPTY_STRING);
                map.put("photo_3", Constants.EMPTY_STRING);
                map.put("photo_4", Constants.EMPTY_STRING);
                return map;
            }
        };

//    static GsonMultipartRequest<ResponseData> updateUserMemo(String token, final int serviceId, final String note, final UpdateMemoPhotoData[] images, final Response.Listener<ResponseData> listener, final Response.ErrorListener errorListener) {
//        Map<String, String> header = new HashMap<>();
//        header.put("Authorization", token);
//        header.put("Accept", "application/json");
//
//        return new GsonMultipartRequest<ResponseData>(
//                UPDATE_USER_MEMO_URL,
//                header,
//                ResponseData.class,
//                new Response.Listener<ResponseData>() {
//
//                    @Override
//                    public void onResponse(ResponseData response) {
//                        Logger.i(TAG, "@updateUserMemo => onResponse");
//                        if (listener != null) {
//                            listener.onResponse(response);
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Logger.i(TAG, "@updateUserMemo => onErrorResponse");
//                        if (errorListener != null) {
//                            errorListener.onErrorResponse(error);
//                        }
//                    }
//                }) {
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("service_id", String.valueOf(serviceId));
//                params.put("note", note);
//                for (int i = 0; i < images.length; i++) {
//                    if (images[i].getUpdateFlag() == UpdateMemoPhotoData.FLAG_DELETE) {
//                        params.put("photo_" + (i + 1), UpdateMemoPhotoData.PARAM_DELETE);
//                    }
//                }
//                return params;
//            }
//
//            @Override
//            protected Map<String, DataPart> getByteData() throws AuthFailureError {
//                Map<String, DataPart> params = new HashMap<>();
//                UpdateMemoPhotoData data;
//                for (int i = 0; i < images.length; i++) {
//                    data = images[i];
//                    if (data != null && data.getUpdateFlag() == UpdateMemoPhotoData.FLAG_MODIFIED) {
//                        params.put("photo_" + (i + 1), new DataPart(Utils.getUploadImageFileName(), data.getImage(), "image/jpeg"));
//                    }
//                }
//                return params;
//            }
//        };
//    }
    }
}
