package wssj.co.jp.olioa.screens.home;

import android.os.Bundle;

import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.model.checkin.CheckInModel;
import wssj.co.jp.olioa.model.checkin.CheckInStatusResponse;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.model.util.UtilsModel;
import wssj.co.jp.olioa.screens.waitstoreconfirm.WaitStoreConfirmFragment;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.screens.switcher.SwitcherPresenter;

/**
 * Created by Nguyen Huu Ta on 20/6/2017.
 */

class HomePresenter extends SwitcherPresenter<IHomeView> {

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
