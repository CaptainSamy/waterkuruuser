package wssj.co.jp.obis.model.coupone;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import wssj.co.jp.obis.model.ResponseData;
import wssj.co.jp.obis.model.volleyrequest.GsonJsonRequest;
import wssj.co.jp.obis.model.volleyrequest.GsonRequest;
import wssj.co.jp.obis.utils.Constants;
import wssj.co.jp.obis.utils.Logger;

/**
 * Created by thang on 1/2/2018.
 */

public class APICreator {
    private static final String API_GET_LIST_COUPON_BY_USER = Constants.BASE_URL + "/api/client/users/get-list-coupon-by-user";
    private static final String TAG = "Coupon API";

    private static final String API_USE_COUPON=Constants.BASE_URL+"/api/client/users/use-coupon";

    static GsonRequest<ListCouponeResponse> getListCoupon(final String token,final int lastCouponId,final int limit,final int isUsed,
                                                                                     final Response.Listener<ListCouponeResponse> listener,
                                                                                     final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        return new GsonJsonRequest<ListCouponeResponse>(Request.Method.POST,
                API_GET_LIST_COUPON_BY_USER,
                ListCouponeResponse.class,
                headers,
                Constants.TIME_OUT_CUSTOM,
                new Response.Listener<ListCouponeResponse>() {
                    @Override
                    public void onResponse(ListCouponeResponse response) {
                        Logger.d(TAG, "#getListCouponeResponse -> onResponse ");
                        if (listener != null) {
                            listener.onResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#getListCouponeResponse -> onErrorResponse");
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> params = new HashMap<>();
                params.put("last_coupon_id",lastCouponId);
                params.put("limit", limit);
                params.put("is_used", isUsed);
                return params;
            }
        };
    }
    static GsonRequest<ResponseData> useCoupon(final String token, final int couponId,final int isUseAgain,
                                               final Response.Listener<ResponseData> listener,
                                               final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        return new GsonJsonRequest<ResponseData>(Request.Method.POST,
                API_USE_COUPON,
                ResponseData.class,
                headers,
                Constants.TIME_OUT_CUSTOM,
                new Response.Listener<ResponseData>() {
                    @Override
                    public void onResponse(ResponseData response) {
                        Logger.d(TAG, "#useCouponeResponse -> onResponse ");
                        if (listener != null) {
                            listener.onResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#useCouponeResponse -> onErrorResponse");
                        if (errorListener != null) {
                            errorListener.onErrorResponse(error);
                        }
                    }
                }) {
            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> params = new HashMap<>();
                params.put("coupon_id",couponId);
                params.put("is_use_again",isUseAgain);
                return params;
            }
        };
    }


}
