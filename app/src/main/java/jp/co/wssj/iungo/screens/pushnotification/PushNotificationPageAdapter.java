package jp.co.wssj.iungo.screens.pushnotification;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import jp.co.wssj.iungo.screens.pushnotification.pushlist.PushNotificationFragment;
import jp.co.wssj.iungo.screens.pushnotificationforstore.PushNotificationForServiceCompanyFragment;
import jp.co.wssj.iungo.utils.Constants;

/**
 * Created by tuanle on 10/16/17.
 */

public class PushNotificationPageAdapter extends FragmentPagerAdapter {

    private Context mContext;

    /*
    * true : push store anouce
    * false : push normal
    * */
    private int mServiceCompanyId;

    public static final String ARG_TYPE_PUSH = "arg_type_push_list";

    public static final int TYPE_ALL_PUSH = 0;

    public static final int TYPE_LIKED_PUSH = 1;

    public static final int TYPE_QUESTION_NAIRE_PUSH = 2;

    public static final int TYPE_PUSH_ANNOUNCE = 3;

    private Bundle bundle = new Bundle();

    public PushNotificationPageAdapter(FragmentManager fm, Context context, int serviceCompanyId) {
        super(fm);
        mContext = context;
        mServiceCompanyId = serviceCompanyId;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if (mServiceCompanyId != 0) {
            switch (position) {
                case 0:
                    Bundle args = new Bundle();
                    args.putInt(Constants.KEY_SERVICE_COMPANY_ID, mServiceCompanyId);
                    args.putInt(ARG_TYPE_PUSH, TYPE_PUSH_ANNOUNCE);
                    fragment = PushNotificationForServiceCompanyFragment.newInstance(args);
                    break;
                case 1:
                case 2:
                    fragment = new PushNotificationFragment();
                    Bundle args1 = new Bundle();
                    args1.putInt(ARG_TYPE_PUSH, position);
                    fragment.setArguments(args1);
                    break;
                default:
                    fragment = PushNotificationForServiceCompanyFragment.newInstance(null);
            }

        } else {
            fragment = new PushNotificationFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_TYPE_PUSH, position);
            fragment.setArguments(args);
        }

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
            case TYPE_QUESTION_NAIRE_PUSH:
                return "アンケート";
        }
        return "すべて";
    }
}