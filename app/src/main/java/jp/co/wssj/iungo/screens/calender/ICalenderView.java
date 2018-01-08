package jp.co.wssj.iungo.screens.calender;

import java.util.List;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by Thanghn on 12/19/2017.
 */

public interface ICalenderView  extends IFragmentView {
    void showListPushNotification(List<NotificationMessage> list, int page, int totalPage);

    void displayErrorMessage(ErrorMessage errorMessage);
}
