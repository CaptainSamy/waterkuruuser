package wssj.co.jp.obis.screens.calender;

import wssj.co.jp.obis.model.preference.SharedPreferencesModel;
import wssj.co.jp.obis.model.pushnotification.PushNotificationModel;
import wssj.co.jp.obis.screens.base.FragmentPresenter;

/**
 * Created by Thanghn on 12/19/2017.
 */

public class CalenderPresenter extends FragmentPresenter<ICalenderView> {

    protected CalenderPresenter(ICalenderView view) {
        super(view);
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new PushNotificationModel(view.getViewContext()));
    }

    public void getListPushNotificationForCalendar(long userPushId, int isSearch, String keySearch, String fromDate, String toDate) {
        String token = getModel(SharedPreferencesModel.class).getToken();
    }
}