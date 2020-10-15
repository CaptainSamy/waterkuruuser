package wssj.co.jp.obis.screens;

import android.os.Bundle;
import android.view.View;

import wssj.co.jp.obis.screens.base.IView;

public interface IMainView extends IView {

    int FRAGMENT_SPLASH_SCREEN = 0;

    int FRAGMENT_INTRODUCTION_SCREEN = 1;

    int FRAGMENT_LOGIN = 2;

    int FRAGMENT_RESET_PASSWORD = 3;

    int FRAGMENT_CHANGE_PASSWORD_CODE = 4;

    int FRAGMENT_TERM_OF_SERVICE_N0_BOTTOM = 14;

    int FRAGMENT_PUSH_NOTIFICATION_LIST = 15;

    int FRAGMENT_PUSH_NOTIFICATION_DETAIL = 16;

    int FRAGMENT_PUSH_NOTIFICATION = 17;

    int FRAGMENT_LIST_STORE_CHAT = 6;

    int FRAGMENT_HOW_TO_USE = 19;

    int FRAGMENT_QA = 20;

    int FRAGMENT_CONTACT_US = 21;

    int FRAGMENT_CHANGE_PASSWORD = 22;

    int FRAGMENT_POLICY = 23;

    int FRAGMENT_ABOUT = 24;

    int FRAGMENT_TERM_OF_SERVICE = 25;

    int FRAGMENT_CHAT = 29;

    int FRAGMENT_TIMELINE = 37;

    int FRAGMENT_STORE_FOLLOW = 40;

    int FRAGMENT_SCANNER = 43;

    int FRAGMENT_HOME = 44;

    int FRAGMENT_CHAT_REALTIME = 57;

    int FRAGMENT_CONVERSATION = 58;

    int FRAGMENT_CHANGE_ACCOUNT = 59;

    int FRAGMENT_GROUP_CHAT = 60;

    int FRAGMENT_GROUP_CHAT_DETAIL = 61;

    int FRAGMENT_BLOCK_CHAT = 62;

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

    void showToast(String message);

    void displayErrorMessage(String message);

    void displayScanCodeScreen();
}
