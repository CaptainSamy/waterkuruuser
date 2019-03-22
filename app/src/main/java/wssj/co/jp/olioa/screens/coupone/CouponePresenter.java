package wssj.co.jp.olioa.screens.coupone;

import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.model.pushnotification.PushNotificationModel;
import wssj.co.jp.olioa.screens.base.FragmentPresenter;

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
