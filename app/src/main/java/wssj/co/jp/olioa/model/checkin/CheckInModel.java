package wssj.co.jp.olioa.model.checkin;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.BaseModel;
import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.model.ErrorResponse;
import wssj.co.jp.olioa.model.ResponseData;
import wssj.co.jp.olioa.screens.pushobject.MappingUserStoreResponse;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.utils.Logger;
import wssj.co.jp.olioa.utils.Utils;
import wssj.co.jp.olioa.utils.VolleySequence;

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

    public interface IMappingUserStoreCallback {

        void onMappingUserStoreSuccess(MappingUserStoreResponse.PushData data);

        void onMappingUserStoreFailure(String message);
    }

    public interface IMappingUserStoreFastCallback {

        void onMappingUserStoreFastSuccess();

        void onMappingUserStoreFastFailure(String message);
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
                        ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                        if (errorResponse != null) {
                            callback.onCheckInStatusFailure(new ErrorMessage(errorResponse.getMessage()));
                        } else {
                            callback.onCheckInStatusFailure(new ErrorMessage(getStringResource(R.string.network_error)));
                        }
                    }
                });
        VolleySequence.getInstance().addRequest(requestCheckInStatus);
    }

    public void mappingUserWithStore(String token, String code, final IMappingUserStoreCallback callback) {
        Request requestCheckInStatus = APICreator.mappingUserWithStore(token, code,
                new Response.Listener<MappingUserStoreResponse>() {

                    @Override
                    public void onResponse(MappingUserStoreResponse response) {
                        Logger.i(TAG, "#mappingUserWithStore => onResponse message");
                        if (response != null && response.isSuccess()) {
                            callback.onMappingUserStoreSuccess(response.getData());
                        } else {
                            callback.onMappingUserStoreFailure(getStringResource(R.string.failure));
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.i(TAG, "#mappingUserWithStore => onErrorResponse");
                        ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                        if (errorResponse != null) {
                            callback.onMappingUserStoreFailure(errorResponse.getMessage());
                        } else {
                            callback.onMappingUserStoreFailure(getStringResource(R.string.network_error));
                        }
                    }
                });
        VolleySequence.getInstance().addRequest(requestCheckInStatus);
    }

    public void mappingUserWithStoreFast(String token, String code, final IMappingUserStoreFastCallback callback) {
        Request requestCheckInStatus = APICreator.mappingUserWithStoreFast(token, code,
                new Response.Listener<ResponseData>() {

                    @Override
                    public void onResponse(ResponseData response) {
                        Logger.i(TAG, "#mappingUserWithStoreFast => onResponse message");
                        if (response != null && response.isSuccess()) {
                            callback.onMappingUserStoreFastSuccess();
                        } else {
                            callback.onMappingUserStoreFastFailure(getStringResource(R.string.failure));
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.i(TAG, "#mappingUserWithStoreFast => onErrorResponse");
                        ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                        if (errorResponse != null) {
                            callback.onMappingUserStoreFastFailure(errorResponse.getMessage());
                        } else {
                            callback.onMappingUserStoreFastFailure(getStringResource(R.string.network_error));
                        }
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
