package jp.co.wssj.iungo.screens.scanner;

import com.google.android.gms.vision.barcode.Barcode;

import jp.co.wssj.iungo.model.checkin.CheckInModel;
import jp.co.wssj.iungo.model.util.UtilsModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;

/**
 * Created by HieuPT on 5/19/2017.
 */

class ScannerPresenter extends FragmentPresenter<IScannerView> {

    ScannerPresenter(IScannerView view) {
        super(view);
        registerModel(new CheckInModel(view.getViewContext()));
        registerModel(new UtilsModel(view.getViewContext()));
    }

    @Override
    protected void onFragmentDestroyView() {
        super.onFragmentDestroyView();
        getView().releaseCamera();
    }

    void verifyCode(Barcode barcode) {
        final String qrCode = barcode.displayValue;
        getModel(CheckInModel.class).verifyStoreQRCode(qrCode, new CheckInModel.IVerifyStoreQRCodeCallback() {

            @Override
            public void onVerified() {
                getView().stopCamera();
                getView().displayConfirmDialog(qrCode);
            }
        });
    }

    void attemptStartCamera() {
        getModel(UtilsModel.class).checkCameraPermission(new UtilsModel.ICheckSelfPermissionResultCallback() {

            @Override
            public void onPermissionGranted() {
                getView().startCamera();
            }

            @Override
            public void onPermissionDenied() {
                getView().requestCameraPermission();
            }
        });
    }

    void onPermissionDenied() {
    }

    void onPermissionDeniedAndDontAskAgain() {
    }
}