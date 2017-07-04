package wssj.co.jp.point.screens.home;

import android.os.Bundle;

import wssj.co.jp.point.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 20/6/2017.
 */

public interface IHomeView extends IFragmentView {

    void requestCameraPermission();

    void switchScreen(int screenId, boolean hasAnimation, boolean addToBackStack, Bundle bundle);
}
