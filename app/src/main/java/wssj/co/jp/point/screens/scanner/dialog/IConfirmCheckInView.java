package wssj.co.jp.point.screens.scanner.dialog;

import wssj.co.jp.point.model.checkin.ConfirmCheckInResponse;
import wssj.co.jp.point.model.checkin.InfoStoreResponse;
import wssj.co.jp.point.screens.base.IDialogView;

/**
 * Created by HieuPT on 5/17/2017.
 */

interface IConfirmCheckInView extends IDialogView {

    void onGetInfoStoreSuccess(InfoStoreResponse.InfoStoreData data);

    void onGetInfoStoreFailure(String message);

    void displayWaitStoreConfirmScreen(ConfirmCheckInResponse.SessionData data);

    void showConfirmFailureMessage(String message);
}
