package wssj.co.jp.point.screens.waitstoreconfirm;

import wssj.co.jp.point.model.ErrorMessage;
import wssj.co.jp.point.model.checkin.CheckInModel;
import wssj.co.jp.point.model.checkin.CheckInStatusResponse;
import wssj.co.jp.point.model.preference.SharedPreferencesModel;
import wssj.co.jp.point.screens.base.FragmentPresenter;
import wssj.co.jp.point.utils.Constants;

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
        final int serviceId = getModel(SharedPreferencesModel.class).getServiceId();
        getModel(CheckInModel.class).getCheckInStatus(token,
                new CheckInModel.IGetCheckInStatusCallback() {

                    @Override
                    public void onCheckInStatusSuccess(CheckInStatusResponse.CheckInStatusData data) {
                        if (isViewAttached() && data != null) {
                            if (Constants.CheckInStatus.STATUS_CHECKED_IN.equals(data.getStatus())) {
                                getView().displayScreenManageStamp(serviceId);
                            } else {
                                getView().recheckStatus(Constants.TIME_DELAYS, data);
                            }
                        } else {
                            if (isViewAttached()) {
                                getView().backToPreviousScreen();
                            }
                        }
                    }

                    @Override
                    public void onCheckInStatusFailure(ErrorMessage errorMessage) {
                        if (isViewAttached() && errorMessage != null && errorMessage.getMessage().equals(Constants.CheckInStatus.STATUS_CANCEL_OR_CHECKED_OUT)) {
                            getView().displayScreenScanCode();
                        } else {
                            getView().recheckStatus(Constants.TIME_DELAYS, null);
                        }
                    }
                });
    }


}
