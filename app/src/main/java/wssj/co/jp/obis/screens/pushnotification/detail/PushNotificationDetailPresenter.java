package wssj.co.jp.obis.screens.pushnotification.detail;

import wssj.co.jp.obis.model.preference.SharedPreferencesModel;
import wssj.co.jp.obis.model.pushnotification.PushNotificationModel;
import wssj.co.jp.obis.screens.base.FragmentPresenter;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationDetailPresenter extends FragmentPresenter<IPushNotificationDetailView> {

    private static final String TAG = "PushNotificationDetailServiceCompanyPresenter";

    protected PushNotificationDetailPresenter(IPushNotificationDetailView view) {
        super(view);
        registerModel(new PushNotificationModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
    }

}
