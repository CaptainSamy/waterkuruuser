package jp.co.wssj.iungo.screens.waitstoreconfirm;

import jp.co.wssj.iungo.model.checkin.CheckInStatusResponse;
import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by HieuPT on 5/19/2017.
 */

interface IWaitStoreConfirmView extends IFragmentView {

    void recheckStatus(int delayTimeMs, CheckInStatusResponse.CheckInStatusData data);

    void displayScreenManageStamp(String serviceName);

    void displayScreenScanCode();

    void stopCheckingStatus();
}
