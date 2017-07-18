package jp.co.wssj.iungo.screens.scanner.dialog;

import jp.co.wssj.iungo.model.checkin.ConfirmCheckInResponse;
import jp.co.wssj.iungo.model.checkin.InfoStoreResponse;
import jp.co.wssj.iungo.screens.base.IDialogView;

/**
 * Created by HieuPT on 5/17/2017.
 */

interface IConfirmCheckInView extends IDialogView {

    void onGetInfoStoreSuccess(InfoStoreResponse.InfoStoreData data);

    void onGetInfoStoreFailure(String message);

    void displayWaitStoreConfirmScreen(ConfirmCheckInResponse.SessionData data);

    void onConfirmFailure(String message);
}
