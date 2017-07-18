package jp.co.wssj.iungo.screens.scanner.dialog;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.checkin.CheckInModel;
import jp.co.wssj.iungo.model.checkin.ConfirmCheckInResponse;
import jp.co.wssj.iungo.model.checkin.InfoStoreResponse;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.screens.base.BasePresenter;

/**
 * Created by HieuPT on 5/17/2017.
 */

class ConfirmCheckInPresenter extends BasePresenter<IConfirmCheckInView> {

    ConfirmCheckInPresenter(IConfirmCheckInView view) {
        super(view);
        registerModel(new CheckInModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
    }

    void onCancelButtonClicked() {
        getView().dismissDialogView();
    }

    void onOkButtonClicked(String code) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(CheckInModel.class).feedbackQRStore(code, token, new CheckInModel.IFeedbackQRStoreCallback() {

            @Override
            public void onFeedbackSuccess(ConfirmCheckInResponse.SessionData data) {
                getModel(SharedPreferencesModel.class).putSession(data.getSessionId());
                getModel(SharedPreferencesModel.class).putServiceId(data.getServiceId());
                getModel(SharedPreferencesModel.class).putServiceCompanyId(data.getServiceCompanyId());
                getView().displayWaitStoreConfirmScreen(data);
                getView().dismissDialogView();
            }

            @Override
            public void onFeedbackFailure(ErrorMessage errorMessage) {
                getView().onConfirmFailure(errorMessage.getMessage());
            }
        });
    }

    public void getInfoStoreByCode(final String qrCode) {
        getModel(CheckInModel.class).getInfoStoreByCode(qrCode, new CheckInModel.IGetInfoStoreCallback() {

            @Override
            public void onSuccess(InfoStoreResponse.InfoStoreData data) {
                getView().onGetInfoStoreSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                getView().onGetInfoStoreFailure(message);
            }
        });
    }
}
