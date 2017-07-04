package wssj.co.jp.point.model.memo;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import wssj.co.jp.point.R;
import wssj.co.jp.point.model.BaseModel;
import wssj.co.jp.point.model.ErrorResponse;
import wssj.co.jp.point.model.ResponseData;
import wssj.co.jp.point.model.entities.UpdateMemoPhotoData;
import wssj.co.jp.point.utils.Utils;
import wssj.co.jp.point.utils.VolleySequence;

/**
 * Created by Nguyen Huu Ta on 19/5/2017.
 */

public class UserMemoModel extends BaseModel {

    public UserMemoModel(Context context) {
        super(context);
    }

    public interface IGetListCompanyCallback {

        void onGetListCompanySuccess(ListCompanyResponse.ServicesData data);

        void onGetListCompanyFailure(String message);
    }

    public interface IGetListServiceCallback {

        void onGetListServiceSuccess(ListServiceResponse.ServiceData data);

        void onGetListServiceFailure(String message);
    }

    public interface IUserMemoCallback {

        void onGetUserMemoSuccess(UserMemoResponse.UserMemoData data);

        void onGetUserMemoFailure(String message);
    }

    public interface IUpdateUserMemoCallback {

        void onUpdateUserMemoSuccess(String message);

        void onUpdateUserMemoFailure(String message);
    }

    public void getListCompany(String token, final IGetListCompanyCallback callback) {
        Request requestGetListServices = APICreator.getListCompany(token,
                new Response.Listener<ListCompanyResponse>() {

                    @Override
                    public void onResponse(ListCompanyResponse response) {
                        if (response.isSuccess()) {
                            callback.onGetListCompanySuccess(response.getData());
                        } else {
                            String message = TextUtils.isEmpty(response.getMessage()) ? getStringResource(R.string.failure) : response.getMessage();
                            callback.onGetListCompanyFailure(message);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                        if (errorResponse != null) {
                            callback.onGetListCompanyFailure(errorResponse.getMessage());
                        } else {
                            callback.onGetListCompanyFailure(getStringResource(R.string.failure));
                        }
                    }
                });
        VolleySequence.getInstance().addRequest(requestGetListServices);
    }

    public void getListService(String token, final IGetListServiceCallback callback) {
        Request requestGetListServices = APICreator.getListService(token,
                new Response.Listener<ListServiceResponse>() {

                    @Override
                    public void onResponse(ListServiceResponse response) {
                        if (response.isSuccess()) {
                            callback.onGetListServiceSuccess(response.getData());
                        } else {
                            String message = TextUtils.isEmpty(response.getMessage()) ? getStringResource(R.string.failure) : response.getMessage();
                            callback.onGetListServiceFailure(message);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                        if (errorResponse != null) {
                            callback.onGetListServiceFailure(errorResponse.getMessage());
                        } else {
                            callback.onGetListServiceFailure(getStringResource(R.string.failure));
                        }
                    }
                });
        VolleySequence.getInstance().addRequest(requestGetListServices);
    }

    public void getUserMemo(String token, int serviceId, final IUserMemoCallback callback) {
        Request requestNote = APICreator.getUserMemoRequest(token, serviceId,
                new Response.Listener<UserMemoResponse>() {

                    @Override
                    public void onResponse(UserMemoResponse response) {
                        callback.onGetUserMemoSuccess(response.getData());
//                        if (response.isSuccess() && response.getData() != null) {
//                            callback.onGetUserMemoSuccess(response.getData());
//                        } else {
//                            callback.onGetUserMemoSuccess(null);
//                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                        if (errorResponse != null) {
                            callback.onGetUserMemoFailure(errorResponse.getMessage());
                        } else {
                            callback.onGetUserMemoFailure(getStringResource(R.string.failure));
                        }
                    }
                });
        VolleySequence.getInstance().addRequest(requestNote);
    }

    public void updateUserMemo(String token, int serviceId, String note, UpdateMemoPhotoData[] images, final IUpdateUserMemoCallback callback) {
        Request requestUpdateMemo = APICreator.updateUserMemo(token, serviceId, note, images, new Response.Listener<ResponseData>() {

            @Override
            public void onResponse(ResponseData response) {
                if (response.isSuccess()) {
                    callback.onUpdateUserMemoSuccess(response.getMessage());
                } else {
                    String message = TextUtils.isEmpty(response.getMessage()) ? getStringResource(R.string.failure) : response.getMessage();
                    callback.onUpdateUserMemoFailure(message);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                if (errorResponse != null) {
                    callback.onUpdateUserMemoFailure(errorResponse.getMessage());
                } else {
                    callback.onUpdateUserMemoFailure(getStringResource(R.string.network_error));
                }
            }
        });
        VolleySequence.getInstance().addRequest(requestUpdateMemo);
    }
}
