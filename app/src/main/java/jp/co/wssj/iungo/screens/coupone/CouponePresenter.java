package jp.co.wssj.iungo.screens.coupone;

import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.model.pushnotification.PushNotificationModel;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;

/**
 * Created by thang on 12/29/2017.
 */

public class CouponePresenter extends FragmentPresenter<ICouponeView>{

    protected CouponePresenter(ICouponeView view) {
        super(view);
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new PushNotificationModel(view.getViewContext()));
    }
}
