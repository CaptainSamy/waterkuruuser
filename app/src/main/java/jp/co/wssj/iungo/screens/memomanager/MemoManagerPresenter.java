package jp.co.wssj.iungo.screens.memomanager;

import android.graphics.drawable.Drawable;

import java.util.List;

import jp.co.wssj.iungo.model.memo.ListServiceResponse;
import jp.co.wssj.iungo.model.memo.MemoDynamicResponse;
import jp.co.wssj.iungo.model.memo.UserMemoModel;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.model.util.UtilsModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;

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

    int onGetLastServiceId() {
        return getModel(SharedPreferencesModel.class).getLastServiceId();
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
        getModel(SharedPreferencesModel.class).putLastServiceId(serviceId);
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
