package wssj.co.jp.point.screens.note;

import android.graphics.drawable.Drawable;

import java.util.List;

import wssj.co.jp.point.model.memo.MemoDynamicResponse;
import wssj.co.jp.point.model.memo.UserMemoModel;
import wssj.co.jp.point.model.preference.SharedPreferencesModel;
import wssj.co.jp.point.model.util.UtilsModel;
import wssj.co.jp.point.screens.base.FragmentPresenter;

/**
 * Created by Nguyen Huu Ta on 19/5/2017.
 */

class UserMemoPresenter extends FragmentPresenter<IUserMemoView> {

    UserMemoPresenter(IUserMemoView view) {
        super(view);
        registerModel(new UserMemoModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new UtilsModel(view.getViewContext()));
    }

    public void getUserMemo(int serviceId) {
        getView().showProgress();
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(UserMemoModel.class).getUserMemoConfig(token, serviceId, new UserMemoModel.IGetMemoConfigCallback() {

            @Override
            public void onGetMemoConfigSuccess(MemoDynamicResponse.UserMemoData data) {
                getView().hideProgress();
                getView().onGetMemoConfigSuccess(data);
            }

            @Override
            public void onGetMemoConfigFailure(String message) {
                getView().hideProgress();
                getView().onGetMemoConfigFailure(message);
            }
        });
    }

    private void checkCameraAndWriteExternalStoragePermission(final int requestCode) {
        getModel(UtilsModel.class).checkCameraAndWriteExternalStoragePermission(new UtilsModel.ICheckSelfPermissionResultCallback() {

            @Override
            public void onPermissionGranted() {
                getView().openCamera(requestCode);
            }

            @Override
            public void onPermissionDenied() {
                getView().requestCameraAndWriteStoragePermission(requestCode);
            }
        });
    }

    private void checkWriteExternalStoragePermission(final int requestCode) {
        getModel(UtilsModel.class).checkWriteExternalStoragePermission(new UtilsModel.ICheckSelfPermissionResultCallback() {

            @Override
            public void onPermissionGranted() {
                getView().openChooseImageScreen(requestCode);
            }

            @Override
            public void onPermissionDenied() {
                getView().requestStoragePermission(requestCode);
            }
        });
    }

    void updateUserMemo(final int serviceId, List<MemoDynamicResponse.UserMemoData.UserMemoValue> memoValues) {
        final String token = getModel(SharedPreferencesModel.class).getToken();
        getView().showProgress();
        getModel(UserMemoModel.class).uploadImageAWS(memoValues, 0, null, new UserMemoModel.IUpdateAWSCallback() {

            @Override
            public void onUpdateUserMemoSuccess(List<MemoDynamicResponse.UserMemoData.UserMemoValue> memoValues) {
                getModel(UserMemoModel.class).updateUserMemo(token, serviceId, memoValues, new UserMemoModel.IUpdateUserMemoCallback() {

                    @Override
                    public void onUpdateUserMemoSuccess(String message) {
                        getView().hideProgress();
                        getView().onUpdateUserMemoSuccess(message);
                    }

                    @Override
                    public void onUpdateUserMemoFailure(String message) {
                        getView().hideProgress();
                        getView().onUpdateUserMemoFailure(message);
                    }
                });
            }
        });
    }

    void onImageViewClicked(Drawable drawable, int requestCodePicker, int requestCodeCamera) {
        if (drawable != null) {
            getView().showPhotoDialog(drawable, requestCodePicker);
        } else {
            getView().showDialogChoose(requestCodePicker, requestCodeCamera);
        }
    }

    void onPickPhotoFromGalleryButtonClicked(int requestCode) {
        checkWriteExternalStoragePermission(requestCode);
    }

    void onPickPhotoFromCameraButtonClicked(int requestCode) {
        checkCameraAndWriteExternalStoragePermission(requestCode);
    }
}
