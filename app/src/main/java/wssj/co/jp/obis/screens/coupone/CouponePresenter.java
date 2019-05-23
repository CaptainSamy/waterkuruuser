package wssj.co.jp.obis.screens.coupone;

import wssj.co.jp.obis.model.preference.SharedPreferencesModel;
import wssj.co.jp.obis.model.pushnotification.PushNotificationModel;
import wssj.co.jp.obis.screens.base.FragmentPresenter;

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
