package wssj.co.jp.olioa.screens.pushnotification.pushpagecontainer;

import android.os.Bundle;

import wssj.co.jp.olioa.screens.IMainView;

/**
 * Created by Nguyen Huu Ta on 18/10/2017.
 */

public class PushNotificationPageNavigationFragment extends PushNotificationPageFragment {

    public static PushNotificationPageNavigationFragment newInstance(Bundle args) {
        PushNotificationPageNavigationFragment fragment = new PushNotificationPageNavigationFragment();
        fragment.setArguments(args);
        return fragment;
    }
//    @Override
//    public String getAppBarTitle() {
//        return getString(R.string.title_push_notification_list);
//    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_PUSH_NOTIFICATION_PAGER_NAVIGATION;
    }

    @Override
    protected String getLogTag() {
        return "PushNotificationPageNavigationFragment";
    }
}
