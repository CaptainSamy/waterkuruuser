package wssj.co.jp.olioa.model.auth;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import wssj.co.jp.olioa.model.ResponseData;
import wssj.co.jp.olioa.model.volleylistener.ResponseListener;
import wssj.co.jp.olioa.model.volleyrequest.GsonJsonRequest;
import wssj.co.jp.olioa.model.volleyrequest.GsonRequest;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.utils.Logger;
import wssj.co.jp.olioa.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

final class APICreator {

    private static final String TAG = "AuthModel.APICreator";

    private static final String REGISTER_URL_AWS = Constants.BASE_URL + "/api/client/users/register";

    private static final String LOGIN_URL_AWS = Constants.BASE_URL + "/api/client/users/login";

    private static final String AUTO_LOGIN = Constants.BASE_URL + "/api/client/users/register_anonymous_user";

    private static final String RESET_PASSWORD_URL = Constants.BASE_URL + "/api/client/users/send-email-contain-reset-password-code";

    private static final String CHANGE_PASSWORD_BY_CODE_URL = Constants.BASE_URL + "/api/client/users/change-password-by-code";

    private static final String CHANGE_PASSWORD_URL = Constants.BASE_URL + "/api/client/users/change-password";

    private static final String REMOVE_DEVICE_TOKEN = Constants.BASE_URL + "/api/client/users/remove-device-token";

    private static final String CHECK_VERSION_APP = Constants.BASE_URL + "/api/client/users/check-version-app-and-server-user";

    private static final String ON_CHECK_INIT_USER = Constants.BASE_URL + "/api/client/users/check-init-user-done";
    private static final String ON_GET_INFO_USER = Constants.BASE_URL + "/api/client/users/get-info-user";

    private static final String ON_SET_INFO_USER = Constants.BASE_URL + "/api/client/users/set-info-user";

    public static GsonRequest<RegisterResponse> getRegisterAWSRequest(final String userId, final String password, final String name, final String email, final int age, final int sex, final int typeLogin, final String token, final Response.Listener<RegisterResponse> listener, final Response.ErrorListener errorListener) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        return new GsonJsonRequest<RegisterResponse>(Request.Method.POST, REGISTER_URL_AWS, RegisterResponse.class, headers,
                new Response.Listener<RegisterResponse>() {

                    @Override
                    public void onResponse(RegisterResponse response) {
                        Logger.d(TAG, "#getRegisterRequest -> onResponse");
                        if (response != null) {
                            listener.onResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#getRegisterRequest -> onErrorResponse");
                        if (email != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> param = new HashMap<>();
                param.put("username", userId);
                param.put("password", password);
                param.put("name", name);
                param.put("email", email);
                param.put("age_avg", age);
                param.put("sex", sex);
                param.put("type_login", typeLogin);
                param.put("token", token);
                param.put("app_id", Constants.APP_ID);
                return param;
            }

        };
    }

    static GsonRequest<LoginResponse> getLoginAWSRequest(final String username,
                                                         final String password, final Response.Listener<LoginResponse> listener,
                                                         final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        return new GsonJsonRequest<LoginResponse>(Request.Method.POST,
                LOGIN_URL_AWS,
                LoginResponse.class,
                headers,
                new Response.Listener<LoginResponse>() {

                    @Override
                    public void onResponse(LoginResponse response) {
                        Logger.d(TAG, "#getLoginAWSRequest -> onResponse");
                        if (listener != null) {
                            listener.onResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#getLoginAWSRequest -> onErrorResponse");
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                params.put("app_id", Constants.APP_ID);
                return params;
            }
        };
    }

    static GsonRequest<LoginResponse> autoLogin(final Response.Listener<LoginResponse> listener,
                                                final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        return new GsonJsonRequest<LoginResponse>(Request.Method.POST,
                AUTO_LOGIN,
                LoginResponse.class,
                headers,
                new Response.Listener<LoginResponse>() {

                    @Override
                    public void onResponse(LoginResponse response) {
                        Logger.d(TAG, "#autoLogin -> onResponse");
                        if (listener != null) {
                            listener.onResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#autoLogin -> onErrorResponse");
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> params = new HashMap<>();
                params.put("store_key_gen", Constants.KEY_GEN_AUTO_LOGIN);
                return params;
            }
        };
    }

    static GsonRequest<ResponseData> resetPassword(final String username, final Response.Listener<ResponseData> listener,
                                                   final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        return new GsonJsonRequest<ResponseData>(Request.Method.POST,
                RESET_PASSWORD_URL,
                ResponseData.class,
                headers,
                new Response.Listener<ResponseData>() {

                    @Override
                    public void onResponse(ResponseData response) {
                        Logger.d(TAG, "#resetPassword -> onResponse");
                        if (listener != null) {
                            listener.onResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#resetPassword -> onErrorResponse");
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> params = new HashMap<>();
                params.put("username_or_email", username);
                return params;
            }


        };
    }

    static GsonRequest<RegisterResponse> changePasswordByCode(final String code, final String newPassword, final Response.Listener<RegisterResponse> listener,
                                                              final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        return new GsonJsonRequest<RegisterResponse>(Request.Method.POST,
                CHANGE_PASSWORD_BY_CODE_URL,
                RegisterResponse.class,
                headers,
                new Response.Listener<RegisterResponse>() {

                    @Override
                    public void onResponse(RegisterResponse response) {
                        Logger.d(TAG, "#resetPassword -> onResponse");
                        if (listener != null) {
                            listener.onResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#resetPassword -> onErrorResponse");
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> params = new HashMap<>();
                params.put("code", code);
                params.put("password", newPassword);
                return params;
            }


        };
    }


    static GsonRequest<ResponseData> removeDeviceToken(String token, final String deviceId, final Response.Listener<ResponseData> responseListener,
                                                       final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        ResponseListener<ResponseData> listener = new ResponseListener<>(TAG, "removeDeviceToken", responseListener, errorListener);
        return new GsonJsonRequest<ResponseData>(Request.Method.POST,
                REMOVE_DEVICE_TOKEN,
                ResponseData.class,
                headers,
                listener,
                listener) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> params = new HashMap<>();
                params.put("device_id", deviceId);
                return params;
            }


        };
    }


    private APICreator() {
    }
}
