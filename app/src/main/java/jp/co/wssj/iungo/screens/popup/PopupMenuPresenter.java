package jp.co.wssj.iungo.screens.popup;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.screens.base.BasePresenter;
import jp.co.wssj.iungo.utils.Logger;

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
