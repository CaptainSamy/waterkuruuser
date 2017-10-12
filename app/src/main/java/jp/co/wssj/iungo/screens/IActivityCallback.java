package jp.co.wssj.iungo.screens;

import android.os.Bundle;
import android.view.View;

import jp.co.wssj.iungo.screens.base.BaseFragment;

/**
 * Created by HieuPT on 3/21/2017.
 */

public interface IActivityCallback {

    void displayScreen(int screenId, boolean hasAnimation, boolean addToBackStack);

    void displayScreen(int screenId, boolean hasAnimation, boolean addToBackStack, Bundle bundle);

    void displayScreen(int screenId, boolean hasAnimation, boolean addToBackStack, Bundle bundle, View sharedElement);

    void clearBackStack();

    void onFragmentResumed(BaseFragment fragment);

    void setSelectedNavigationBottom(int id);

    void setAppBarTitle(String title);

    void onBackPressed(Bundle bundle);

    void showTextNoItem(boolean isShow, String content);
}
