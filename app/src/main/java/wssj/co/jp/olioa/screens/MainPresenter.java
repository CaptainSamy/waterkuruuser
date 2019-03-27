package wssj.co.jp.olioa.screens;

import android.os.Bundle;

import wssj.co.jp.olioa.model.auth.AuthModel;
import wssj.co.jp.olioa.model.checkin.CheckInModel;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.model.pushnotification.PushNotificationModel;
import wssj.co.jp.olioa.model.util.UtilsModel;
import wssj.co.jp.olioa.screens.base.BasePresenter;

class MainPresenter extends BasePresenter<IMainView> {

    private static final String TAG = "MainPresenter";

    MainPresenter(IMainView view) {
        super(view);
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new UtilsModel(view.getViewContext()));
        registerModel(new CheckInModel(view.getViewContext()));
        registerModel(new PushNotificationModel(view.getViewContext()));
        registerModel(new AuthModel(view.getViewContext()));
    }

    void displaySplashScreen() {
        getView().switchScreen(IMainView.FRAGMENT_SPLASH_SCREEN, true, false, null);
    }

    void onBackPress() {
        getView().goBack();
    }

    void logout() {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(AuthModel.class).removeDeviceToken(token);
        getModel(SharedPreferencesModel.class).clearAll();
        getView().logout();

    }

    void onOpenDrawableLayout() {
        getView().onOpenDrawableLayout();
    }

    void onCloseDrawableLayout(int screenId, boolean hasAnimation, boolean addToBackStack, Bundle bundle, int navigationId) {
        getView().onCloseDrawableLayout(screenId, hasAnimation, addToBackStack, bundle, navigationId);
    }

    void onBottomNavigationButtonClicked(int screenId, Bundle bundle) {
        getView().switchScreen(screenId, false, true, bundle);
    }

    void onDispatchFinishActivity() {
        getView().finishActivity();
    }

    void onEnableDrawerLayout() {
        getView().enableDrawerLayout();
    }

    void clearData() {
        getModel(SharedPreferencesModel.class).clearAll();
    }

    void onDisableDrawerLayout() {
        getView().disableDrawerLayout();
    }

    public String getUserName() {
        return getModel(SharedPreferencesModel.class).getUserName();
    }

    public boolean isLogin() {
        return getModel(SharedPreferencesModel.class).isLogin();
    }

    public void savePush(String objectPush) {
        getModel(SharedPreferencesModel.class).putObjectPush(objectPush);
    }

}
