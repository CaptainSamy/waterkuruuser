package wssj.co.jp.obis.screens.switcher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import wssj.co.jp.obis.R;
import wssj.co.jp.obis.screens.base.BaseFragment;
import wssj.co.jp.obis.utils.FragmentBackStackManager;

/**
 * Created by HieuPT on 10/17/2017.
 */

public abstract class SwitcherFragment<V extends ISwitcherView, P extends SwitcherPresenter<V>> extends BaseFragment<V, P>
        implements ISwitcherView {

    private FragmentBackStackManager mFragmentBackStackManager;

    private BaseFragment mCurrentFragment;

    private View mProgressLayout;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_switcher;
    }

    @Override
    public String getAppBarTitle() {
        if (mCurrentFragment != null) {
            return mCurrentFragment.getAppBarTitle();
        }
        return super.getAppBarTitle();
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (mCurrentFragment != null) {
            getActivityCallback().setAppBarTitle(mCurrentFragment.getAppBarTitle());
        }
    }

    @Override
    protected void initViews(View rootView) {
        mFragmentBackStackManager = new FragmentBackStackManager(getChildFragmentManager(), R.id.fragment_container);
        mProgressLayout = findViewById(rootView, R.id.progress_layout);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCurrentFragment != null) {
            mCurrentFragment.setUserVisibleHint(getUserVisibleHint());
        }
    }

    @Override
    public void showProgress() {
        mProgressLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressLayout.setVisibility(View.GONE);
    }

    @Override
    public void displayFragment(BaseFragment fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            mFragmentBackStackManager.replaceFragment(fragment, true, false);
        }
    }

    protected Bundle createBaseBundle() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_SET_GLOBAL, false);
        return bundle;
    }
}
