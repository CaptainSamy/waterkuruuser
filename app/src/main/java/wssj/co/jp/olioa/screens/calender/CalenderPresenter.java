package wssj.co.jp.olioa.screens.calender;

import java.util.List;

import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.model.firebase.NotificationMessage;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.model.pushnotification.PushNotificationModel;
import wssj.co.jp.olioa.screens.base.FragmentPresenter;
import wssj.co.jp.olioa.utils.Constants;

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