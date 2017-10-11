package jp.co.wssj.iungo.screens;

import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.List;

import jp.co.wssj.iungo.model.auth.AuthModel;
import jp.co.wssj.iungo.model.checkin.CheckInModel;
import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.model.pushnotification.PushNotificationModel;
import jp.co.wssj.iungo.model.util.UtilsModel;
import jp.co.wssj.iungo.screens.base.BasePresenter;
import jp.co.wssj.iungo.screens.pushnotification.detail.PushNotificationDetailFragment;
import jp.co.wssj.iungo.screens.pushobject.MappingUserStoreResponse;
import jp.co.wssj.iungo.screens.pushobject.ObjectPush;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;

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

    void onLogout() {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(AuthModel.class).removeDeviceToken(token);
        getModel(SharedPreferencesModel.class).clearAll();
        getView().onLogout();

    }

    void onOpenDrawableLayout() {
        getView().onOpenDrawableLayout();
    }

    void onCloseDrawableLayout(int screenId, boolean hasAnimation, boolean addToBackStack, Bundle bundle, int navigationId) {
        getView().onCloseDrawableLayout(screenId, hasAnimation, addToBackStack, bundle, navigationId);
    }

    void onBottomNavigationButtonClicked(int itemId) {
        getView().setSelectedPage(itemId);
    }

    void onDispatchFinishActivity() {
        getView().finishActivity();
    }

    void onEnableDrawerLayout() {
        getView().onEnableDrawerLayout();
    }

    void clearData() {
        getModel(SharedPreferencesModel.class).clearAll();
    }

    void onDisableDrawerLayout() {
        getView().onDisableDrawerLayout();
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

    public void mappingUserWithStoreFast(String code) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        if (!TextUtils.isEmpty(token)) {
            getModel(CheckInModel.class).mappingUserWithStoreFast(token, code, new CheckInModel.IMappingUserStoreFastCallback() {

                @Override
                public void onMappingUserStoreFastSuccess() {
                    getView().onMappingUserStoreFastSuccess();
                }

                @Override
                public void onMappingUserStoreFastFailure(String message) {
                    getView().onMappingUserStoreFastFailure(message);
                }
            });
        }
    }

    public void mappingUserWithStore() {
        String jsonPush = getModel(SharedPreferencesModel.class).getObjectPush();
        String token = getModel(SharedPreferencesModel.class).getToken();
        Gson gson = new Gson();
        ObjectPush objectPush = gson.fromJson(jsonPush, ObjectPush.class);
        if (objectPush != null) {
            if (System.currentTimeMillis() <= objectPush.getSaveTime()) {
                getModel(CheckInModel.class).mappingUserWithStore(token, objectPush.getCode(), new CheckInModel.IMappingUserStoreCallback() {

                    @Override
                    public void onMappingUserStoreSuccess(MappingUserStoreResponse.PushData data) {
                        getModel(SharedPreferencesModel.class).putObjectPush(Constants.EMPTY_STRING);
                        NotificationMessage notificationMessage = new NotificationMessage(data.getPushId(),
                                Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING, 0);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(PushNotificationDetailFragment.NOTIFICATION_ARG, notificationMessage);
                        bundle.putBoolean(PushNotificationDetailFragment.FLAG_FROM_ACTIVITY, true);
                        getView().switchScreen(IMainView.FRAGMENT_PUSH_NOTIFICATION_DETAIL, true, false, bundle);
                    }

                    @Override
                    public void onMappingUserStoreFailure(String message) {
                        getView().displayScanCodeScreen();
                    }
                });
            } else {
                getModel(SharedPreferencesModel.class).putObjectPush(Constants.EMPTY_STRING);
                getView().displayScanCodeScreen();
            }
        } else {
            getModel(SharedPreferencesModel.class).putObjectPush(Constants.EMPTY_STRING);
            getView().displayScanCodeScreen();
        }
    }

    public String getUserName() {
        return getModel(SharedPreferencesModel.class).getUserName();
    }

    public String getPhotoUrl() {
        return getModel(SharedPreferencesModel.class).getPhotoUrl();
    }

    public boolean isLogin() {
        return getModel(SharedPreferencesModel.class).isLogin();
    }

    public void savePush(String objectPush) {
        getModel(SharedPreferencesModel.class).putObjectPush(objectPush);
    }

}
