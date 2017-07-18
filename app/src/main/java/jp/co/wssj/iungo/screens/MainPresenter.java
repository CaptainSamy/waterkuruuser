package jp.co.wssj.iungo.screens;

import android.os.Bundle;
import android.text.TextUtils;

import java.util.List;

import jp.co.wssj.iungo.model.checkin.CheckInModel;
import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.model.pushnotification.PushNotificationModel;
import jp.co.wssj.iungo.model.util.UtilsModel;
import jp.co.wssj.iungo.screens.base.BasePresenter;

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

    void onCloseDrawableLayout(int screenId, boolean hasAnimation, boolean addToBackStack, Bundle bundle, int navigationId) {
        getView().onCloseDrawableLayout(screenId, hasAnimation, addToBackStack, bundle, navigationId);
    }

    void onEnableDrawableLayout() {
        getView().onEnableDrawableLayout();
    }

    void onDisableDrawableLayout() {
        getView().onDisableDrawableLayout();
    }

    public void getListPushNotificationUnRead(int page, int limit) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        if (!TextUtils.isEmpty(token)) {
            getModel(PushNotificationModel.class).getListPushNotificationUnRead(token, page, limit, new PushNotificationModel.IGetListPushNotificationUnReadCallback() {

                @Override
                public void onGetListPushNotificationUnReadSuccess(List<NotificationMessage> list, int page, int totalPage, int totalPushUnRead) {
                    getView().showListPushNotificationUnRead(list, page, totalPage, totalPushUnRead);
                }

                @Override
                public void onGetListPushNotificationUnReadFailure(String message) {
                    getView().displayErrorMessage(message);
                }
            });
        }
    }
}
