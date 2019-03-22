package wssj.co.jp.olioa.screens.waitstoreconfirm;

import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.model.checkin.CheckInModel;
import wssj.co.jp.olioa.model.checkin.CheckInStatusResponse;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.utils.Logger;
import wssj.co.jp.olioa.screens.base.FragmentPresenter;

/**
 * Created by HieuPT on 5/19/2017.
 */

class WaitStoreConfirmPresenter extends FragmentPresenter<IWaitStoreConfirmView> {

    WaitStoreConfirmPresenter(IWaitStoreConfirmView view) {
        super(view);
        registerModel(new CheckInModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
    }

    @Override
    protected void onFragmentDestroyView() {
        super.onFragmentDestroyView();
        getView().stopCheckingStatus();
    }

    void getCheckInStatus() {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(CheckInModel.class).getCheckInStatus(token,
                new CheckInModel.IGetCheckInStatusCallback() {

                    @Override
                    public void onCheckInStatusSuccess(CheckInStatusResponse.CheckInStatusData data) {
                        if (isViewAttached() && data != null) {
                            switch (data.getStatus()) {
                                case Constants.CheckInStatus.STATUS_CHECKED_IN:
                                    getView().displayScreenManageStamp(data.getServiceName());
                                    break;
                                case Constants.CheckInStatus.STATUS_WAIT_CONFIRM:
                                    getView().recheckStatus(Constants.TIME_DELAYS, data);
                                    break;
                                case Constants.CheckInStatus.STATUS_CANCEL:
                                    getView().displayScreenScanCode();
                                    break;
                            }
                        } else {
                            Logger.d("onCheckInStatusSuccess", "data null");
                            if (isViewAttached()) {
                                getView().displayScreenManageStamp(Constants.EMPTY_STRING);
                            }
                        }
                    }

                    @Override
                    public void onCheckInStatusFailure(ErrorMessage errorMessage) {
                        if (isViewAttached() && errorMessage != null && errorMessage.getMessage().equals(Constants.CheckInStatus.STATUS_CHECKED_OUT)) {
                            Logger.d("onCheckInStatusFailure", "data null");
                            getView().displayScreenScanCode();
                        } else {
                            getView().recheckStatus(Constants.TIME_DELAYS, null);
                        }
                    }
                });
    }


}
