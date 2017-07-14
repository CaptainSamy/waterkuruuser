package wssj.co.jp.point.screens.home;

import android.os.Bundle;
import android.text.TextUtils;

import wssj.co.jp.point.model.ErrorMessage;
import wssj.co.jp.point.model.checkin.CheckInModel;
import wssj.co.jp.point.model.checkin.CheckInStatusResponse;
import wssj.co.jp.point.model.preference.SharedPreferencesModel;
import wssj.co.jp.point.model.util.UtilsModel;
import wssj.co.jp.point.screens.IMainView;
import wssj.co.jp.point.screens.base.FragmentPresenter;
import wssj.co.jp.point.screens.waitstoreconfirm.WaitStoreConfirmFragment;
import wssj.co.jp.point.utils.Constants;

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
            getModel(CheckInModel.class).getCheckInStatus(token, new CheckInModel.IGetCheckInStatusCallback() {

                @Override
                public void onCheckInStatusSuccess(CheckInStatusResponse.CheckInStatusData data) {
                    if (data != null) {
                        Bundle bundle = new Bundle();
                        getModel(SharedPreferencesModel.class).putServiceId(data.getServiceId());
                        getModel(SharedPreferencesModel.class).putServiceCompanyId(data.getServiceCompanyId());

                        switch (data.getStatus()) {
                            case Constants.CheckInStatus.STATUS_CHECKED_IN:
                                bundle.putString(WaitStoreConfirmFragment.KEY_STORE_NAME, data.getStoreName());
                                getView().switchScreen(IMainView.FRAGMENT_MANAGER_STAMP, true, false, bundle);
                                break;
                            case Constants.CheckInStatus.STATUS_WAIT_CONFIRM:
                                bundle.putString(WaitStoreConfirmFragment.KEY_STORE_NAME, data.getStoreName());
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
                        displayScannerCode();
                    }
                }

                @Override
                public void onCheckInStatusFailure(ErrorMessage errorMessage) {
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
