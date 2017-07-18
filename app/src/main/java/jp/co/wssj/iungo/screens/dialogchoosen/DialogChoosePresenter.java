package jp.co.wssj.iungo.screens.dialogchoosen;

import jp.co.wssj.iungo.screens.base.BasePresenter;

/**
 * Created by Nguyen Huu Ta on 7/6/2017.
 */

class DialogChoosePresenter extends BasePresenter<IDialogChooseView> {

    DialogChoosePresenter(IDialogChooseView view) {
        super(view);
    }

    void onTakePhotoButtonClicked() {
        getView().onTakePhotoButtonClicked();
        getView().dismissDialogView();
    }

    void onPickFromGalerryButtonClicked() {
        getView().onPickFromGalerryClicked();
        getView().dismissDialogView();
    }

    void onCancelButtonClicked() {
        getView().dismissDialogView();
    }
}
