package wssj.co.jp.olioa.screens.pushnotificationforstore;

import android.text.TextUtils;

import java.util.List;

import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.model.firebase.NotificationMessage;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.model.pushnotification.PushNotificationModel;
import wssj.co.jp.olioa.screens.base.FragmentPresenter;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.utils.Logger;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationForStoreAnnouncePresenter extends FragmentPresenter<IPushNotificationForStoreAnnounce> {

    private static final String TAG = "PushNotificationForStoreAnnouncePresenter";

    protected PushNotificationForStoreAnnouncePresenter(IPushNotificationForStoreAnnounce view) {
        super(view);
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new PushNotificationModel(view.getViewContext()));
    }

    public void getListPushNotificationForStoreAnnounce(int serviceCompanyId, long lastUserPushId, final int isSearch, final String keySearch) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(PushNotificationModel.class).getListPushNotification(token, lastUserPushId, isSearch, keySearch, serviceCompanyId, Constants.TypePush.TYPE_PUSH_ANNOUNCE,Constants.EMPTY_STRING,Constants.EMPTY_STRING,new PushNotificationModel.IGetListPushNotificationCallback() {

            @Override
            public void onGetListPushNotificationSuccess(List<NotificationMessage> list, int page, int totalPage) {
                getView().showListPushNotification(list, page, totalPage);
            }

            @Override
            public void onGetListPushNotificationFailure(ErrorMessage errorMessage) {
                getView().displayErrorMessage(errorMessage);
            }
        });
    }

    public void setListPushUnRead(List<Long> pushId, int type) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        if (!TextUtils.isEmpty(token)) {
            getModel(PushNotificationModel.class).setListPushUnRead(token, pushId, type, new PushNotificationModel.ISetListPushNotificationCallback() {

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
}
