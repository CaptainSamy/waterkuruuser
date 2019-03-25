package wssj.co.jp.olioa.screens.scanner;

import com.google.android.gms.vision.barcode.Barcode;

import wssj.co.jp.olioa.model.baseapi.APICallback;
import wssj.co.jp.olioa.model.checkin.CheckInModel;
import wssj.co.jp.olioa.model.entities.StoreInfo;
import wssj.co.jp.olioa.model.util.UtilsModel;
import wssj.co.jp.olioa.screens.base.FragmentPresenter;

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

    void checkInCode(String code) {
        getView().stopCamera();
        getView().showProgress();
        getModel(CheckInModel.class).checkIn(code, new APICallback<StoreInfo>() {

            @Override
            public void onSuccess(StoreInfo storeInfo) {
                getView().hideProgress();
                getView().checkInSuccess(storeInfo);
            }

            @Override
            public void onFailure(String errorMessage) {
                getView().hideProgress();
                getView().showDialog(errorMessage);
            }
        });
    }

    void onConfirm(String code) {
        getView().showProgress();
        getModel(CheckInModel.class).userConfirm(code, new APICallback<Integer>() {

            @Override
            public void onSuccess(Integer id) {
                getView().hideProgress();
                getView().onConfirmSuccess(id);
            }

            @Override
            public void onFailure(String errorMessage) {
                getView().hideProgress();
                getView().showDialog(errorMessage);
            }
        });
    }

    void verifyCode(Barcode barcode) {
        final String qrCode = barcode.displayValue;
        getModel(CheckInModel.class).verifyStoreQRCode(qrCode, new CheckInModel.IVerifyStoreQRCodeCallback() {

            @Override
            public void onVerified() {
                getView().stopCamera();
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