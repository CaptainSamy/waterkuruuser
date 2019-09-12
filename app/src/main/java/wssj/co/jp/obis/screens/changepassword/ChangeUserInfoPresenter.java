package wssj.co.jp.obis.screens.changepassword;

import android.text.TextUtils;

import wssj.co.jp.obis.model.auth.AuthModel;
import wssj.co.jp.obis.model.baseapi.APICallback;
import wssj.co.jp.obis.model.baseapi.APIService;
import wssj.co.jp.obis.model.entities.AccessToken;
import wssj.co.jp.obis.model.entities.UserResponse;
import wssj.co.jp.obis.model.firebase.FirebaseModel;
import wssj.co.jp.obis.model.preference.SharedPreferencesModel;
import wssj.co.jp.obis.model.util.UtilsModel;
import wssj.co.jp.obis.screens.base.FragmentPresenter;
import wssj.co.jp.obis.utils.Constants;

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


    void autoRegister(final String username, final String password, final UserResponse infoUse) {
        String message = getAuth().validateInfoUser(infoUse);
        if (!message.isEmpty()) {
            getView().showDialog(message);
            return;
        }
        register(username, password, infoUse);

//        if (TextUtils.isEmpty(newAvatar)) {
//            register(username, password, infoUse);
//        } else {
//            APIService.getInstance().addAuthorizationHeader(Constants.TOKEN_UPLOAD_IMAGE);
//            getUtils().uploadImage(newAvatar, new APICallback<String>() {
//
//                @Override
//                public void onSuccess(String s) {
//                    infoUse.setAvatar(s);
//                    register(username, password, infoUse);
//                }
//
//                @Override
//                public void onFailure(String errorMessage) {
//                    register(username, password, infoUse);
//                }
//            });
//        }


    }

    private void register(final String username, final String password, final UserResponse infoUse) {
        getView().showProgress();
        getAuth().registerAccount(username, password, infoUse, new APICallback<AccessToken>() {
            @Override
            public void onSuccess(AccessToken accessToken) {
                String token = "Bearer " + accessToken.getAccessToken();
                APIService.getInstance().addAuthorizationHeader(token);
                getModel(SharedPreferencesModel.class).putToken(token);
                getModel(SharedPreferencesModel.class).putExpireDate(accessToken.getExpired());
                getModel(SharedPreferencesModel.class).putUserName(username);
                getModel(SharedPreferencesModel.class).putPassword(password);
                getModel(SharedPreferencesModel.class).putEmail(infoUse.getEmail());
                getModel(FirebaseModel.class).uploadDeviceToken(token, null);
                if (TextUtils.isEmpty(infoUse.getNewAvatar())) {
                    getView().hideProgress();
                    getView().onRegisterSuccess();
                } else {
                    uploadImage(infoUse);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                getView().hideProgress();
                getView().showDialog(errorMessage);
            }
        });
    }

    private void uploadImage(final UserResponse infoUse) {
        getUtils().uploadImage(infoUse.getNewAvatar(), new APICallback<String>() {

            @Override
            public void onSuccess(String s) {
                infoUse.setAvatar(s);
                requestUpdate(infoUse, true);
            }

            @Override
            public void onFailure(String errorMessage) {
                getView().hideProgress();
                getView().onRegisterSuccess();
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
        String message = getAuth().validateInfoUser(infoUser);
        if (message.isEmpty()) {
            getView().showProgress();
            String newAvatar = infoUser.getNewAvatar();
            if (TextUtils.isEmpty(newAvatar)) {
                requestUpdate(infoUser, false);
            } else {
                getUtils().uploadImage(newAvatar, new APICallback<String>() {

                    @Override
                    public void onSuccess(String s) {
                        infoUser.setAvatar(s);
                        requestUpdate(infoUser, false);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        requestUpdate(infoUser, false);
                    }
                });
            }
        } else {
            getView().showDialog(message);
        }

    }

    private void requestUpdate(UserResponse infoUser, final boolean isFromRegister) {
        getAuth().onUpdateInfoUser(infoUser, new APICallback() {

            @Override
            public void onSuccess(Object o) {
                getView().hideProgress();
                if (isFromRegister) {
                    getView().onRegisterSuccess();
                } else {
                    getView().onChangePasswordSuccess("更新しました。");
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                getView().hideProgress();
                if (isFromRegister) {
                    getView().onRegisterSuccess();
                } else {
                    getView().showDialog(errorMessage);
                }


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
