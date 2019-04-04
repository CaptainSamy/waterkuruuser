package wssj.co.jp.olioa.screens.changepassword;

import android.text.TextUtils;

import wssj.co.jp.olioa.model.auth.AuthModel;
import wssj.co.jp.olioa.model.auth.RegisterData;
import wssj.co.jp.olioa.model.baseapi.APICallback;
import wssj.co.jp.olioa.model.entities.UserResponse;
import wssj.co.jp.olioa.model.firebase.FirebaseModel;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.model.util.UtilsModel;
import wssj.co.jp.olioa.screens.base.FragmentPresenter;

/**
 * Created by HieuPT on 5/15/2017.
 */

class ChangeUserInfoPresenter extends FragmentPresenter<IChangeUserInfoView> {

    ChangeUserInfoPresenter(IChangeUserInfoView view) {
        super(view);
        registerModel(new AuthModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new FirebaseModel(view.getViewContext()));
        registerModel(new UtilsModel(view.getViewContext()));
    }

    private AuthModel getAuth() {
        return getModel(AuthModel.class);
    }

    private UtilsModel getUtils() {
        return getModel(UtilsModel.class);
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

    void onGetInfoUser() {
        getView().showProgress();
        getAuth().onGetInfoUser(new APICallback<UserResponse>() {

            @Override
            public void onSuccess(UserResponse userResponse) {
                getView().hideProgress();
                getView().onGetInfoUserSuccess(userResponse);
            }

            @Override
            public void onFailure(String errorMessage) {
                getView().hideProgress();
                getView().showDialog(errorMessage);
            }
        });
    }

    void onUpdateInfoUser(final UserResponse infoUser) {
        getView().showProgress();
        String message = getAuth().validateInfoUser(infoUser);
        if (message.isEmpty()) {
            String newAvatar = infoUser.getNewAvatar();
            if (TextUtils.isEmpty(newAvatar)) {
                requestUpdate(infoUser);
            } else {
                getUtils().uploadImage(newAvatar, new APICallback<String>() {

                    @Override
                    public void onSuccess(String s) {
                        infoUser.setAvatar(s);
                        requestUpdate(infoUser);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        requestUpdate(infoUser);
                    }
                });
            }
        } else {
            getView().showDialog(message);
        }

    }

    private void requestUpdate(UserResponse infoUser) {

        getAuth().onUpdateInfoUser(infoUser, new APICallback() {

            @Override
            public void onSuccess(Object o) {
                getView().onChangePasswordSuccess("");
            }

            @Override
            public void onFailure(String errorMessage) {
                getView().showDialog(errorMessage);
            }
        });
    }

    void onImageViewClicked(int requestCodePicker, int requestCodeCamera) {
        getView().showDialogChoose(requestCodePicker, requestCodeCamera);
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
