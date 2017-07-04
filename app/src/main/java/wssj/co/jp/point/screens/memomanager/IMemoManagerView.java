package wssj.co.jp.point.screens.memomanager;

import android.graphics.drawable.Drawable;

import wssj.co.jp.point.model.memo.ListServiceResponse;
import wssj.co.jp.point.model.memo.UserMemoResponse;
import wssj.co.jp.point.screens.base.IFragmentView;

/**
 * Created by HieuPT on 17/5/2017.
 */

interface IMemoManagerView extends IFragmentView {

    void onGetUserMemoSuccess(UserMemoResponse.UserMemo response);

    void onGetUserMemoFailure(String message);

    void requestCameraAndWriteStoragePermission(int requestCode);

    void openCamera(int requestCode);

    void requestStoragePermission(int requestCode);

    void showPhotoDialog(Drawable drawable, int requestCode);

    void showDialogChoose(int requestCodePicker, int requestCodeCamera);

    void onGetListServicesSuccess(ListServiceResponse.ServiceData data);

    void onGetListServicesFailure(String message);

    void openChooseImageScreen(int requestCode);

    void onUpdateUserMemoSuccess(String message);

    void onUpdateUserMemoFailure(String message);
}
