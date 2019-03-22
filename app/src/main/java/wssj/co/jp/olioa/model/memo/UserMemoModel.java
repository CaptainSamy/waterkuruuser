package wssj.co.jp.olioa.model.memo;

import android.content.Context;
import android.text.TextUtils;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.File;
import java.util.List;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.BaseModel;
import wssj.co.jp.olioa.model.ErrorResponse;
import wssj.co.jp.olioa.model.ResponseData;
import wssj.co.jp.olioa.utils.AmazonS3Utils;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.utils.Logger;
import wssj.co.jp.olioa.utils.Utils;
import wssj.co.jp.olioa.utils.VolleySequence;

/**
 * Created by Nguyen Huu Ta on 19/5/2017.
 */

public class UserMemoModel extends BaseModel {

    private static String TAG = "UserMemoModel";

    public UserMemoModel(Context context) {
        super(context);
    }

    public interface IGetMemoConfigCallback {

        void onGetMemoConfigSuccess(MemoDynamicResponse.UserMemoData data);

        void onGetMemoConfigFailure(String message);
    }

    public interface IGetListServiceCallback {

        void onGetListServiceSuccess(ListServiceResponse.ServiceData data);

        void onGetListServiceFailure(String message);
    }

    public interface IUserMemoCallback {

        void onGetUserMemoSuccess(UserMemoResponse.UserMemoData data);

        void onGetUserMemoFailure(String message);
    }

    public interface IUpdateAWSCallback {

        void onUpdateUserMemoSuccess(List<MemoDynamicResponse.UserMemoData.UserMemoValue> memoValues);

    }

    public interface IUpdateUserMemoCallback {

        void onUpdateUserMemoSuccess(String message);

        void onUpdateUserMemoFailure(String message);
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

    public void uploadImageAWS(final List<MemoDynamicResponse.UserMemoData.UserMemoValue> memoValues, final int position, List<MemoDynamicResponse.UserMemoData.UserMemoValue.Value.Image> listImage, final IUpdateAWSCallback callback) {

        if (listImage == null) {
            for (MemoDynamicResponse.UserMemoData.UserMemoValue userMemoValue : memoValues) {
                if (userMemoValue.getType() == Constants.MemoConfig.TYPE_IMAGE) {
                    listImage = userMemoValue.getValue().getListImage();
                    break;
                }
            }
        }
        final List<MemoDynamicResponse.UserMemoData.UserMemoValue.Value.Image> images = listImage;
        if (position < listImage.size()) {
            String path = listImage.get(position).getUrlImage();
            if (!TextUtils.isEmpty(path)) {
                final String fileName = Utils.getFileName(path);
                File file = new File(path);
                if (file.exists()) {
                    AmazonS3Utils.getInstance().upload(file, fileName, new TransferListener() {

                        @Override
                        public void onStateChanged(int id, TransferState state) {
                            if (state == TransferState.COMPLETED) {
                                images.get(position).setUrlImage(AmazonS3Utils.BASE_IMAGE_URL + fileName);
                                uploadImageAWS(memoValues, position + 1, images, callback);
                            }
                        }

                        @Override
                        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                            Logger.d(TAG, "#onProgressChanged " + bytesCurrent + "/" + bytesTotal);
                        }

                        @Override
                        public void onError(int id, Exception ex) {
                            Logger.d(TAG, "#onError " + ex);
                            images.get(position).setUrlImage(Constants.EMPTY_STRING);
                            uploadImageAWS(memoValues, position + 1, images, callback);
                        }
                    });
                } else {
                    uploadImageAWS(memoValues, position + 1, images, callback);
                }
            } else {
                uploadImageAWS(memoValues, position + 1, images, callback);
            }
        } else {
            callback.onUpdateUserMemoSuccess(memoValues);
        }

    }

    public void updateUserMemo(String token, int serviceId, List<MemoDynamicResponse.UserMemoData.UserMemoValue> memoValues, final IUpdateUserMemoCallback callback) {
        Request requestUpdateMemo = APICreator.updateUserMemo(token, serviceId, memoValues,
                new Response.Listener<ResponseData>() {

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

    public void getUserMemoConfig(String token, int serviceId, final IGetMemoConfigCallback callback) {
        Request requestNote = APICreator.getUserMemoConfigRequest(token, serviceId,
                new Response.Listener<MemoDynamicResponse>() {

                    @Override
                    public void onResponse(MemoDynamicResponse response) {
                        if (response.isSuccess()) {
                            callback.onGetMemoConfigSuccess(response.getData());
                        } else {
                            callback.onGetMemoConfigFailure(response.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                        if (errorResponse != null) {
                            callback.onGetMemoConfigFailure(errorResponse.getMessage());
                        } else {
                            callback.onGetMemoConfigFailure(getStringResource(R.string.failure));
                        }
                    }
                });
        VolleySequence.getInstance().addRequest(requestNote);
    }
}
