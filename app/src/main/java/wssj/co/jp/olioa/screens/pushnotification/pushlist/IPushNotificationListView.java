package wssj.co.jp.olioa.screens.pushnotification.pushlist;

import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.model.pushnotification.PushNotificationResponse;
import wssj.co.jp.olioa.screens.base.IFragmentView;

/**
 * Created by tuanle on 6/7/17.
 */

public interface IPushNotificationListView extends IFragmentView {

    void showListPushNotification(PushNotificationResponse response);

    void displayErrorMessage(ErrorMessage errorMessage);
}
