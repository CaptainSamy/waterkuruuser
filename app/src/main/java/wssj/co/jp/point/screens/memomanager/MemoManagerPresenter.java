package wssj.co.jp.point.screens.memomanager;

import android.graphics.drawable.Drawable;

import wssj.co.jp.point.model.entities.StatusMemoData;
import wssj.co.jp.point.model.memo.ListServiceResponse;
import wssj.co.jp.point.model.memo.MemoDynamicResponse;
import wssj.co.jp.point.model.memo.UserMemoModel;
import wssj.co.jp.point.model.memo.UserMemoResponse;
import wssj.co.jp.point.model.preference.SharedPreferencesModel;
import wssj.co.jp.point.model.util.UtilsModel;
import wssj.co.jp.point.screens.base.FragmentPresenter;

/**
 * Created by HieuPT on 17/5/2017.
 */

class MemoManagerPresenter extends FragmentPresenter<IMemoManagerView> {

    MemoManagerPresenter(IMemoManagerView view) {
        super(view);
        registerModel(new UserMemoModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new UtilsModel(view.getViewContext()));
    }

    void getUserMemoByServiceId(int serviceId) {
        getView().showProgress();
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(UserMemoModel.class).getUserMemo(token, serviceId, new UserMemoModel.IUserMemoCallback() {

            @Override
            public void onGetUserMemoSuccess(UserMemoResponse.UserMemoData data) {
                getView().hideProgress();
                if (data != null) {
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

    void getListService() {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getView().showProgress();
        getModel(UserMemoModel.class).getListService(token, new UserMemoModel.IGetListServiceCallback() {

            @Override
            public void onGetListServiceSuccess(ListServiceResponse.ServiceData data) {
                getView().hideProgress();
                getView().onGetListServicesSuccess(data);
            }

            @Override
            public void onGetListServiceFailure(String message) {
                getView().hideProgress();
                getView().onGetListServicesFailure(message);
            }
        });
    }

    public void getMemoConfigByServiceId(int serviceId) {
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

    void onImageViewClicked(Drawable drawable, int requestCodePicker, int requestCodeCamera) {
        if (drawable != null) {
            getView().showPhotoDialog(drawable, requestCodePicker);
        } else {
            getView().showDialogChoose(requestCodePicker, requestCodeCamera);
        }
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

    void onPickPhotoFromGalleryButtonClicked(int requestCode) {
        checkWriteExternalStoragePermission(requestCode);
    }

    void onPickPhotoFromCameraButtonClicked(int requestCode) {
        checkCameraAndWriteExternalStoragePermission(requestCode);
    }
}
