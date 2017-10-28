package jp.co.wssj.iungo.model.pushnotification;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.BaseModel;
import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.ErrorResponse;
import jp.co.wssj.iungo.model.ResponseData;
import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Utils;
import jp.co.wssj.iungo.utils.VolleySequence;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationModel extends BaseModel {

    public interface IGetListPushNotificationCallback {

        void onGetListPushNotificationSuccess(List<NotificationMessage> list, int page, int totalPage);

        void onGetListPushNotificationFailure(ErrorMessage errorMessage);
    }

    public interface ISetListPushNotificationCallback {

        void onSetListPushNotificationSuccess();

        void onSetListPushNotificationFailure();
    }

    public interface IGetListPushNotificationUnReadCallback {

        void onGetListPushNotificationUnReadSuccess(List<NotificationMessage> list, int page, int totalPage, int totalPushUnread);

        void onGetListPushNotificationUnReadFailure(String message);
    }

    public interface IUpdateActionPushCallback {

        void onUpdateActionPushSuccess();

        void onUpdateActionPushFailure();
    }

    public interface IGetContentPushCallback {

        void onGetContentPushSuccess(ContentPushResponse.ContentPushData contentPushResponse);

        void onGetContentPushFailure(String message);
    }

//    public interface IGetListPushForServiceCompanyCallback {
//
//        void onGetListPushNotificationSuccess(List<NotificationMessage> list, int page, int totalPage);
//
//        void onGetListPushNotificationFailure(ErrorMessage errorMessage);
//    }

    public interface IGetQuestionNaireCallback {

        void onGetQuestionNaireSuccess(QuestionNaireResponse response);

        void onGetQuestionNaireFailure(String message);
    }

    public PushNotificationModel(Context context) {
        super(context);
    }

    public void getListPushNotification(String token, long userPushId, int isSearch, String keySearch, int serviceCompanyId, int typePush, final IGetListPushNotificationCallback callback) {
        Request request = APICreator.getListNotification(token, userPushId, isSearch, keySearch, serviceCompanyId, typePush, new Response.Listener<ListNotificationResponse>() {

            @Override
            public void onResponse(final ListNotificationResponse response) {
                if (response.isSuccess()) {
                    if (response.getData() != null && response.getData().getListPushNotification() != null) {
                        final List<NotificationMessage> listNotification = response.getData().getListPushNotification();
                        callback.onGetListPushNotificationSuccess(listNotification, response.getData().getPage(), response.getData().getTotalPage());
                    } else {
                        callback.onGetListPushNotificationSuccess(new ArrayList<NotificationMessage>(), 0, 0);
                    }
                } else {
                    callback.onGetListPushNotificationFailure(new ErrorMessage(response.getMessage()));
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onGetListPushNotificationFailure(new ErrorMessage(getStringResource(R.string.network_error)));
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySequence.getInstance().addRequest(request);
    }

//    public void getListPushQuestionNaire(String token, long userPushId, int isSearch, String keySearch, final IGetListPushNotificationCallback callback) {
//        Request request = APICreator.getListPushQuestionNaire(token, userPushId, isSearch, keySearch, new Response.Listener<ListNotificationResponse>() {
//
//            @Override
//            public void onResponse(final ListNotificationResponse response) {
//                if (response.isSuccess()) {
//                    if (response.getData() != null && response.getData().getListPushNotification() != null) {
//                        final List<NotificationMessage> pushQuestionNaireList = response.getData().getListPushNotification();
//                        callback.onGetListPushNotificationSuccess(pushQuestionNaireList, response.getData().getPage(), response.getData().getTotalPage());
//                    } else {
//                        callback.onGetListPushNotificationSuccess(new ArrayList<NotificationMessage>(), 0, 0);
//                    }
//                } else {
//                    callback.onGetListPushNotificationFailure(new ErrorMessage(response.getMessage()));
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                callback.onGetListPushNotificationFailure(new ErrorMessage(getStringResource(R.string.network_error)));
//            }
//        });
//        request.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        VolleySequence.getInstance().addRequest(request);
//    }
//    public void getListPushNotificationForStoreAnnounce(String token, int serviceCompanyId, long lastUserPushId, final int isSearch, final String keySearch, final IGetListPushForServiceCompanyCallback callback) {
//        Request request = APICreator.getListNotificationForStoreAnnounce(token, serviceCompanyId, lastUserPushId, isSearch, keySearch, new Response.Listener<ListNotificationResponse>() {
//
//            @Override
//            public void onResponse(ListNotificationResponse response) {
//                if (response.isSuccess()) {
//                    if (response.getData() != null && response.getData().getListPushNotification() != null) {
//                        List<NotificationMessage> listNotification = response.getData().getListPushNotification();
//                        callback.onGetListPushNotificationSuccess(listNotification, response.getData().getPage(), response.getData().getTotalPage());
//                    } else {
//                        callback.onGetListPushNotificationSuccess(new ArrayList<NotificationMessage>(), 0, 0);
//                    }
//                } else {
//                    callback.onGetListPushNotificationFailure(new ErrorMessage(response.getMessage()));
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                callback.onGetListPushNotificationFailure(new ErrorMessage(getStringResource(R.string.network_error)));
//            }
//        });
//        VolleySequence.getInstance().addRequest(request);
//    }

    public void getListPushNotificationUnRead(String token, int page, int limit, final IGetListPushNotificationUnReadCallback callback) {
        final Request request = APICreator.getListPushUnRead(token, page, limit, new Response.Listener<ListNotificationResponse>() {

            @Override
            public void onResponse(ListNotificationResponse response) {
                if (response.isSuccess()) {
                    if (response.getData() != null && response.getData().getListPushNotification() != null) {
                        List<NotificationMessage> listNotification = response.getData().getListPushNotification();
                        callback.onGetListPushNotificationUnReadSuccess(listNotification, response.getData().getPage(), response.getData().getTotalPage(), response.getData().getUnreadPush());
                    } else {
                        callback.onGetListPushNotificationUnReadSuccess(new ArrayList<NotificationMessage>(), 0, 0, 0);
                    }
                } else {
                    callback.onGetListPushNotificationUnReadFailure(response.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onGetListPushNotificationUnReadFailure(getStringResource(R.string.network_error));
            }
        });
        VolleySequence.getInstance().addRequest(request);
    }

    public void setListPushUnRead(String token, List<Long> pushId, int type, final ISetListPushNotificationCallback callback) {
        Request request = APICreator.setListNotificationUnRead(token, pushId, type, new Response.Listener<ResponseData>() {

            @Override
            public void onResponse(ResponseData response) {
                callback.onSetListPushNotificationSuccess();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onSetListPushNotificationFailure();
            }
        });
        VolleySequence.getInstance().addRequest(request);
    }

    public void updateActionPush(String token, long pushId, final IUpdateActionPushCallback callback) {
        Request request = APICreator.updateActionPush(token, pushId, new Response.Listener<ResponseData>() {

            @Override
            public void onResponse(ResponseData response) {
                callback.onUpdateActionPushSuccess();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onUpdateActionPushFailure();
            }
        });
        VolleySequence.getInstance().addRequest(request);
    }

    public void getContentPush(String token, long pushId, final IGetContentPushCallback callback) {
        Request request = APICreator.getContentPush(token, pushId, new Response.Listener<ContentPushResponse>() {

            @Override
            public void onResponse(ContentPushResponse response) {
                if (response.isSuccess()) {
                    callback.onGetContentPushSuccess(response.getData());
                } else {
                    callback.onGetContentPushFailure(response.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                if (errorResponse != null) {
                    callback.onGetContentPushFailure(errorResponse.getMessage());
                } else {
                    callback.onGetContentPushFailure(getStringResource(R.string.failure));
                }
            }
        });
        VolleySequence.getInstance().addRequest(request);
    }

    public void getQuestionNaire(String token, long pushId, final IGetQuestionNaireCallback callback) {
        Request request = APICreator.getQuestionNaire(token, pushId, new Response.Listener<QuestionNaireResponse>() {

            @Override
            public void onResponse(QuestionNaireResponse response) {
                if (response.isSuccess()) {
                    callback.onGetQuestionNaireSuccess(response);
                } else {
                    callback.onGetQuestionNaireFailure(Constants.EMPTY_STRING);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onGetQuestionNaireFailure(Constants.EMPTY_STRING);
            }
        });
        VolleySequence.getInstance().addRequest(request);
    }

}
