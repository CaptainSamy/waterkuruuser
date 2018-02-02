package jp.co.wssj.iungo.screens.calender;

import java.util.List;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.model.pushnotification.PushNotificationModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;
import jp.co.wssj.iungo.utils.Constants;

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
            getModel(PushNotificationModel.class).getListPushNotification(token, userPushId, isSearch, keySearch, 0, Constants.TypePush.TYPE_ALL_PUSH,fromDate,toDate,
                    new PushNotificationModel.IGetListPushNotificationCallback() {

                        @Override
                        public void onGetListPushNotificationSuccess(List<NotificationMessage> list, int page, int totalPage) {
                            getView().showListPushNotification(list, page, totalPage);
                        }

                        @Override
                        public void onGetListPushNotificationFailure(ErrorMessage errorMessage) {
                            getView().displayErrorMessage(errorMessage);
                        }
                    });
        }
    }
