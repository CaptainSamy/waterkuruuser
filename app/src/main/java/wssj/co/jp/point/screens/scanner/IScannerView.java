package wssj.co.jp.point.screens.scanner;

import wssj.co.jp.point.screens.base.IFragmentView;

/**
 * Created by HieuPT on 5/19/2017.
 */

interface IScannerView extends IFragmentView {

    void displayConfirmDialog(String qrCode);

    void startCamera();

    void releaseCamera();

    void stopCamera();

}
