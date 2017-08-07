package jp.co.wssj.iungo.screens.splash;

import jp.co.wssj.iungo.model.auth.CheckVersionAppResponse;
import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 5/6/2017.
 */

public interface ISplashView extends IFragmentView {

    void displayScreen(int fragmentId);

    void showDialog(CheckVersionAppResponse.CheckVersionAppData response);
}
