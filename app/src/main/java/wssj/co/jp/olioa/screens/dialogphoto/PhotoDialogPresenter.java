package wssj.co.jp.olioa.screens.dialogphoto;

import wssj.co.jp.olioa.screens.base.BasePresenter;

/**
 * Created by Nguyen Huu Ta on 19/5/2017.
 */

class PhotoDialogPresenter extends BasePresenter<IPhotoDialogView> {

    PhotoDialogPresenter(IPhotoDialogView view) {
        super(view);
    }

    void dismissDialog() {
        getView().dismissDialogView();
    }
}
