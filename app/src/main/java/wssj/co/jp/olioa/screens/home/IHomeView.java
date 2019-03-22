package wssj.co.jp.olioa.screens.home;

import android.os.Bundle;

import wssj.co.jp.olioa.screens.switcher.ISwitcherView;

/**
 * Created by Nguyen Huu Ta on 20/6/2017.
 */

interface IHomeView extends ISwitcherView {

    void displayStampManagerScreen(Bundle bundle);

    void displayScanQRCodeScreen();

    void displayWaitStoreConfirmScreen(Bundle bundle);
}
