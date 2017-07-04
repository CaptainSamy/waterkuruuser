package wssj.co.jp.point.screens.popup;

import wssj.co.jp.point.R;
import wssj.co.jp.point.model.preference.SharedPreferencesModel;
import wssj.co.jp.point.screens.base.BasePresenter;
import wssj.co.jp.point.utils.Logger;

/**
 * Created by HieuPT on 5/24/2017.
 */

class PopupMenuPresenter extends BasePresenter<IPopupMenuView> {

    private static final String TAG = "PopupMenuPresenter";

    PopupMenuPresenter(IPopupMenuView view) {
        super(view);
        registerModel(new SharedPreferencesModel(view.getViewContext()));
    }

    void inflateMenu() {
        Logger.d(TAG, "#inflateMenu");
        getView().inflateMenu(R.id.menu_logout);
    }
}
