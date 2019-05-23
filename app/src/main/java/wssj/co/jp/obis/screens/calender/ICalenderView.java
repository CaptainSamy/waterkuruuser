package wssj.co.jp.obis.screens.calender;

import java.util.List;

import wssj.co.jp.obis.model.ErrorMessage;
import wssj.co.jp.obis.model.firebase.NotificationMessage;
import wssj.co.jp.obis.screens.base.IFragmentView;

/**
 * Created by Thanghn on 12/19/2017.
 */

public interface ICalenderView  extends IFragmentView {
    void showListPushNotification(List<NotificationMessage> list, int page, int totalPage);

    void displayErrorMessage(ErrorMessage errorMessage);
}
