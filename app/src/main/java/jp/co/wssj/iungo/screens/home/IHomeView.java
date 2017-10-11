package jp.co.wssj.iungo.screens.home;

import android.os.Bundle;

import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 20/6/2017.
 */

interface IHomeView extends IFragmentView {

    void displayStampManagerScreen(Bundle bundle);

    void displayScanQRCodeScreen();

    void displayWaitStoreConfirmScreen(Bundle bundle);
}
