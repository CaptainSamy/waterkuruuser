package wssj.co.jp.olioa.screens.liststorecheckedin;

import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.model.stamp.ListStoreCheckedResponse;
import wssj.co.jp.olioa.model.stamp.StampModel;
import wssj.co.jp.olioa.model.util.UtilsModel;
import wssj.co.jp.olioa.screens.base.FragmentPresenter;

/**
 * Created by Nguyen Huu Ta on 12/6/2017.
 */

public class ListStoreCheckedInPresenter extends FragmentPresenter<IListStoreCheckedInView> {

    protected ListStoreCheckedInPresenter(IListStoreCheckedInView view) {
        super(view);
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new StampModel(view.getViewContext()));
        registerModel(new UtilsModel(view.getViewContext()));
    }

    public void checkAccessLocationPermission() {
        getModel(UtilsModel.class).checkAccessLocationPermission(new UtilsModel.ICheckSelfPermissionResultCallback() {

            @Override
            public void onPermissionGranted() {
                getView().onAllowAccessLocation();
            }

            @Override
            public void onPermissionDenied() {
                getView().onRequestLocationPermission();
            }
        });
    }

    public void getListStoreCheckedIn(int serviceCompanyId, double latitude, double longitude) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getView().showProgress();
        getModel(StampModel.class).getListStoreCheckedIn(token, serviceCompanyId, latitude, longitude, new StampModel.IGetListStoreCheckedResponse() {

            @Override
            public void onSuccess(ListStoreCheckedResponse.ListStoreData data) {
                getView().hideProgress();
                getView().onGetListStoreCheckedInSuccess(data.getListStore());
            }

            @Override
            public void onFailure(ErrorMessage errorMessage) {
                getView().hideProgress();
                getView().onGetListStoreCheckedInFailure(errorMessage.getMessage());
            }
        });
    }
}
