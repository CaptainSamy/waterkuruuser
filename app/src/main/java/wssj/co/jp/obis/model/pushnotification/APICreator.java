package wssj.co.jp.obis.model.pushnotification;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wssj.co.jp.obis.model.ResponseData;
import wssj.co.jp.obis.model.volleyrequest.GsonJsonRequest;
import wssj.co.jp.obis.model.volleyrequest.GsonRequest;
import wssj.co.jp.obis.utils.Constants;
import wssj.co.jp.obis.utils.Logger;

/**
 * Created by tuanle on 6/7/17.
 */

public class APICreator {

    private static final String TAG = "Notification.ApiCreator";

    private static final String API_GET_LIST_NOTIFICATION = Constants.BASE_URL + "/api/client/users/get-notification-list-user";

//    private static final String API_GET_LIST_PUSH_QUESTION_NAIRE = Constants.BASE_URL + "/api/client/users/get-questionnaire-list-user";

    private static final String API_GET_LIST_NOTIFICATION_FOR_STORE_ANNOUNCE = Constants.BASE_URL + "/api/client/users/get-list-push-notification-by-service-company-id";

    private static final String API_GET_LIST_PUSH_UN_READ = Constants.BASE_URL + "/api/client/users/get-notification-list-user-unread";

    private static final String API_SET_LIST_NOTIFICATION = Constants.BASE_URL + "/api/client/users/update-view-status-notification";

    private static final String API_UPDATE_ACTION_NOTIFICATION = Constants.BASE_URL + "/api/client/users/update-push-notification";

    private static final String API_GET_CONTENT_PUSH = Constants.BASE_URL + "/api/client/users/get-push-notification-by-id";

//    private static final String API_GET_QUESTION_NAIRE = Constants.BASE_URL + "/api/client/users/get-code-to-survey";

    static GsonRequest<ListNotificationResponse> getListNotification(final String token, final long userPushId, final int isSearch, final String keySearch, final int serviceCompanyId, final int typePush, final String fromDate, final String toDate,
                                                                     final Response.Listener<ListNotificationResponse> listener,
                                                                     final Response.ErrorListener errorListener) {

        String url;

        switch (typePush) {
            case Constants.TypePush.TYPE_ALL_PUSH:
                url = API_GET_LIST_NOTIFICATION;
                break;
            case Constants.TypePush.TYPE_PUSH_ANNOUNCE:
                url = API_GET_LIST_NOTIFICATION_FOR_STORE_ANNOUNCE;
                break;
//            case Constants.TypePush.TYPE_QUESTION_NAIRE_PUSH:
//                url = API_GET_LIST_PUSH_QUESTION_NAIRE;
//                break;
            default:
                url = API_GET_LIST_NOTIFICATION;
                break;

        }
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        return new GsonJsonRequest<ListNotificationResponse>(Request.Method.POST,
                url,
                ListNotificationResponse.class,
                headers,
                Constants.TIME_OUT_CUSTOM,
                new Response.Listener<ListNotificationResponse>() {

                    @Override
                    public void onResponse(ListNotificationResponse response) {
                        Logger.d(TAG, "#getListNotification -> onResponse ");
                        if (listener != null) {
                            listener.onResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#getListNotification -> onErrorResponse");
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> map = new HashMap<>();

                switch (typePush) {
                    case Constants.TypePush.TYPE_PUSH_ANNOUNCE:
                        map.put("service_company_id", serviceCompanyId);
                        break;
                }
                map.put("last_user_push_id", userPushId);
                map.put("limit", Constants.LIMIT);
                if (isSearch == 1) {
                    map.put("is_search", isSearch);
                    map.put("text_search", keySearch);
                }
                map.put("fromdate", fromDate);
                map.put("todate", toDate);
                return map;
            }
        };
    }

//    static GsonRequest<ListNotificationResponse> getListPushQuestionNaire(final String token, final long userPushId, final int isSearch, final String keySearch,
//                                                                          final Response.Listener<ListNotificationResponse> listener,
//                                                                          final Response.ErrorListener errorListener) {
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Accept", "application/json");
//        headers.put("Authorization", token);
//        return new GsonJsonRequest<ListNotificationResponse>(Request.Method.POST,
//                API_GET_LIST_PUSH_QUESTION_NAIRE,
//                ListNotificationResponse.class,
//                headers,
//                new Response.Listener<ListNotificationResponse>() {
//
//                    @Override
//                    public void onResponse(ListNotificationResponse response) {
//                        Logger.d(TAG, "#getListNotification -> onResponse ");
//                        if (listener != null) {
//                            listener.onResponse(response);
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Logger.d(TAG, "#getListNotification -> onErrorResponse");
//                        if (errorListener != null) {
//                            errorListener.onErrorResponse(error);
//                        }
//                    }
//                }) {
//
//            @Override
//            protected Map<String, Object> getBodyParams() {
//                Map<String, Object> map = new HashMap<>();
//                map.put("last_user_push_id", userPushId);
//                map.put("limit", Constants.LIMIT);
//                if (isSearch == 1) {
//                    map.put("is_search", isSearch);
//                    map.put("text_search", keySearch);
//                }
//                return map;
//            }
//        };
//    }

    static GsonRequest<ListNotificationResponse> getListNotificationForStoreAnnounce(final String token, final int serviceCompanyId, final long lastUserPushId, final int isSearch, final String keySearch,
                                                                                     final Response.Listener<ListNotificationResponse> listener,
                                                                                     final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        return new GsonJsonRequest<ListNotificationResponse>(Request.Method.POST,
                API_GET_LIST_NOTIFICATION_FOR_STORE_ANNOUNCE,
                ListNotificationResponse.class,
                headers,
                Constants.TIME_OUT_CUSTOM,
                new Response.Listener<ListNotificationResponse>() {

                    @Override
                    public void onResponse(ListNotificationResponse response) {
                        Logger.d(TAG, "#getListNotification -> onResponse ");
                        if (listener != null) {
                            listener.onResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#getListNotification -> onErrorResponse");
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> params = new HashMap<>();
                params.put("service_company_id", serviceCompanyId);
                params.put("last_user_push_id", lastUserPushId);
                params.put("limit", Constants.LIMIT);
                if (isSearch == 1) {
                    params.put("is_search", isSearch);
                    params.put("text_search", keySearch);
                }
                return params;
            }
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
                        Logger.d(TAG, "#getListNotification -> onResponse ");
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#getListNotification -> onErrorResponse");
                        errorListener.onErrorResponse(error);
                    }
                }) {

        };
    }

    static GsonRequest<ResponseData> setListNotificationUnRead(final String token, final List<Long> pushId, final int type,
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
                map.put("type_read", type);
                String listPush = Constants.EMPTY_STRING;
                for (long push : pushId) {
                    if (pushId.indexOf(push) == (pushId.size() - 1)) {
                        listPush = listPush + push;
                    } else {
                        listPush = listPush + push + ",";
                    }
                }
                map.put("push_id_list", listPush);
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
                map.put("push_id", pushId);
                return map;
            }
        };
    }

    static GsonJsonRequest<ContentPushResponse> getContentPush(final String token, final long pushId,
                                                               final Response.Listener<ContentPushResponse> listener,
                                                               final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        return new GsonJsonRequest<ContentPushResponse>(Request.Method.POST,
                API_GET_CONTENT_PUSH,
                ContentPushResponse.class,
                headers,
                new Response.Listener<ContentPushResponse>() {

                    @Override
                    public void onResponse(ContentPushResponse response) {
                        Logger.d(TAG, "#getContentPush -> onResponse ");
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#getContentPush -> onErrorResponse");
                        errorListener.onErrorResponse(error);
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> map = new HashMap<>();
                map.put("push_id", pushId);
                return map;
            }
        };
    }

//    static GsonJsonRequest<QuestionNaireResponse> getQuestionNaire(final String token, final long pushId,
//                                                                   final Response.Listener<QuestionNaireResponse> listener,
//                                                                   final Response.ErrorListener errorListener) {
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Accept", "application/json");
//        headers.put("Authorization", token);
//        return new GsonJsonRequest<QuestionNaireResponse>(Request.Method.POST,
//                API_GET_QUESTION_NAIRE,
//                QuestionNaireResponse.class,
//                headers,
//                new Response.Listener<QuestionNaireResponse>() {
//
//                    @Override
//                    public void onResponse(QuestionNaireResponse response) {
//                        Logger.d(TAG, "#getContentPush -> onResponse ");
//                        listener.onResponse(response);
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Logger.d(TAG, "#getContentPush -> onErrorResponse");
//                        errorListener.onErrorResponse(error);
//                    }
//                }) {
//
//            @Override
//            protected Map<String, Object> getBodyParams() {
//                Map<String, Object> map = new HashMap<>();
//                map.put("push_id", pushId);
//                return map;
//            }
//        };
//    }
}
