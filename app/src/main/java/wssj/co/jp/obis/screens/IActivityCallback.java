package wssj.co.jp.obis.screens;

import android.os.Bundle;
import android.view.View;

import wssj.co.jp.obis.screens.base.BaseFragment;

/**
 * Created by HieuPT on 3/21/2017.
 */

public interface IActivityCallback {

    void displayScreen(int screenId, boolean hasAnimation, boolean addToBackStack);

    void displayScreen(int screenId, boolean hasAnimation, boolean addToBackStack, Bundle bundle);

    void displayScreen(int screenId, boolean hasAnimation, boolean addToBackStack, Bundle bundle, View sharedElement);

    void clearBackStack();

    void onFragmentResumed(BaseFragment fragment);

    void setAppBarTitle(String title);

    void onBackPressed(Bundle bundle);

    void finishActivity();
}
