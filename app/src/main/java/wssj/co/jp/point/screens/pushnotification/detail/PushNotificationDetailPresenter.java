package wssj.co.jp.point.screens.pushnotification.detail;

import android.text.TextUtils;

import java.util.List;

import wssj.co.jp.point.model.firebase.NotificationMessage;
import wssj.co.jp.point.model.preference.SharedPreferencesModel;
import wssj.co.jp.point.model.pushnotification.PushNotificationModel;
import wssj.co.jp.point.screens.base.FragmentPresenter;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationDetailPresenter extends FragmentPresenter<IPushNotificationDetailView> {

    protected PushNotificationDetailPresenter(IPushNotificationDetailView view) {
        super(view);
        registerModel(new PushNotificationModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
    }

    public void setListPushUnRead(List<NotificationMessage> listPushNotification) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        if (!TextUtils.isEmpty(token)) {
            getModel(PushNotificationModel.class).setListPushUnRead(token, listPushNotification, new PushNotificationModel.ISetListPushNotificationCallback() {

                @Override
                public void onSetListPushNotificationSuccess(int numberNotificationUnRead) {

                }

                @Override
                public void onSetListPushNotificationFailure() {
                }
            });
        }
    }

    public void updateActionPush(long pushId) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        if (!TextUtils.isEmpty(token)) {
            getModel(PushNotificationModel.class).updateActionPush(token, pushId, new PushNotificationModel.IUpdateActionPushCallback() {

                @Override
                public void onUpdateActionPushSuccess() {

                }

                @Override
                public void onUpdateActionPushFailure() {

                }
            });
        }
    }
}
