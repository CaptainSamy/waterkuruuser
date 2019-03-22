package wssj.co.jp.olioa.screens.changepassword;

import android.graphics.drawable.Drawable;

import wssj.co.jp.olioa.model.auth.InfoUserResponse;
import wssj.co.jp.olioa.screens.base.IFragmentView;

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
