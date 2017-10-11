package jp.co.wssj.iungo.screens.home;

import android.os.Bundle;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.checkin.CheckInModel;
import jp.co.wssj.iungo.model.checkin.CheckInStatusResponse;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.model.util.UtilsModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;
import jp.co.wssj.iungo.screens.waitstoreconfirm.WaitStoreConfirmFragment;
import jp.co.wssj.iungo.utils.Constants;

/**
 * Created by Nguyen Huu Ta on 20/6/2017.
 */

class HomePresenter extends FragmentPresenter<IHomeView> {

    HomePresenter(IHomeView view) {
        super(view);
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new UtilsModel(view.getViewContext()));
        registerModel(new CheckInModel(view.getViewContext()));
    }

    void showFragment() {
        String token = getModel(SharedPreferencesModel.class).getToken();
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
                            getView().displayStampManagerScreen(bundle);
                            break;
                        case Constants.CheckInStatus.STATUS_WAIT_CONFIRM:
                            bundle.putString(WaitStoreConfirmFragment.KEY_STORE_NAME, data.getStoreName());
                            bundle.putString(WaitStoreConfirmFragment.KEY_STATUS_CHECK_IN, data.getStatus());
                            bundle.putInt(WaitStoreConfirmFragment.KEY_NUMBER_PEOPLE, data.getNumberPeople());
                            bundle.putLong(WaitStoreConfirmFragment.KEY_TIME_WAITING, data.getTimeWaiting());
                            bundle.putInt(WaitStoreConfirmFragment.KEY_NUMBER_SESSION, data.getNumberSession());
                            getView().displayWaitStoreConfirmScreen(bundle);
                            break;
                        case Constants.CheckInStatus.STATUS_CANCEL:
                            getView().displayScanQRCodeScreen();
                            break;
                    }
                } else {
                    getView().hideProgress();
                    getView().displayScanQRCodeScreen();
                }
            }

            @Override
            public void onCheckInStatusFailure(ErrorMessage errorMessage) {
                getView().hideProgress();
                getView().displayScanQRCodeScreen();
            }
        });
    }
}
