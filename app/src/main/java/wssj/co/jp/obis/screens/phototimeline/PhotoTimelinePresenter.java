package wssj.co.jp.obis.screens.phototimeline;

import wssj.co.jp.obis.screens.base.BasePresenter;

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
