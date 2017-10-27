package jp.co.wssj.iungo.screens.pushnotificationforstore;

import android.text.TextUtils;

import java.util.List;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.model.pushnotification.PushNotificationModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;
import jp.co.wssj.iungo.utils.Logger;

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
        getModel(PushNotificationModel.class).getListPushNotificationForStoreAnnounce(token, serviceCompanyId, lastUserPushId, isSearch, keySearch, new PushNotificationModel.IGetListPushForServiceCompanyCallback() {

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
