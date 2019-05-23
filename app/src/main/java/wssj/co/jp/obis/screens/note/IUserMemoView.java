package wssj.co.jp.obis.screens.note;

import android.graphics.drawable.Drawable;

import wssj.co.jp.obis.model.memo.MemoDynamicResponse;
import wssj.co.jp.obis.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 19/5/2017.
 */

interface IUserMemoView extends IFragmentView {

    void showPhotoDialog(Drawable drawable, int requestCode);

    void showDialogChoose(int requestCodePicker, int requestCodeCamera);

    void openCamera(int requestCode);

    void requestCameraAndWriteStoragePermission(int requestCode);

    void requestStoragePermission(int requestCode);

    void openChooseImageScreen(int requestCode);

    void onGetMemoConfigSuccess(MemoDynamicResponse.UserMemoData data);

    void onGetMemoConfigFailure(String message);

    void onUpdateUserMemoSuccess(String message);

    void onUpdateUserMemoFailure(String message);
}
