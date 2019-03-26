package wssj.co.jp.olioa.screens.pushnotification.pushlist;

import android.text.TextUtils;

import java.util.List;

import wssj.co.jp.olioa.model.baseapi.APICallback;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.model.pushnotification.PushNotificationModel;
import wssj.co.jp.olioa.model.pushnotification.PushNotificationResponse;
import wssj.co.jp.olioa.screens.base.FragmentPresenter;
import wssj.co.jp.olioa.utils.Logger;

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

    public void getListPushNotification(int storeId, int page) {
        getView().showProgress();
        getModel(PushNotificationModel.class).getListPushNotification(storeId, page, new APICallback<PushNotificationResponse>() {

            @Override
            public void onSuccess(PushNotificationResponse response) {
                getView().hideProgress();
                getView().showListPushNotification(response);
            }

            @Override
            public void onFailure(String errorMessage) {
                getView().hideProgress();
                getView().showToast(errorMessage);
            }
        });
    }

//    public void getListPushNotification(long userPushId, int isSearch, String keySearch) {
//        String token = getModel(SharedPreferencesModel.class).getToken();
//        getModel(PushNotificationModel.class).getListPushNotification(token, userPushId, isSearch, keySearch, 0, Constants.TypePush.TYPE_ALL_PUSH, Constants.EMPTY_STRING, Constants.EMPTY_STRING,
//                new PushNotificationModel.IGetListPushNotificationCallback() {
//
//                    @Override
//                    public void onGetListPushNotificationSuccess(List<NotificationMessage> list, int page, int totalPage) {
//                        //getView().showListPushNotification(list, page, totalPage);
//                    }
//
//                    @Override
//                    public void onGetListPushNotificationFailure(ErrorMessage errorMessage) {
//                        //getView().displayErrorMessage(errorMessage);
//                    }
//                });
//    }

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
