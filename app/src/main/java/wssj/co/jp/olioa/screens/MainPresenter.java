package wssj.co.jp.olioa.screens;

import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;

import wssj.co.jp.olioa.model.auth.AuthModel;
import wssj.co.jp.olioa.model.checkin.CheckInModel;
import wssj.co.jp.olioa.model.firebase.NotificationMessage;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.model.pushnotification.PushNotificationModel;
import wssj.co.jp.olioa.model.util.UtilsModel;
import wssj.co.jp.olioa.screens.pushnotification.detail.PushNotificationDetailFragment;
import wssj.co.jp.olioa.screens.pushobject.MappingUserStoreResponse;
import wssj.co.jp.olioa.screens.pushobject.ObjectPush;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.model.baseapi.APICallback;
import wssj.co.jp.olioa.model.pushnotification.PushNotificationResponse;
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

    public void getListPushNotification(int page, int limit) {
        getModel(PushNotificationModel.class).getListPushNotification(page, new APICallback<PushNotificationResponse>() {

            @Override
            public void onSuccess(PushNotificationResponse response) {
                getView().showListPushNotification(response);
            }

            @Override
            public void onFailure(String errorMessage) {
                getView().showToast(errorMessage);
            }
        });
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
