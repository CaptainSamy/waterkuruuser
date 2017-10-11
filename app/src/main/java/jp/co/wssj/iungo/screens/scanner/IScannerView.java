package jp.co.wssj.iungo.screens.scanner;

import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by HieuPT on 5/19/2017.
 */

interface IScannerView extends IFragmentView {

    void displayConfirmDialog(String qrCode);

    void startCamera();

    void releaseCamera();

    void stopCamera();

    void requestCameraPermission();
}
