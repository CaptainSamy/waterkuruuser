package wssj.co.jp.point.screens.note;

import android.graphics.drawable.Drawable;

import wssj.co.jp.point.model.entities.StatusMemoData;
import wssj.co.jp.point.model.memo.UserMemoModel;
import wssj.co.jp.point.model.memo.UserMemoResponse;
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

    void getUserMemo(int serviceId) {
        getView().showProgress();
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(UserMemoModel.class).getUserMemo(token, serviceId, new UserMemoModel.IUserMemoCallback() {

            @Override
            public void onGetUserMemoSuccess(UserMemoResponse.UserMemoData data) {
                getView().hideProgress();
                if (data.getUserMemo() != null) {
                    getView().onGetUserMemoSuccess(data.getUserMemo());
                }
            }

            @Override
            public void onGetUserMemoFailure(String message) {
                getView().hideProgress();
                getView().onGetUserMemoFailure(message);
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

    void updateUserMemo(final int serviceId, final String note, StatusMemoData[] listStatusImage) {
        final String token = getModel(SharedPreferencesModel.class).getToken();
        getView().showProgress();
        getModel(UserMemoModel.class).uploadImageAWS(listStatusImage, 0, new UserMemoModel.IUpdateAWSCallback() {

            @Override
            public void onUpdateUserMemoSuccess(StatusMemoData[] listStatusImage) {
                getModel(UserMemoModel.class).updateUserMemo(token, serviceId, note, listStatusImage, new UserMemoModel.IUpdateUserMemoCallback() {

                    @Override
                    public void onUpdateUserMemoSuccess(String message) {
                        getView().hideProgress();
                        getView().onUpdateSuccess(message);
                    }

                    @Override
                    public void onUpdateUserMemoFailure(String message) {
                        getView().hideProgress();
                        getView().onUpdateFailure(message);
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
