package jp.co.wssj.iungo.screens.home;

import android.os.Bundle;
import android.text.TextUtils;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.checkin.CheckInModel;
import jp.co.wssj.iungo.model.checkin.CheckInStatusResponse;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.model.util.UtilsModel;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;
import jp.co.wssj.iungo.screens.waitstoreconfirm.WaitStoreConfirmFragment;
import jp.co.wssj.iungo.utils.Constants;

/**
 * Created by Nguyen Huu Ta on 20/6/2017.
 */

public class HomePresenter extends FragmentPresenter<IHomeView> {

    public HomePresenter(IHomeView view) {
        super(view);
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new UtilsModel(view.getViewContext()));
        registerModel(new CheckInModel(view.getViewContext()));

    }

    void onHomeNavigationButtonClicked() {
        String token = getModel(SharedPreferencesModel.class).getToken();
        if (!TextUtils.isEmpty(token)) {
            getView().showProgress();
            getModel(CheckInModel.class).getCheckInStatus(token, new CheckInModel.IGetCheckInStatusCallback() {

                @Override
                public void onCheckInStatusSuccess(CheckInStatusResponse.CheckInStatusData data) {
                    getView().hideProgress();
                    if (data != null) {
                        Bundle bundle = new Bundle();
                        getModel(SharedPreferencesModel.class).putServiceId(data.getServiceId());
                        getModel(SharedPreferencesModel.class).putServiceCompanyId(data.getServiceCompanyId());
                        switch (data.getStatus()) {
                            case Constants.CheckInStatus.STATUS_CHECKED_IN:
                                bundle.putString(WaitStoreConfirmFragment.KEY_STORE_NAME, data.getStoreName());
                                bundle.putString(WaitStoreConfirmFragment.KEY_SERVICE_NAME, data.getServiceName());
                                getView().switchScreen(IMainView.FRAGMENT_MANAGER_STAMP, true, false, bundle);
                                break;
                            case Constants.CheckInStatus.STATUS_WAIT_CONFIRM:
                                bundle.putString(WaitStoreConfirmFragment.KEY_STORE_NAME, data.getStoreName());
                                bundle.putString(WaitStoreConfirmFragment.KEY_STATUS_CHECK_IN, data.getStatus());
                                bundle.putInt(WaitStoreConfirmFragment.KEY_NUMBER_PEOPLE, data.getNumberPeople());
                                bundle.putLong(WaitStoreConfirmFragment.KEY_TIME_WAITING, data.getTimeWaiting());
                                bundle.putInt(WaitStoreConfirmFragment.KEY_NUMBER_SESSION, data.getNumberSession());
                                getView().switchScreen(IMainView.FRAGMENT_WAIT_STORE_CONFIRM, true, false, bundle);
                                break;
                            case Constants.CheckInStatus.STATUS_CANCEL:
                                displayScannerCode();
                                break;
                        }
                    } else {
                        getView().hideProgress();
                        displayScannerCode();
                    }
                }

                @Override
                public void onCheckInStatusFailure(ErrorMessage errorMessage) {
                    getView().hideProgress();
                    displayScannerCode();
                }
            });
        }
    }

    private void displayScannerCode() {
        getModel(UtilsModel.class).checkCameraPermission(new UtilsModel.ICheckSelfPermissionResultCallback() {

            @Override
            public void onPermissionGranted() {
                getView().switchScreen(IMainView.FRAGMENT_SCANNER, true, false, null);
            }

            @Override
            public void onPermissionDenied() {
                getView().requestCameraPermission();
            }
        });
    }
}
