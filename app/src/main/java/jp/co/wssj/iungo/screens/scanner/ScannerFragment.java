package jp.co.wssj.iungo.screens.scanner;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.screens.base.IWrapperFragment;
import jp.co.wssj.iungo.screens.base.IWrapperFragmentController;
import jp.co.wssj.iungo.screens.base.PagedFragment;
import jp.co.wssj.iungo.screens.scanner.dialog.ConfirmCheckInDialog;
import jp.co.wssj.iungo.utils.Logger;

/**
 * Created by HieuPT on 5/19/2017.
 */

public class ScannerFragment extends PagedFragment<IScannerView, ScannerPresenter>
        implements IScannerView, ConfirmCheckInDialog.IListenerDismissDialog, IWrapperFragmentController, PagedFragment.IOnPageSelectChangeListener {

    private static final String TAG = "ScannerFragment";

    private static final int REQUEST_CAMERA_CODE = 100;

    private BarcodeDetector mBarcode;

    private CameraSource mCameraSource;

    private SurfaceHolder mHolder;

    private ConfirmCheckInDialog mDialog;

    private boolean mIsSurfaceCreated, mIsCameraStarted;

    private IWrapperFragment mWrapperFragment;

    private final Handler mHandler = new Handler();

    private final SurfaceHolder.Callback mHolderCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Logger.d(TAG, "#surfaceCreated");
            mIsSurfaceCreated = true;
            attemptStartCamera();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Logger.d(TAG, "#surfaceChanged");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Logger.d(TAG, "#surfaceDestroyed");
            mIsSurfaceCreated = false;
            stopCamera();
        }
    };

    @Override
    public String getPageTitle(Context context) {
        return getString(context, R.string.read_qr_code);
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_scanner;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected void initViews(View rootView) {
        SurfaceView cameraView = (SurfaceView) rootView.findViewById(R.id.cameraView);
        cameraView.setZOrderMediaOverlay(true);
        mHolder = cameraView.getHolder();
        mBarcode = new BarcodeDetector.Builder(getActivityContext())
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
    }

    @Override
    protected void initAction() {
        mHolder.addCallback(mHolderCallback);
        mBarcode.setProcessor(new Detector.Processor<Barcode>() {

            @Override
            public void release() {
                Logger.d(TAG, "#release");
            }

            @Override
            public void receiveDetections(final Detector.Detections<Barcode> detections) {
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (mCameraSource != null && mIsCameraStarted) {
                            SparseArray<Barcode> barCodes = detections.getDetectedItems();
                            if (barCodes.size() > 0) {
                                getPresenter().verifyCode(barCodes.valueAt(0));
                            }
                        }
                    }
                });
            }
        });
        mCameraSource = new CameraSource.Builder(getActivityContext(), mBarcode)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(24)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(getResources().getDisplayMetrics().heightPixels, getResources().getDisplayMetrics().widthPixels)
                .build();
        addOnPageSelectChangeListener(this);
    }

    @Override
    protected ScannerPresenter onCreatePresenter(IScannerView view) {
        return new ScannerPresenter(view);
    }

    @Override
    protected IScannerView onCreateView() {
        return this;
    }

    @Override

    public void displayConfirmDialog(String qrCode) {
        if (mDialog == null) {
            mDialog = new ConfirmCheckInDialog(getActivityContext(), getActivityCallback(), mWrapperFragment, this);
        }
        mDialog.showDialog(qrCode);
    }

    @Override
    public void onDismissDialog() {
        attemptStartCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint() && ContextCompat.checkSelfPermission(getActivityContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopCamera();
    }

    private void attemptStartCamera() {
        getPresenter().attemptStartCamera();
    }

    @Override
    public void startCamera() {
        Logger.d(TAG, "#startCamera");
        try {
            if (mCameraSource != null && mIsSurfaceCreated) {
                if (ContextCompat.checkSelfPermission(getActivityContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    if (!mIsCameraStarted) {
                        mCameraSource.start(mHolder);
                        mIsCameraStarted = true;
                    }
                }
            }
        } catch (IOException e) {
            Logger.e(TAG, "IOException: " + e.getMessage());
        }
    }

    @Override
    public void stopCamera() {
        Logger.d(TAG, "#stopCamera");
        if (mCameraSource != null) {
            if (mIsCameraStarted) {
                mCameraSource.stop();
                mIsCameraStarted = false;
            }
        }
    }

    @Override
    public void requestCameraPermission() {
        requestPermissions(new String[]{
                Manifest.permission.CAMERA
        }, REQUEST_CAMERA_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_CODE
                && permissions.length > 0
                && Manifest.permission.CAMERA.equals(permissions[0])) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    getPresenter().onPermissionDenied();
                } else {
                    getPresenter().onPermissionDeniedAndDontAskAgain();
                }
            }
        }
    }

    @Override
    public void releaseCamera() {
        if (mCameraSource != null) {
            mCameraSource.release();
            mCameraSource = null;
        }
    }

    @Override
    public void setWrapperFragment(IWrapperFragment fragment) {
        mWrapperFragment = fragment;
    }

    @Override
    public void onPageSelected() {
        attemptStartCamera();
    }

    @Override
    public void onPageUnselected() {
        stopCamera();
    }
}