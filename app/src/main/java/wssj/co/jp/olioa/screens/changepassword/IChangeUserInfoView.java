package wssj.co.jp.olioa.screens.changepassword;

import wssj.co.jp.olioa.model.entities.UserResponse;
import wssj.co.jp.olioa.screens.base.IFragmentView;

/**
 * Created by HieuPT on 5/15/2017.
 */

interface IChangeUserInfoView extends IFragmentView {

    void showDialogChoose(int requestCodePicker, int requestCodeCamera);

    void onValidateFailure(String message);

    void onChangePasswordSuccess(String message);

    void onChangePasswordFailure(String message);

    void onGetInfoUserSuccess(UserResponse infoUser);

    void openCamera(int requestCode);

    void requestCameraAndWriteStoragePermission(int requestCode);

    void openChooseImageScreen(int requestCode);

    void requestStoragePermission(int requestCode);

    void onRegisterSuccess();

    void onOnUpdateInfoUserFailure(String message);

}
