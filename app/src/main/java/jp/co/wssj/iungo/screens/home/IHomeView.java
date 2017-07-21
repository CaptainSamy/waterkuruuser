package jp.co.wssj.iungo.screens.home;

import android.os.Bundle;

import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 20/6/2017.
 */

public interface IHomeView extends IFragmentView {

    void requestCameraPermission();

    void switchScreen(int screenId, boolean hasAnimation, boolean addToBackStack, Bundle bundle);
}
