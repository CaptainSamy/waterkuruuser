package wssj.co.jp.olioa.model.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import wssj.co.jp.olioa.model.BaseModel;

/**
 * Created by HieuPT on 5/17/2017.
 */

public class UtilsModel extends BaseModel {

    public interface ICheckSelfPermissionResultCallback {

        void onPermissionGranted();

        void onPermissionDenied();
    }

    public UtilsModel(Context context) {
        super(context);
    }

    public void checkCameraPermission(ICheckSelfPermissionResultCallback callback) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            callback.onPermissionGranted();
        } else {
            callback.onPermissionDenied();
        }
    }

    public void checkCameraAndWriteExternalStoragePermission(ICheckSelfPermissionResultCallback callback) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            callback.onPermissionGranted();
        } else {
            callback.onPermissionDenied();
        }
    }

    public void checkWriteExternalStoragePermission(ICheckSelfPermissionResultCallback callback) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            callback.onPermissionGranted();
        } else {
            callback.onPermissionDenied();
        }
    }

    public void checkAccessLocationPermission(ICheckSelfPermissionResultCallback callback) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            callback.onPermissionGranted();
        } else {
            callback.onPermissionDenied();
        }
    }
}
