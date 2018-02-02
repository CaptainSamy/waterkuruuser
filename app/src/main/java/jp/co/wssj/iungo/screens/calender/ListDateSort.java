package jp.co.wssj.iungo.screens.calender;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.model.firebase.NotificationMessage;

/**
 * Created by Thanghn on 12/21/2017.
 */

public class ListDateSort {

    private int stringDate;

    private List<NotificationMessage> mListNotification;

    public int getStringDate() {
        return stringDate;
    }

    public void setStringDate(int stringDate) {
        this.stringDate = stringDate;
    }

    public List<NotificationMessage> getmListNotification() {
        if (mListNotification==null)mListNotification=new ArrayList<>();
        return mListNotification;
    }

    public void setmListNotification(List<NotificationMessage> mListNotification) {
        this.mListNotification = mListNotification;
    }
}