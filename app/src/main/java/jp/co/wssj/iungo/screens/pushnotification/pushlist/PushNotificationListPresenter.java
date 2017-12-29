package jp.co.wssj.iungo.screens.pushnotification.pushlist;

import android.text.TextUtils;

import java.util.List;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.model.pushnotification.PushNotificationModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationListPresenter extends FragmentPresenter<IPushNotificationListView> {

    private static final String TAG = "PushNotificationListPresenter";

    protected PushNotificationListPresenter(IPushNotificationListView view) {
        super(view);
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new PushNotificationModel(view.getViewContext()));
    }

    public void getListPushNotification(long userPushId, int isSearch, String keySearch) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(PushNotificationModel.class).getListPushNotification(token, userPushId, isSearch, keySearch, 0, Constants.TypePush.TYPE_ALL_PUSH,
                new PushNotificationModel.IGetListPushNotificationCallback() {

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
