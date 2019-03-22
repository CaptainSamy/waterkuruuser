package wssj.co.jp.olioa.screens.changepassword;

import android.graphics.drawable.Drawable;

import wssj.co.jp.olioa.model.auth.AuthModel;
import wssj.co.jp.olioa.model.auth.InfoUserResponse;
import wssj.co.jp.olioa.model.auth.RegisterData;
import wssj.co.jp.olioa.model.firebase.FirebaseModel;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.model.util.UtilsModel;
import wssj.co.jp.olioa.screens.base.FragmentPresenter;

/**
 * Created by HieuPT on 5/15/2017.
 */

class ChangePasswordPresenter extends FragmentPresenter<IChangePasswordView> {

    ChangePasswordPresenter(IChangePasswordView view) {
        super(view);
        registerModel(new AuthModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new FirebaseModel(view.getViewContext()));
        registerModel(new UtilsModel(view.getViewContext()));
    }

    private AuthModel getAuth() {
        return getModel(AuthModel.class);
    }

    void onChangePasswordByCodeClicked(String code, String password, String confirmPassword) {
        getView().showProgress();
        getAuth().changePasswordByCode(code, password, confirmPassword, new AuthModel.IOnChangePasswordCallback() {

            @Override
            public void onValidateFailure(String message) {
                getView().hideProgress();
                getView().onValidateFailure(message);
            }

            @Override
            public void onChangePasswordSuccess(RegisterData data, String message) {
                getView().hideProgress();
                if (data != null) {
                    getModel(SharedPreferencesModel.class).putToken(data.getToken());
                    getModel(SharedPreferencesModel.class).putExpireDate(data.getExpireDate());
                    getModel(FirebaseModel.class).uploadDeviceToken(data.getToken(), null);
                }
                getView().onChangePasswordSuccess(message);
            }

            @Override
            public void onChangePasswordFailure(String message) {
                getView().hideProgress();
                getView().onChangePasswordFailure(message);
            }
        });
    }

//    void onChangePasswordClicked(String currentPassword, String password, String confirmPassword) {
//        String token = getModel(SharedPreferencesModel.class).getToken();
//        getView().showProgress();
//        getAuth().changePassword(token, currentPassword, password, confirmPassword, new AuthModel.IOnChangePasswordCallback() {
//
//            @Override
//            public void onValidateFailure(String message) {
//                getView().hideProgress();
//                getView().onValidateFailure(message);
//            }
//
//            @Override
//            public void onChangePasswordSuccess(RegisterData data, String message) {
//                getView().hideProgress();
//                if (data != null) {
//                    getModel(SharedPreferencesModel.class).putToken(data.getToken());
//                    getModel(SharedPreferencesModel.class).putExpireDate(data.getExpireDate());
//                    getModel(FirebaseModel.class).uploadDeviceToken(data.getToken(), null);
//                }
//                getView().onChangePasswordSuccess(message);
//            }
//
//            @Override
//            public void onChangePasswordFailure(String message) {
//                getView().hideProgress();
//                getView().onChangePasswordFailure(message);
//            }
//        });
//    }

    void onGetInfoUser() {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getView().showProgress();
        getAuth().onGetInfoUser(token, new AuthModel.IOnGetInfoUserCallback() {

            @Override
            public void onGetInfoUserSuccess(InfoUserResponse.InfoUser infoUser) {
                getView().hideProgress();
                getView().onGetInfoUserSuccess(infoUser);
            }

            @Override
            public void onGetInfoUserFailure(String message) {
                getView().hideProgress();
                getView().onGetInfoUserFailure(message);
            }
        });
    }

    public void validateInfoUser(final InfoUserResponse.InfoUser infoUser) {
        getAuth().validateInfoUser(infoUser, new AuthModel.IOnValidate() {

            @Override
            public void onSuccess() {
                onUploadImage(infoUser);
            }

            @Override
            public void onFailure(String message) {
                getView().onValidateFailure(message);
            }
        });
    }

    public void onUploadImage(final InfoUserResponse.InfoUser infoUser) {
        getView().showProgress();
        getAuth().uploadImageUser(infoUser, new AuthModel.IOnUploadImage() {

            @Override
            public void onSuccess() {
                onUpdateInfoUser(infoUser);
            }

            @Override
            public void onFailure() {
                onUpdateInfoUser(infoUser);
            }
        });
    }

    void onUpdateInfoUser(InfoUserResponse.InfoUser infoUser) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getAuth().onUpdateInfoUser(token, infoUser, new AuthModel.IOnUpdateInfoUserCallback() {

            @Override
            public void onOnUpdateInfoUserSuccess() {
                getView().hideProgress();
                getView().onOnUpdateInfoUserSuccess();
            }

            @Override
            public void onOnUpdateInfoUserFailure(String message) {
                getView().hideProgress();
                getView().onOnUpdateInfoUserFailure(message);
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
}
