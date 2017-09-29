package jp.co.wssj.iungo.model.checkin;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import jp.co.wssj.iungo.model.ResponseData;
import jp.co.wssj.iungo.model.volleyrequest.GsonJsonRequest;
import jp.co.wssj.iungo.model.volleyrequest.GsonRequest;
import jp.co.wssj.iungo.screens.pushobject.MappingUserStoreResponse;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;

/**
 * Created by Nguyen Huu Ta on 18/5/2017.
 */

final class APICreator {

    public static final String TAG = "CheckIn.APICreator";

    private static final String INFO_STORE_URL = Constants.BASE_URL + "/api/storeclient/management-users/get-store-information-by-code";

    private static final String FEEDBACK_QR_STORE_URL = Constants.BASE_URL + "/api/client/users/feedback-scan-qr-code-store";

    private static final String GET_CHECK_IN_STATUS_BY_USER = Constants.BASE_URL + "/api/client/users/get-check-in-status-by-user";

    private static final String MAPPING_USER_WITH_STORE = Constants.BASE_URL + "/api/client/users/feedback-ignore-session";

    private static final String MAPPING_USER_WITH_STORE_FAST = Constants.BASE_URL + "/api/client/users/check-in-fast";

    static GsonRequest<ConfirmCheckInResponse> getFeedbackQRStoreRequest(String token, final String code, final Response.Listener<ConfirmCheckInResponse> listener, final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", token);
        headers.put("Accept", "application/json");

        return new GsonJsonRequest<ConfirmCheckInResponse>(
                Request.Method.POST,
                FEEDBACK_QR_STORE_URL,
                ConfirmCheckInResponse.class,
                headers,
                new Response.Listener<ConfirmCheckInResponse>() {

                    @Override
                    public void onResponse(ConfirmCheckInResponse response) {
                        Logger.d(TAG, "#getFeedbackQRStoreRequest -> onResponse");
                        if (response != null) {
                            listener.onResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#getFeedbackQRStoreRequest -> onErrorResponse");
                        if (error != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> map = new HashMap<>();
                map.put("qr_code", code);
                return map;
            }
        };
    }

    static GsonRequest<CheckInStatusResponse> getCheckInStatusByUser(String token, final Response.Listener<CheckInStatusResponse> listener, final Response.ErrorListener errorListener) {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", token);
        header.put("Accept", "application/json");
        return new GsonRequest<>(Request.Method.GET,
                GET_CHECK_IN_STATUS_BY_USER,
                CheckInStatusResponse.class,
                header,
                new Response.Listener<CheckInStatusResponse>() {

                    @Override
                    public void onResponse(CheckInStatusResponse response) {
                        Logger.i(TAG, "#getCheckInStatusByUser => onResponse");
                        if (listener != null) {
                            listener.onResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.i(TAG, "#getCheckInStatusByUser => onErrorResponse");
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                });
    }

    static GsonRequest<InfoStoreResponse> getInfoStoreRequest(final String code, final Response.Listener<InfoStoreResponse> responseListener, final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        return new GsonJsonRequest<InfoStoreResponse>(Request.Method.POST,
                INFO_STORE_URL,
                InfoStoreResponse.class,
                headers,
                new Response.Listener<InfoStoreResponse>() {

                    @Override
                    public void onResponse(InfoStoreResponse response) {
                        Logger.d(TAG, "#getInfoStoreRequest -> onResponse");
                        if (responseListener != null) {
                            responseListener.onResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#getInfoStoreRequest -> onErrorResponse");
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> param = new HashMap<>();
                param.put("qr_code", code);
                return param;
            }
        };
    }

    static GsonRequest<MappingUserStoreResponse> mappingUserWithStore(String token, final String code, final Response.Listener<MappingUserStoreResponse> responseListener, final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        return new GsonJsonRequest<MappingUserStoreResponse>(Request.Method.POST,
                MAPPING_USER_WITH_STORE,
                MappingUserStoreResponse.class,
                headers,
                new Response.Listener<MappingUserStoreResponse>() {

                    @Override
                    public void onResponse(MappingUserStoreResponse response) {
                        Logger.d(TAG, "#mappingUserWithStore -> onResponse");
                        if (responseListener != null) {
                            responseListener.onResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#mappingUserWithStore -> onErrorResponse");
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> param = new HashMap<>();
                param.put("code", code);
                return param;
            }
        };
    }

    static GsonRequest<ResponseData> mappingUserWithStoreFast(String token, final String code, final Response.Listener<ResponseData> responseListener, final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        return new GsonJsonRequest<ResponseData>(Request.Method.POST,
                MAPPING_USER_WITH_STORE_FAST,
                ResponseData.class,
                headers,
                new Response.Listener<ResponseData>() {

                    @Override
                    public void onResponse(ResponseData response) {
                        Logger.d(TAG, "#mappingUserWithStoreFast -> onResponse");
                        responseListener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#mappingUserWithStoreFast -> onErrorResponse");
                        errorListener.onErrorResponse(error);
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> param = new HashMap<>();
                param.put("code_store", code);
                return param;
            }
        };
    }
}
