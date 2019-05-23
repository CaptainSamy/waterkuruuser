package wssj.co.jp.obis.screens.scanner.dialog;

import wssj.co.jp.obis.model.checkin.CheckInModel;
import wssj.co.jp.obis.model.preference.SharedPreferencesModel;
import wssj.co.jp.obis.screens.base.BasePresenter;

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

//    void onOkButtonClicked(String code) {
//        getModel(CheckInModel.class).userConfirm(code, new APICallback<Integer>() {
//
//            @Override
//            public void onSuccess(Integer integer) {
//                getView().dismissDialogView();
//            }
//
//            @Override
//            public void onFailure(String errorMessage) {
//                getView().onConfirmFailure(errorMessage);
//            }
//        });
//    }

}
