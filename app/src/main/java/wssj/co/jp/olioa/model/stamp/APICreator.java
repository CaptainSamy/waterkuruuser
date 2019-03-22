package wssj.co.jp.olioa.model.stamp;

import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

import wssj.co.jp.olioa.model.ResponseData;
import wssj.co.jp.olioa.model.volleylistener.ResponseListener;
import wssj.co.jp.olioa.model.volleyrequest.GsonJsonRequest;
import wssj.co.jp.olioa.model.volleyrequest.GsonRequest;
import wssj.co.jp.olioa.utils.Constants;

/**
 * Created by HieuPT on 5/18/2017.
 */

final class APICreator {

    private static final String TAG = "stamp.APICreator";

    private static final String LIST_COMPANY_URL = Constants.BASE_URL + "/api/client/users/get-list-company";

    private static final String LIST_CARD_BY_SERVICE_COMPANY_URL = Constants.BASE_URL + "/api/client/users/get-list-card-by-service-company-id";

    private static final String LIST_STORE_CHECK_IN = Constants.BASE_URL + "/api/client/users/get-store-list-of-company-which-user-has-used";

    private static final String REVIEW_SERVICE_BY_STAMP = Constants.BASE_URL + "/api/client/users/review-service-by-stamp";

    static GsonRequest<ListCompanyResponse> createGetListCompanyRequest(String token, Response.Listener<ListCompanyResponse> responseListener, Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        ResponseListener<ListCompanyResponse> listener = new ResponseListener<>(TAG, "createGetListCompanyRequest", responseListener, errorListener);
        return new GsonRequest<>(Request.Method.GET,
                LIST_COMPANY_URL,
                ListCompanyResponse.class,
                headers,
                listener,
                listener);
    }

    static GsonRequest<ListCardResponse> createGetListCardByServiceCompanyRequest(String token, final int serviceCompanyId, int page, int limit,
                                                                                  Response.Listener<ListCardResponse> responseListener, Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);

        String url = LIST_CARD_BY_SERVICE_COMPANY_URL + "?page=" + page + "&limit=" + limit;

        ResponseListener<ListCardResponse> listener = new ResponseListener<>(TAG, "createGetListCardByServiceCompanyRequest", responseListener, errorListener);
        return new GsonJsonRequest<ListCardResponse>(Request.Method.POST,
                url,
                ListCardResponse.class,
                headers,
                listener,
                listener) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> params = new HashMap<>();
                params.put("service_company_id", String.valueOf(serviceCompanyId));
                return params;
            }

        };
    }

    static GsonJsonRequest<ListStoreCheckedResponse> getListStoreCheckedIn(String token, final int serviceCompanyId, final double latitude, final double longitude, Response.Listener<ListStoreCheckedResponse> responseListener, Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);

        String url = LIST_STORE_CHECK_IN + "?service_company_id=" + serviceCompanyId;
        ResponseListener<ListStoreCheckedResponse> listener = new ResponseListener<>(TAG, "getListStoreCheckedIn", responseListener, errorListener);
        return new GsonJsonRequest<ListStoreCheckedResponse>(Request.Method.POST,
                url,
                ListStoreCheckedResponse.class,
                headers,
                listener,
                listener) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> param = new HashMap<>();
                param.put("latitude", latitude);
                param.put("longitude", longitude);
                return param;
            }
        };
    }

    static GsonRequest<ResponseData> reviewServiceByStamp(String token, final int stampId, final float rating, final String note, Response.Listener<ResponseData> responseListener, Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);

        ResponseListener<ResponseData> listener = new ResponseListener<>(TAG, "reviewServiceByStamp", responseListener, errorListener);
        return new GsonJsonRequest<ResponseData>(Request.Method.POST,
                REVIEW_SERVICE_BY_STAMP,
                ResponseData.class,
                headers,
                listener,
                listener) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> param = new HashMap<>();
                param.put("stamp_id", String.valueOf(stampId));
                param.put("star_review", String.valueOf(rating));
                param.put("text_review", String.valueOf(note));
                return param;
            }

        };
    }

    private APICreator() {
    }
}
