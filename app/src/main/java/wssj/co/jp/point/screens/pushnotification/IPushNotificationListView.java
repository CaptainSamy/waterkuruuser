package wssj.co.jp.point.screens.pushnotification;

import java.util.List;

import wssj.co.jp.point.model.ErrorMessage;
import wssj.co.jp.point.model.firebase.NotificationMessage;
import wssj.co.jp.point.screens.base.IFragmentView;

/**
 * Created by tuanle on 6/7/17.
 */

public interface IPushNotificationListView extends IFragmentView {

    void showListPushNotification(List<NotificationMessage> list, int page, int totalPage);

    void displayErrorMessage(ErrorMessage errorMessage);
}
