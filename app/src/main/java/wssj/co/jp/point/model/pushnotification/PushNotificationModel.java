package wssj.co.jp.point.model.pushnotification;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import wssj.co.jp.point.R;
import wssj.co.jp.point.model.BaseModel;
import wssj.co.jp.point.model.ErrorMessage;
import wssj.co.jp.point.model.ResponseData;
import wssj.co.jp.point.model.firebase.NotificationMessage;
import wssj.co.jp.point.utils.VolleySequence;

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

    public PushNotificationModel(Context context) {
        super(context);
    }

    public void getListPushNotification(String token, int page, int limit, final IGetListPushNotificationCallback callback) {
        Request request = APICreator.getUploadDeviceTokenRequest(token, page, limit, new Response.Listener<ListNotificationResponse>() {

            @Override
            public void onResponse(ListNotificationResponse response) {
                if (response.isSuccess()) {
                    if (response.getData() != null && response.getData().getListPushNotification() != null) {
                        List<NotificationMessage> listNotification = response.getData().getListPushNotification();
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
        VolleySequence.getInstance().addRequest(request);
    }

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

    public void setListPushUnRead(String token, long pushId, final ISetListPushNotificationCallback callback) {
        Request request = APICreator.setListNotificationUnRead(token, pushId, new Response.Listener<ResponseData>() {

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

}
