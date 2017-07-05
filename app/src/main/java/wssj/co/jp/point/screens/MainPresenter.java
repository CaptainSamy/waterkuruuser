package wssj.co.jp.point.screens;

import android.os.Bundle;
import android.text.TextUtils;

import java.util.List;

import wssj.co.jp.point.model.ErrorMessage;
import wssj.co.jp.point.model.checkin.CheckInModel;
import wssj.co.jp.point.model.firebase.NotificationMessage;
import wssj.co.jp.point.model.preference.SharedPreferencesModel;
import wssj.co.jp.point.model.pushnotification.PushNotificationModel;
import wssj.co.jp.point.model.util.UtilsModel;
import wssj.co.jp.point.screens.base.BasePresenter;

class MainPresenter extends BasePresenter<IMainView> {

    private static final String TAG = "MainPresenter";

    MainPresenter(IMainView view) {
        super(view);
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new UtilsModel(view.getViewContext()));
        registerModel(new CheckInModel(view.getViewContext()));
        registerModel(new PushNotificationModel(view.getViewContext()));
    }

    void onCreate() {
        getView().switchScreen(IMainView.FRAGMENT_SPLASH_SCREEN, true, false, null);
    }

    void onBackPress() {
        getView().goBack();
    }

    void onLogout() {
        getModel(SharedPreferencesModel.class).clearAll();
        getView().onLogout();
    }

    void onOpenDrawableLayout() {
        getView().onOpenDrawableLayout();
    }

    void onCloseDrawableLayout(int screenId, boolean hasAnimation, boolean addToBackStack, Bundle bundle) {
        getView().onCloseDrawableLayout(screenId, hasAnimation, addToBackStack, bundle);
    }

    void onEnableDrawableLayout() {
        getView().onEnableDrawableLayout();
    }

    void onDisableDrawableLayout() {
        getView().onDisableDrawableLayout();
    }

    public void getListPushNotification(int page, int limit) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        if (!TextUtils.isEmpty(token)) {
            getModel(PushNotificationModel.class).getListPushNotification(token, page, limit, new PushNotificationModel.IGetListPushNotificationCallback() {

                @Override
                public void onGetListPushNotificationSuccess(List<NotificationMessage> list, int page, int totalPage, int numberPushUnreadThisPage, int totalPushUnRead) {
                    getView().showListPushNotification(list, page, totalPage, numberPushUnreadThisPage, totalPushUnRead);
                }

                @Override
                public void onGetListPushNotificationFailure(ErrorMessage errorMessage) {
                    getView().displayErrorMessage(errorMessage.getMessage());
                }
            });
        }
    }

    public void setListPushUnRead(List<NotificationMessage> listPustNotification) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        if (!TextUtils.isEmpty(token)) {
            getModel(PushNotificationModel.class).setListPushUnRead(token, listPustNotification, new PushNotificationModel.ISetListPushNotificationCallback() {

                @Override
                public void onSetListPushNotificationSuccess(int numberNotificationUnRead) {
                    getView().setListPushUnReadSuccess(numberNotificationUnRead);
                }

                @Override
                public void onSetListPushNotificationFailure() {

                }
            });
        }
    }
}
