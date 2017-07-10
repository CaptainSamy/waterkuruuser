package wssj.co.jp.point.model.memo;

import android.content.Context;
import android.text.TextUtils;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.io.File;

import wssj.co.jp.point.R;
import wssj.co.jp.point.model.BaseModel;
import wssj.co.jp.point.model.ErrorResponse;
import wssj.co.jp.point.model.ResponseData;
import wssj.co.jp.point.model.entities.StatusMemoData;
import wssj.co.jp.point.utils.AmazonS3Utils;
import wssj.co.jp.point.utils.Logger;
import wssj.co.jp.point.utils.Utils;
import wssj.co.jp.point.utils.VolleySequence;

/**
 * Created by Nguyen Huu Ta on 19/5/2017.
 */

public class UserMemoModel extends BaseModel {

    private static String TAG = "UserMemoModel";

    public UserMemoModel(Context context) {
        super(context);
    }

    public interface IGetMemoConfigCallback {

        void onGetMemoConfigSuccess(MemoDynamicResponse.ServiceListData data);

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

        void onUpdateUserMemoSuccess(StatusMemoData[] listStatusImage);

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

    public void getUserMemo(String token, int serviceId, final IUserMemoCallback callback) {
        Request requestNote = APICreator.getUserMemoRequest(token, serviceId,
                new Response.Listener<UserMemoResponse>() {

                    @Override
                    public void onResponse(UserMemoResponse response) {
                        callback.onGetUserMemoSuccess(response.getData());
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

    public void uploadImageAWS(final StatusMemoData[] listStatusImage, final int position, final IUpdateAWSCallback callback) {
        if (position < listStatusImage.length) {
            if (listStatusImage[position].getStatus() != 1) {
                final String path = listStatusImage[position].getPathNewImage();
                if (TextUtils.isEmpty(path)) {
                    listStatusImage[position].setIsUploadAWSSuccess(true);
                    uploadImageAWS(listStatusImage, position + 1, callback);
                } else {
                    final String fileName = Utils.getFileName(path);
                    File file = new File(path);
                    AmazonS3Utils.getInstance().upload(file, fileName, new TransferListener() {

                        @Override
                        public void onStateChanged(int id, TransferState state) {
                            if (state == TransferState.COMPLETED) {
                                listStatusImage[position].setIsUploadAWSSuccess(true);
                                listStatusImage[position].setPathNewImage(AmazonS3Utils.BASE_IMAGE_URL + fileName);
                                uploadImageAWS(listStatusImage, position + 1, callback);
                            }
                        }

                        @Override
                        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                            Logger.d(TAG, "#onProgressChanged " + bytesCurrent + "/" + bytesTotal);
                        }

                        @Override
                        public void onError(int id, Exception ex) {
                            Logger.d(TAG, "#onError " + ex);
                            listStatusImage[position].setIsUploadAWSSuccess(false);
                            uploadImageAWS(listStatusImage, position + 1, callback);
                        }
                    });
                }
            } else {
                listStatusImage[position].setIsUploadAWSSuccess(true);
                uploadImageAWS(listStatusImage, position + 1, callback);
            }
        } else {
            callback.onUpdateUserMemoSuccess(listStatusImage);
        }

    }

    public void updateUserMemo(String token, int serviceId, String note, StatusMemoData[] listStatusImage, final IUpdateUserMemoCallback callback) {
        Request requestUpdateMemo = APICreator.updateUserMemo(token, serviceId, note, listStatusImage,
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
        String data = "{  \"result\":\"success\", \n" +
                "  \"data\" :{\n" +
                "    \"service_list\":[       \n" +
                "        {\"service_id\":1,      \n" +
                "        \"service_name\":\"Ten service\",    \n" +
                "        \"service_memo_config\":[         \n" +
                "          { \"type\":\"textbox\",\n" +
                "            \"id\":\"title\",\n" +
                "            \"name\":\"title\",\n" +
                "            \"config\":{\n" +
                "              \"multiline\":0,\n" +
                "              \"max_lengt\":2048\n" +
                "              }},\n" +
                "              {\n" +
                "                \"type\":\"textbox\",\n" +
                "                \"id\":\"content\",\n" +
                "                \"name\":\"content\",\n" +
                "                \"config\":{ \n" +
                "                  \"multiline\":0,\n" +
                "                  \"max_lengt\":2048 \n" +
                "                  }\n" +
                "                \n" +
                "              },\n" +
                "              {\n" +
                "                \"type\":\"image\",\n" +
                "                \"id\":\"image\",\n" +
                "                \"name\":\"image\",\n" +
                "                \"config\":{\n" +
                "                  \"numb_image\":4\n" +
                "                  }\n" +
                "              },\n" +
                "             {\n" +
                "                    \"type\":\"radio\", \n" +
                "                    \"id\":\"radio\", \n" +
                "                    \"name\":\"radio\",\n" +
                "                    \"config\":{\n" +
                "                      \"data\":[ \n" +
                "                        \"yes\",\n" +
                "                        \"no\",\n" +
                "                        \"ok\" \n" +
                "                      ] \n" +
                "                    } \n" +
                "              },\n" +
                "                {\n" +
                "                    \"type\":\"radio\", \n" +
                "                    \"id\":\"radio1\", \n" +
                "                    \"name\":\"radio1\",\n" +
                "                    \"config\":{\n" +
                "                      \"data\":[ \n" +
                "                        \"I\",\n" +
                "                        \"am\",\n" +
                "                        \"Dai\",\n" +
                "                        \"Ky\",\n" +
                "                        \"Sy\" \n" +
                "                      ] \n" +
                "                    } \n" +
                "              },\n" +
                "                {\n" +
                "                          \"type\":\"droplist\", \n" +
                "                          \"id\":\"droplist\",\n" +
                "                          \"name\":\"droplist\",\n" +
                "                          \"config\" :{\n" +
                "                            \"data\":[\n" +
                "                              \"Option 1\",\n" +
                "                              \"Option 2\",\n" +
                "                              \"Option 3\"\n" +
                "                              ]\n" +
                "                          }  \n" +
                "                        }\n" +
                "                  ] \n" +
                "              } \n" +
                "            ]  \n" +
                "  }   \n" +
                "  \n" +
                "}";
        Gson gson = new Gson();
        MemoDynamicResponse response = gson.fromJson(data, MemoDynamicResponse.class);
        callback.onGetMemoConfigSuccess(response.getData());

//        Request requestNote = APICreator.getUserMemoConfigRequest(token, serviceId,
//                new Response.Listener<MemoDynamicResponse>() {
//
//                    @Override
//                    public void onResponse(MemoDynamicResponse response) {
//                        if (response.isSuccess()) {
//                            callback.onGetMemoConfigSuccess(response.getData());
//                        } else {
//                            callback.onGetMemoConfigFailure(response.getMessage());
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        ErrorResponse errorResponse = Utils.parseErrorResponse(error);
//                        if (errorResponse != null) {
//                            callback.onGetMemoConfigFailure(errorResponse.getMessage());
//                        } else {
//                            callback.onGetMemoConfigFailure(getStringResource(R.string.failure));
//                        }
//                    }
//                });
//        VolleySequence.getInstance().addRequest(requestNote);
    }
}
