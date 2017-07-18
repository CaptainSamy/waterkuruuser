package jp.co.wssj.iungo.model.auth;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import jp.co.wssj.iungo.model.ResponseData;
import jp.co.wssj.iungo.model.volleylistener.ResponseListener;
import jp.co.wssj.iungo.model.volleyrequest.GsonJsonRequest;
import jp.co.wssj.iungo.model.volleyrequest.GsonRequest;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

final class APICreator {

    private static final String TAG = "AuthModel.APICreator";

    private static final String REGISTER_URL_AWS = Constants.BASE_URL_AWS + "/api/client/users/register";

    private static final String LOGIN_URL_AWS = Constants.BASE_URL_AWS + "/api/client/users/login";

    private static final String RESET_PASSWORD_URL = Constants.BASE_URL_AWS + "/api/client/users/send-email-contain-reset-password-code";

    private static final String CHANGE_PASSWORD_BY_CODE_URL = Constants.BASE_URL_AWS + "/api/client/users/change-password-by-code";

    private static final String CHANGE_PASSWORD_URL = Constants.BASE_URL_AWS + "/api/client/users/change-password";

    public static GsonRequest<RegisterResponse> getRegisterAWSRequest(final String userName, final String password, final String name, final String email, final Response.Listener<RegisterResponse> listener, final Response.ErrorListener errorListener) {
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
                param.put("username", userName);
                param.put("password", password);
                param.put("name", name);
                param.put("email", email);
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

    static GsonRequest<RegisterResponse> changePassword(String token, final String currentPassword, final String newPassword, final Response.Listener<RegisterResponse> responseListener,
                                                        final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        ResponseListener<RegisterResponse> listener = new ResponseListener<>(TAG, "changePassword", responseListener, errorListener);
        return new GsonJsonRequest<RegisterResponse>(Request.Method.POST,
                CHANGE_PASSWORD_URL,
                RegisterResponse.class,
                headers,
                listener,
                listener) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> params = new HashMap<>();
                params.put("old_password", currentPassword);
                params.put("new_password", newPassword);
                return params;
            }


        };
    }

    private APICreator() {
    }
}
