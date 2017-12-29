package jp.co.wssj.iungo.screens;

import android.os.Bundle;
import android.view.View;

import java.util.List;

import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.screens.base.IView;

public interface IMainView extends IView {

    int FRAGMENT_SPLASH_SCREEN = 0;

    int FRAGMENT_INTRODUCTION_SCREEN = 1;

    int FRAGMENT_LOGIN = 2;

    int FRAGMENT_RESET_PASSWORD = 3;

    int FRAGMENT_CHANGE_PASSWORD_CODE = 4;

    int FRAGMENT_REGISTER_ACCOUNT = 5;

    int FRAGMENT_MEMO_MANAGER = 8;

    int FRAGMENT_USER_MEMO = 12;

    int FRAGMENT_LIST_CARD = 13;

    int FRAGMENT_TERM_OF_SERVICE_N0_BOTTOM = 14;

    int FRAGMENT_PUSH_NOTIFICATION_LIST = 15;

    int FRAGMENT_PUSH_NOTIFICATION_DETAIL = 16;

    int FRAGMENT_LIST_STORE_CHECKED_IN = 18;

    int FRAGMENT_HOW_TO_USE = 19;

    int FRAGMENT_QA = 20;

    int FRAGMENT_CONTACT_US = 21;

    int FRAGMENT_CHANGE_PASSWORD = 22;

    int FRAGMENT_POLICY = 23;

    int FRAGMENT_ABOUT = 24;

    int FRAGMENT_TERM_OF_SERVICE = 25;

    int FRAGMENT_NOTIFICATION_FOR_SERVICE_COMPANY = 26;

    int FRAGMENT_QA_DETAIL = 27;

    int FRAGMENT_CHAT = 29;

    int FRAGMENT_COMMENT = 36;

    int FRAGMENT_TIMELINE = 37;

    int FRAGMENT_PUSH_NOTIFICATION_PAGER = 38;

    int FRAGMENT_LIST_SERVICE_COMPANY = 39;

    int FRAGMENT_STORE_FOLLOW = 40;

    int FRAGMENT_WAIT_STORE_CONFIRM = 41;

    int FRAGMENT_MANAGE_STAMP = 42;

    int FRAGMENT_SCANNER = 43;

    int FRAGMENT_HOME = 44;

    int FRAGMENT_CHAT_WRAPPER = 45;

    int FRAGMENT_LIST_SERVICE_COMPANY_WRAPPER = 46;

    int FRAGMENT_PUSH_NOTIFICATION_PAGER_NAVIGATION = 47;

    int FRAGMENT_TIMELINE_DETAIL = 48;

    int FRAGMENT_PROFILE = 49;

    int FRAGMENT_SCREEN_TEST=50;

    int FRAGMENT_CALENDER=51;

    int FRAGMENT_COUPONE=52;

    void switchScreen(int screenId, boolean hasAnimation, boolean addToBackStack, Bundle bundle);

    void switchScreen(int screenId, boolean hasAnimation, boolean addToBackStack, Bundle bundle, View sharedElement);

    void goBack();

    void clearBackStack();

    void finishActivity();

    void onOpenDrawableLayout();

    void onCloseDrawableLayout(int screenId, boolean hasAnimation, boolean addToBackStack, Bundle bundle, int navigationId);

    void enableDrawerLayout();

    void disableDrawerLayout();

    void logout();

    void showListPushNotificationUnRead(List<NotificationMessage> list, int page, int totalPage, int totalPushUnRead);

    void displayErrorMessage(String message);

    void onMappingUserStoreFastSuccess();

    void onMappingUserStoreFastFailure(String message);

    void displayScanCodeScreen();
}
