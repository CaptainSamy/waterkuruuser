package jp.co.wssj.iungo.screens.pushnotification.pushpagecontainer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import jp.co.wssj.iungo.screens.pushnotification.pushlike.PushLikeFragment;
import jp.co.wssj.iungo.screens.pushnotification.pushlist.PushNotificationFragment;
import jp.co.wssj.iungo.screens.pushnotification.pushquestionnaire.PushTypeQuestionNaireFragment;
import jp.co.wssj.iungo.screens.pushnotificationforstore.PushNotificationForStoreAnnounce;
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

    public PushNotificationPageAdapter(FragmentManager fm, Context context, int serviceCompanyId) {
        super(fm);
        mContext = context;
        mServiceCompanyId = serviceCompanyId;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                if (mServiceCompanyId != 0) {
                    Bundle args = new Bundle();
                    args.putInt(Constants.KEY_SERVICE_COMPANY_ID, mServiceCompanyId);
                    args.putInt(ARG_TYPE_PUSH, Constants.TypePush.TYPE_PUSH_ANNOUNCE);
                    fragment = PushNotificationForStoreAnnounce.newInstance(args);
                } else {
                    fragment = new PushNotificationFragment();
                }
                break;
            case 1:
                fragment = new PushLikeFragment();
                break;
            case 2:
                fragment = new PushTypeQuestionNaireFragment();
                break;
            default:
                fragment = PushNotificationForStoreAnnounce.newInstance(null);
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
            case Constants.TypePush.TYPE_LIKED_PUSH:
                return "お気に入り";
            case Constants.TypePush.TYPE_QUESTION_NAIRE_PUSH:
                return "アンケート";
        }
        return "すべて";
    }
}