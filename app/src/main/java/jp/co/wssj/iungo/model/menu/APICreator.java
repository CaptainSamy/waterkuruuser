package jp.co.wssj.iungo.model.menu;

import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

import jp.co.wssj.iungo.model.ResponseData;
import jp.co.wssj.iungo.model.volleylistener.ResponseListener;
import jp.co.wssj.iungo.model.volleyrequest.GsonJsonRequest;
import jp.co.wssj.iungo.model.volleyrequest.GsonRequest;
import jp.co.wssj.iungo.utils.Constants;

/**
 * Created by Nguyen Huu Ta on 5/7/2017.
 */

final class APICreator {

    private static final String TAG = "Menu.APICreator";

    private static final String GET_LIST_QA_URL = Constants.BASE_URL_AWS + "/api/client/users/get-list-question";

    private static final String FEED_BACK_URL = Constants.BASE_URL_AWS + "/api/client/users/save-user-contact";

    private static final String HOW_USE_APP_URL = Constants.BASE_URL_AWS + "/api/client/users/how-use-app";

    private static final String TERM_OF_SERVICE_URL = Constants.BASE_URL_AWS + "/api/client/users/get-term-of-service";

    private static final String POLICY_URL = Constants.BASE_URL_AWS + "/api/client/users/get-policy";

    static GsonRequest<QAResponse> getListQA(String token, final int page, int limit, final Response.Listener<QAResponse> responseListener,
                                             final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);

        String url = GET_LIST_QA_URL + "?page=" + page + "&limit=" + limit;
        ResponseListener<QAResponse> listener = new ResponseListener<>(TAG, "#getListQA", responseListener, errorListener);
        return new GsonJsonRequest<QAResponse>(Request.Method.POST,
                url,
                QAResponse.class,
                headers,
                listener,
                listener) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> param = new HashMap<>();
                /*
                * 2 : Ios
                * 3 : Android
                * */
                param.put("system", 3);
                return param;
            }
        };
    }

    static GsonRequest<ResponseData> feedback(String token, final String feedback, final Response.Listener<ResponseData> responseListener,
                                              final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);

        ResponseListener<ResponseData> listener = new ResponseListener<>(TAG, "#feedback", responseListener, errorListener);
        return new GsonJsonRequest<ResponseData>(Request.Method.POST,
                FEED_BACK_URL,
                ResponseData.class,
                headers,
                listener,
                listener) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> params = new HashMap<>();
                params.put("ơ", feedback);
                return params;
            }
        };
    }

    static GsonRequest<HtmlResponse> howUseApp(String token, final Response.Listener<HtmlResponse> responseListener,
                                               final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);

        ResponseListener<HtmlResponse> listener = new ResponseListener<>(TAG, "#getListQA", responseListener, errorListener);
        return new GsonJsonRequest<>(Request.Method.GET,
                HOW_USE_APP_URL,
                HtmlResponse.class,
                headers,
                listener,
                listener);
    }

    static GsonRequest<HtmlResponse> policy(final Response.Listener<HtmlResponse> responseListener,
                                            final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");

        ResponseListener<HtmlResponse> listener = new ResponseListener<>(TAG, "#getListQA", responseListener, errorListener);
        return new GsonJsonRequest<>(Request.Method.GET,
                POLICY_URL,
                HtmlResponse.class,
                headers,
                listener,
                listener);
    }

    static GsonRequest<HtmlResponse> termOfService(final Response.Listener<HtmlResponse> responseListener,
                                                   final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");

        ResponseListener<HtmlResponse> listener = new ResponseListener<>(TAG, "#termOfService", responseListener, errorListener);
        return new GsonJsonRequest<>(Request.Method.GET,
                TERM_OF_SERVICE_URL,
                HtmlResponse.class,
                headers,
                listener,
                listener);
    }
}
