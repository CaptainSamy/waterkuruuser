package wssj.co.jp.point.model.checkin;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import wssj.co.jp.point.R;
import wssj.co.jp.point.model.BaseModel;
import wssj.co.jp.point.model.ErrorMessage;
import wssj.co.jp.point.model.ErrorResponse;
import wssj.co.jp.point.utils.Constants;
import wssj.co.jp.point.utils.Logger;
import wssj.co.jp.point.utils.Utils;
import wssj.co.jp.point.utils.VolleySequence;

/**
 * Created by Nguyen Huu Ta on 18/5/2017.
 */

public class CheckInModel extends BaseModel {

    public interface IFeedbackQRStoreCallback {

        void onFeedbackSuccess(ConfirmCheckInResponse.SessionData data);

        void onFeedbackFailure(ErrorMessage errorMessage);
    }

    public interface IGetCheckInStatusCallback {

        void onCheckInStatusSuccess(CheckInStatusResponse.CheckInStatusData data);

        void onCheckInStatusFailure(ErrorMessage errorMessage);
    }

    public interface IVerifyStoreQRCodeCallback {

        void onVerified();
    }

    public interface IGetInfoStoreCallback {

        void onSuccess(InfoStoreResponse.InfoStoreData data);

        void onFailure(String message);
    }

    public static final String TAG = "CheckInModel";

    public CheckInModel(Context context) {
        super(context);
    }

    public void getInfoStoreByCode(String qrCode, final IGetInfoStoreCallback callback) {
        Request request = APICreator.getInfoStoreRequest(qrCode, new Response.Listener<InfoStoreResponse>() {

            @Override
            public void onResponse(InfoStoreResponse response) {
                Logger.i(TAG, "#getInfoStoreByCode => onResponse");
                if (response.isSuccess()) {
                    callback.onSuccess(response.getData());
                } else {
                    callback.onFailure(response.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.i(TAG, "#getInfoStoreByCode => onErrorResponse");
                ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                if (errorResponse != null) {
                    callback.onFailure(errorResponse.getMessage());
                } else {
                    callback.onFailure(getStringResource(R.string.message_read_qr_code_failure));
                }
            }
        });
        VolleySequence.getInstance().addRequest(request);
    }

    public void feedbackQRStore(String code, String token, final IFeedbackQRStoreCallback callback) {
        Request requestCheckIn = APICreator.getFeedbackQRStoreRequest(token, code,
                new Response.Listener<ConfirmCheckInResponse>() {

                    @Override
                    public void onResponse(ConfirmCheckInResponse response) {
                        if (response.isSuccess()) {
                            ConfirmCheckInResponse.SessionData sessionData = response.getData();
                            callback.onFeedbackSuccess(sessionData);
                        } else {
                            callback.onFeedbackFailure(new ErrorMessage(response.getMessage()));
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                        if (errorResponse != null) {
                            callback.onFeedbackFailure(new ErrorMessage(errorResponse.getMessage()));
                        } else {
                            callback.onFeedbackFailure(new ErrorMessage(getStringResource(R.string.network_error)));
                        }
                    }
                });
        VolleySequence.getInstance().addRequest(requestCheckIn);
    }

    public void getCheckInStatus(String token, final IGetCheckInStatusCallback callback) {
        Request requestCheckInStatus = APICreator.getCheckInStatusByUser(token,
                new Response.Listener<CheckInStatusResponse>() {

                    @Override
                    public void onResponse(CheckInStatusResponse response) {
                        Logger.i(TAG, "#getCheckInStatusByUser => onResponse message");
                        callback.onCheckInStatusSuccess(response.getData());

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.i(TAG, "#getCheckInStatusByUser => onErrorResponse");
                        callback.onCheckInStatusFailure(new ErrorMessage(getStringResource(R.string.failure)));
                    }
                });
        VolleySequence.getInstance().addRequest(requestCheckInStatus);
    }

    public void verifyStoreQRCode(String qrCode, IVerifyStoreQRCodeCallback callback) {
        if (qrCode != null && qrCode.startsWith(Constants.PREFIX_QR_STORE)) {
            callback.onVerified();
        }
    }
}
