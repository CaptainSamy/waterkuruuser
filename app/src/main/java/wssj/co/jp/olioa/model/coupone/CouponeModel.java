package wssj.co.jp.olioa.model.coupone;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.BaseModel;
import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.model.ResponseData;
import wssj.co.jp.olioa.utils.VolleySequence;

/**
 * Created by thang on 1/2/2018.
 */

public class CouponeModel extends BaseModel {

    public CouponeModel(Context context) {
        super(context);
    }

    public interface IGetListCouponeResponseCallback {

        void onGetListCouponeResponseSuccess(List<Coupone> list);

        void onGetListCouponeResponseFailure(ErrorMessage errorMessage);
    }

    public void getListCoupone(String token, final int lastCouponId, final int limit, final int isUsed, final CouponeModel.IGetListCouponeResponseCallback callback) {
        Request request = APICreator.getListCoupon(token, lastCouponId, limit, isUsed, new Response.Listener<ListCouponeResponse>() {

            @Override
            public void onResponse(final ListCouponeResponse response) {
                if (response.isSuccess()) {
                    if (response.getData() != null && response.getData().getmList() != null) {
                        final List<Coupone> listCouponen = response.getData().getmList();
                        callback.onGetListCouponeResponseSuccess(listCouponen);
                    } else {
                        callback.onGetListCouponeResponseSuccess(null);
                    }
                } else {
                    callback.onGetListCouponeResponseFailure(new ErrorMessage(response.getMessage()));
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onGetListCouponeResponseFailure(new ErrorMessage(getStringResource(R.string.network_error)));
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySequence.getInstance().addRequest(request);
    }

    public interface IUseCouponeResponseCallback {

        void onUseResponseSuccess(Boolean kq);

        void onUseResponseFailure(ErrorMessage errorMessage);

    }
    public void useCoupone(String token, final int couponId, final int isUseAgain, final CouponeModel.IUseCouponeResponseCallback callback) {
        final Request request = APICreator.useCoupon(token, couponId, isUseAgain,
                new Response.Listener<ResponseData>() {

                    @Override
                    public void onResponse(final ResponseData response) {
                        if (response.isSuccess()) {
                            Boolean kq = response.isSuccess();
//                    Integer isUseAgain=response.hashCode()
                            callback.onUseResponseSuccess(kq);
                        } else {
                            //// TODO: 1/8/2018
                            callback.onUseResponseFailure(new ErrorMessage(getStringResource(R.string.network_error)));
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onUseResponseFailure(new ErrorMessage(getStringResource(R.string.network_error)));
                    }
                });
        request.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySequence.getInstance().addRequest(request);
    }
}
