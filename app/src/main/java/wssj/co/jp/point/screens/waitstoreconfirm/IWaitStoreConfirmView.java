package wssj.co.jp.point.screens.waitstoreconfirm;

import wssj.co.jp.point.model.checkin.CheckInStatusResponse;
import wssj.co.jp.point.screens.base.IFragmentView;

/**
 * Created by HieuPT on 5/19/2017.
 */

interface IWaitStoreConfirmView extends IFragmentView {

    void recheckStatus(int delayTimeMs, CheckInStatusResponse.CheckInStatusData data);

    void displayScreenManageStamp(int serviceId);

    void displayScreenScanCode();

    void stopCheckingStatus();
}
