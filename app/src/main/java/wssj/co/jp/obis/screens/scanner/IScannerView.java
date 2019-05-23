package wssj.co.jp.obis.screens.scanner;

import wssj.co.jp.obis.model.entities.StoreInfo;
import wssj.co.jp.obis.screens.base.IFragmentView;

/**
 * Created by HieuPT on 5/19/2017.
 */

interface IScannerView extends IFragmentView {

    void checkInSuccess(StoreInfo storeInfo);

    void onConfirmSuccess(Integer id);

    void startCamera();

    void releaseCamera();

    void stopCamera();

    void requestCameraPermission();
}
