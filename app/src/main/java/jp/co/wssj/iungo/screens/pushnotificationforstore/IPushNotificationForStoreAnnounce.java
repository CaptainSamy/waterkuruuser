package jp.co.wssj.iungo.screens.pushnotificationforstore;

import java.util.List;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by tuanle on 6/7/17.
 */

public interface IPushNotificationForStoreAnnounce extends IFragmentView {

    void showListPushNotification(List<NotificationMessage> list, int page, int totalPage);

    void displayErrorMessage(ErrorMessage errorMessage);
}
