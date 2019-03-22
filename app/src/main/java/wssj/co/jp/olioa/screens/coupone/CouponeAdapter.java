package wssj.co.jp.olioa.screens.coupone;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import wssj.co.jp.olioa.screens.coupone.unused.UnusedCouponeFragment;
import wssj.co.jp.olioa.screens.coupone.used.UsedCouponeFragment;

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
