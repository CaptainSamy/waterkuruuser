package jp.co.wssj.iungo.screens.pushnotification.pushpagecontainer;

import android.os.Bundle;

import jp.co.wssj.iungo.screens.IMainView;

/**
 * Created by Nguyen Huu Ta on 18/10/2017.
 */

public class PushNotificationPageNavigationFragment extends PushNotificationPageFragment {

    public static PushNotificationPageNavigationFragment newInstance(Bundle args) {
        PushNotificationPageNavigationFragment fragment = new PushNotificationPageNavigationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_PUSH_NOTIFICATION_PAGER_NAVIGATION;
    }

    @Override
    protected String getLogTag() {
        return "PushNotificationPageNavigationFragment";
    }
}
