package wssj.co.jp.point.screens.note;

import android.graphics.drawable.Drawable;

import wssj.co.jp.point.model.memo.UserMemoResponse;
import wssj.co.jp.point.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 19/5/2017.
 */

interface IUserMemoView extends IFragmentView {

    void onGetUserMemoSuccess(UserMemoResponse.UserMemo response);

    void showPhotoDialog(Drawable drawable, int requestCode);

    void showDialogChoose(int requestCodePicker, int requestCodeCamera);

    void openCamera(int requestCode);

    void requestCameraAndWriteStoragePermission(int requestCode);

    void onGetUserMemoFailure(String message);

    void requestStoragePermission(int requestCode);

    void openChooseImageScreen(int requestCode);

    void onUpdateSuccess(String message);

    void onUpdateFailure(String message);
}
