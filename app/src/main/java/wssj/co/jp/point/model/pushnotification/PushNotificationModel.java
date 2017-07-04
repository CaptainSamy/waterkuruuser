package wssj.co.jp.point.model.pushnotification;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

        void onGetListPushNotificationSuccess(List<NotificationMessage> list, int page, int totalPage, int numberPushUnreadThisPage, int totalPushUnread);

        void onGetListPushNotificationFailure(ErrorMessage errorMessage);
    }

    public interface ISetListPushNotificationCallback {

        void onSetListPushNotificationSuccess();

        void onSetListPushNotificationFailure();
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
                        int numberNotificationUnReadThisPage = countNotificationNotRead(listNotification);
                        sort(listNotification);
                        callback.onGetListPushNotificationSuccess(listNotification, response.getData().getPage(), response.getData().getTotalPage(), numberNotificationUnReadThisPage, response.getData().getUnreadPush());
                    } else {
                        callback.onGetListPushNotificationSuccess(new ArrayList<NotificationMessage>(), 0, 0, 0, 0);
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

    public void setListPushUnRead(String token, List<NotificationMessage> listNotification, final ISetListPushNotificationCallback callback) {
        List<Integer> listIdPush = new ArrayList<>();

        for (NotificationMessage message : listNotification) {
            if (message.getStatusRead() == 0) {
                listIdPush.add(message.getPushId());
            }

        }
        if (listIdPush.size() > 0) {
            Request request = APICreator.setListNotificationUnRead(token, listIdPush, new Response.Listener<ResponseData>() {

                @Override
                public void onResponse(ResponseData response) {
                    callback.onSetListPushNotificationSuccess();
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onSetListPushNotificationSuccess();
                }
            });
            VolleySequence.getInstance().addRequest(request);
        }
    }

    private int countNotificationNotRead(List<NotificationMessage> list) {
        if (list == null) return 0;
        int count = 0;
        for (NotificationMessage message : list) {
            if (message.getStatusRead() == 0) {
                count++;
            }
        }
        return count;
    }

    public void sort(List<NotificationMessage> list) {
        Comparator<NotificationMessage> comparator = new Comparator<NotificationMessage>() {

            @Override
            public int compare(NotificationMessage left, NotificationMessage right) {
                if (left.getPushTime() > right.getPushTime()) {
                    return -1;
                } else if (left.getPushTime() < right.getPushTime()) {
                    return 1;
                }
                return 0;
            }
        };
        Collections.sort(list, comparator);
    }

}
