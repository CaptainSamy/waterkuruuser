package jp.co.wssj.iungo.screens.changepassword;

import android.graphics.drawable.Drawable;

import jp.co.wssj.iungo.model.auth.InfoUserResponse;
import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by HieuPT on 5/15/2017.
 */

interface IChangePasswordView extends IFragmentView {

    void showPhotoDialog(Drawable drawable, int requestCode);

    void showDialogChoose(int requestCodePicker, int requestCodeCamera);

    void onValidateFailure(String message);

    void onChangePasswordSuccess(String message);

    void onChangePasswordFailure(String message);

    void onGetInfoUserSuccess(InfoUserResponse.InfoUser infoUser);

    void onGetInfoUserFailure(String message);

    void openCamera(int requestCode);

    void requestCameraAndWriteStoragePermission(int requestCode);

    void openChooseImageScreen(int requestCode);

    void requestStoragePermission(int requestCode);

    void onOnUpdateInfoUserSuccess();

    void onOnUpdateInfoUserFailure(String message);

}
