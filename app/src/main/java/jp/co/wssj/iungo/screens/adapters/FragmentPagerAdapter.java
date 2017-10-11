package jp.co.wssj.iungo.screens.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import jp.co.wssj.iungo.screens.base.PagedFragment;
import jp.co.wssj.iungo.utils.Utils;

/**
 * Created by HieuPT on 6/5/2017.
 */

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    private final List<PagedFragment> mFragments;

    private final Context mContext;

    public FragmentPagerAdapter(@NonNull Context context, FragmentManager fm, List<PagedFragment> fragments) {
        super(fm);
        Utils.requireNonNull(context, "Context must not be null");
        mContext = context;
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getPageTitle(mContext);
    }
}
