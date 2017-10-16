package jp.co.wssj.iungo.screens.pushnotification;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import jp.co.wssj.iungo.screens.pushnotification.pushlist.PushNotificationListFragment;

/**
 * Created by tuanle on 10/16/17.
 */

public class PushNotificationPageAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public static final String ARG_TYPE_PUSH = "arg_type_push_list";
    public static final int TYPE_ALL_PUSH = 0;
    public static final int TYPE_LIKED_PUSH = 1;
    public static final int TYPE_QUESTIONAIRE_PUSH = 2;

    public PushNotificationPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = new PushNotificationListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case TYPE_LIKED_PUSH:
                return "お気に入り";
            case TYPE_QUESTIONAIRE_PUSH:
                return "アンケート";
        }
        return "すべて";
    }
}