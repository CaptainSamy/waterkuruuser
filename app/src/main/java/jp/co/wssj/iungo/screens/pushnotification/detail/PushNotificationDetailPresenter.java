package jp.co.wssj.iungo.screens.pushnotification.detail;

import android.text.TextUtils;

import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.model.pushnotification.PushNotificationModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;
import jp.co.wssj.iungo.utils.Logger;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationDetailPresenter extends FragmentPresenter<IPushNotificationDetailView> {

    private static final String TAG = "PushNotificationDetailPresenter";

    protected PushNotificationDetailPresenter(IPushNotificationDetailView view) {
        super(view);
        registerModel(new PushNotificationModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
    }

    public void setListPushUnRead(long pushId) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        if (!TextUtils.isEmpty(token)) {
            getModel(PushNotificationModel.class).setListPushUnRead(token, pushId, new PushNotificationModel.ISetListPushNotificationCallback() {

                @Override
                public void onSetListPushNotificationSuccess() {
                    Logger.d(TAG, "onSetListPushNotificationSuccess");
                }

                @Override
                public void onSetListPushNotificationFailure() {
                    Logger.d(TAG, "onSetListPushNotificationFailure");
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
