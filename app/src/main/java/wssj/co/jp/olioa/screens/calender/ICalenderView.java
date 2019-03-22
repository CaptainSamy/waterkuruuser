package wssj.co.jp.olioa.screens.calender;

import java.util.List;

import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.model.firebase.NotificationMessage;
import wssj.co.jp.olioa.screens.base.IFragmentView;

/**
 * Created by Thanghn on 12/19/2017.
 */

public interface ICalenderView  extends IFragmentView {
    void showListPushNotification(List<NotificationMessage> list, int page, int totalPage);

    void displayErrorMessage(ErrorMessage errorMessage);
}
