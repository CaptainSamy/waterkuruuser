package wssj.co.jp.obis.screens.changepassword;

import wssj.co.jp.obis.model.entities.UserResponse;
import wssj.co.jp.obis.screens.base.IFragmentView;

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
