package jp.co.wssj.iungo.screens;

import android.os.Bundle;

import jp.co.wssj.iungo.screens.base.BaseFragment;

/**
 * Created by HieuPT on 3/21/2017.
 */

public interface IActivityCallback {

    void displayScreen(int screenId, boolean hasAnimation, boolean addToBackStack);

    void displayScreen(int screenId, boolean hasAnimation, boolean addToBackStack, Bundle bundle);

    void clearBackStack();

    void onFragmentResumed(BaseFragment fragment);
}