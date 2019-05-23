package wssj.co.jp.obis.screens.pushnotification.pushlist;

import wssj.co.jp.obis.model.ErrorMessage;
import wssj.co.jp.obis.model.pushnotification.PushNotificationResponse;
import wssj.co.jp.obis.screens.base.IFragmentView;

/**
 * Created by tuanle on 6/7/17.
 */

public interface IPushNotificationListView extends IFragmentView {

    void showListPushNotification(PushNotificationResponse response);

    void displayErrorMessage(ErrorMessage errorMessage);
}
