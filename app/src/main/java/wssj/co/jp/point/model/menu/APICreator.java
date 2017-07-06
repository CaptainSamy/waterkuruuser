package wssj.co.jp.point.model.menu;

import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

import wssj.co.jp.point.model.ResponseData;
import wssj.co.jp.point.model.volleylistener.ResponseListener;
import wssj.co.jp.point.model.volleyrequest.GsonJsonRequest;
import wssj.co.jp.point.model.volleyrequest.GsonRequest;
import wssj.co.jp.point.utils.Constants;

/**
 * Created by Nguyen Huu Ta on 5/7/2017.
 */

final class APICreator {

    private static final String TAG = "Menu.APICreator";

    private static final String GET_LIST_QA_URL = Constants.BASE_URL_AWS + "/api/client/users/get-list-question";

    private static final String FEED_BACK_URL = Constants.BASE_URL_AWS + "/api/client/users/save-user-contact";

    private static final String HOW_USE_APP_URL = Constants.BASE_URL_AWS + "/api/client/users/how-use-app";

    private static final String POLICY_URL = Constants.BASE_URL_AWS + "/api/client/users/get-term-of-service";

    static GsonRequest<QAResponse> getListQA(String token, int page, int limit, final Response.Listener<QAResponse> responseListener,
                                             final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);

        String url = GET_LIST_QA_URL + "?page=" + page + "&limit=" + limit;
        ResponseListener<QAResponse> listener = new ResponseListener<>(TAG, "#getListQA", responseListener, errorListener);
        return new GsonJsonRequest<>(Request.Method.GET,
                url,
                QAResponse.class,
                headers,
                listener,
                listener);
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
                params.put("info", feedback);
                return params;
            }
        };
    }

    static GsonRequest<HowUseAppResponse> howUseApp(String token, final Response.Listener<HowUseAppResponse> responseListener,
                                                    final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);

        ResponseListener<HowUseAppResponse> listener = new ResponseListener<>(TAG, "#getListQA", responseListener, errorListener);
        return new GsonJsonRequest<>(Request.Method.GET,
                HOW_USE_APP_URL,
                HowUseAppResponse.class,
                headers,
                listener,
                listener);
    }

    static GsonRequest<HowUseAppResponse> policy(final Response.Listener<HowUseAppResponse> responseListener,
                                                 final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");

        ResponseListener<HowUseAppResponse> listener = new ResponseListener<>(TAG, "#getListQA", responseListener, errorListener);
        return new GsonJsonRequest<>(Request.Method.GET,
                POLICY_URL,
                HowUseAppResponse.class,
                headers,
                listener,
                listener);
    }
}
