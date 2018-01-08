package jp.co.wssj.iungo.screens.coupone;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.apache.commons.lang.CharRange;

import jp.co.wssj.iungo.screens.coupone.unused.UnusedCouponeFragment;
import jp.co.wssj.iungo.screens.coupone.used.UsedCouponeFragment;
import jp.co.wssj.iungo.utils.Constants;

/**
 * Created by thang on 1/2/2018.
 */

public class CouponeAdapter extends FragmentPagerAdapter {
    private Context mcontext;

    public CouponeAdapter(FragmentManager fm,Context context) {
        super(fm);
        mcontext=context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position){
            case 0:
                fragment=new UnusedCouponeFragment();
                break;
            case 1:
                fragment=new UsedCouponeFragment();
                break;
            default:
                fragment=new UnusedCouponeFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "お気に入り";
            case 1:
                return "アンケート";
        }
        return "title";
    }

}
