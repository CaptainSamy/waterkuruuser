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
import jp.co.wssj.iungo.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

final class APICreator {

    private static final String TAG = "AuthModel.APICreator";

    private static final String REGISTER_URL_AWS = Constants.BASE_URL + "/api/client/users/register";

    private static final String LOGIN_URL_AWS = Constants.BASE_URL + "/api/client/users/login";

    private static final String RESET_PASSWORD_URL = Constants.BASE_URL + "/api/client/users/send-email-contain-reset-password-code";

    private static final String CHANGE_PASSWORD_BY_CODE_URL = Constants.BASE_URL + "/api/client/users/change-password-by-code";

    private static final String CHANGE_PASSWORD_URL = Constants.BASE_URL + "/api/client/users/change-password";

    private static final String REMOVE_DEVICE_TOKEN = Constants.BASE_URL + "/api/client/users/remove-device-token";

    private static final String CHECK_VERSION_APP = Constants.BASE_URL + "/api/client/users/check-version-app-and-server-user";

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
                params.put("password", Utils.toMD5(newPassword));
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

    /*
    *
    * app_type: 1 (user app)
                2 (store app)
       os_type: 1 (android)
    * */
    static GsonRequest<CheckVersionAppResponse> checkVersionApp(String token, final int versionApp, final String deviceId, final Response.Listener<CheckVersionAppResponse> responseListener,
                                                                final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        ResponseListener<CheckVersionAppResponse> listener = new ResponseListener<>(TAG, "removeDeviceToken", responseListener, errorListener);
        return new GsonJsonRequest<CheckVersionAppResponse>(Request.Method.POST,
                CHECK_VERSION_APP,
                CheckVersionAppResponse.class,
                headers,
                listener,
                listener) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> params = new HashMap<>();
                params.put("device_id", deviceId);
                params.put("current_version", versionApp);
                params.put("app_type", 1);
                params.put("os_type", 1);
                return params;
            }
        };
    }

    static GsonRequest<InfoUserResponse> onGetInfoUser(String token, final Response.Listener<InfoUserResponse> responseListener,
                                                       final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        ResponseListener<InfoUserResponse> listener = new ResponseListener<>(TAG, "onGetInfoUser", responseListener, errorListener);
        return new GsonJsonRequest<InfoUserResponse>(Request.Method.GET,
                ON_GET_INFO_USER,
                InfoUserResponse.class,
                headers,
                listener,
                listener) {

        };
    }

    static GsonRequest<ResponseData> onUpdateInfoUser(String token, final InfoUserResponse.InfoUser infoUser, final Response.Listener<ResponseData> responseListener,
                                                      final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        ResponseListener<ResponseData> listener = new ResponseListener<>(TAG, "onUpdateInfoUser", responseListener, errorListener);
        return new GsonJsonRequest<ResponseData>(Request.Method.POST,
                ON_SET_INFO_USER,
                ResponseData.class,
                headers,
                listener,
                listener) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> map = new HashMap<>();
                map.put("name", infoUser.getName());
                map.put("age_avg", infoUser.getAvg());
                map.put("img_url", infoUser.getAvatar());
                map.put("email", infoUser.getEmail());
                map.put("sex", infoUser.getSex());
                String currentPassword = Constants.EMPTY_STRING;
                String newPassword = Constants.EMPTY_STRING;
                if (infoUser.isChangePassword()) {
                    currentPassword = Utils.toMD5(infoUser.getCurrentPassword());
                    newPassword = Utils.toMD5(infoUser.getNewPassword());
                }
                map.put("old_pass", currentPassword);
                map.put("new_pass", newPassword);
                return map;
            }
        };
    }

    private APICreator() {
    }
}
