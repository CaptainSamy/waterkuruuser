package jp.co.wssj.iungo.screens.phototimeline;

import jp.co.wssj.iungo.screens.base.BasePresenter;

/**
 * Created by Nguyen Huu Ta on 19/5/2017.
 */

class PhotoTimelinePresenter extends BasePresenter<IPhotoTimelineView> {

    PhotoTimelinePresenter(IPhotoTimelineView view) {
        super(view);
    }

    void dismissDialog() {
        getView().dismissDialogView();
    }
}
