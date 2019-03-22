package wssj.co.jp.olioa.screens.waitstoreconfirm;

import wssj.co.jp.olioa.model.checkin.CheckInStatusResponse;
import wssj.co.jp.olioa.screens.base.IFragmentView;

/**
 * Created by HieuPT on 5/19/2017.
 */

interface IWaitStoreConfirmView extends IFragmentView {

    void recheckStatus(int delayTimeMs, CheckInStatusResponse.CheckInStatusData data);

    void displayScreenManageStamp(String serviceName);

    void displayScreenScanCode();

    void stopCheckingStatus();
}
