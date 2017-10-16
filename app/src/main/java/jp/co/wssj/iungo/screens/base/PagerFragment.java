package jp.co.wssj.iungo.screens.base;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.screens.adapters.FragmentPagerAdapter;
import jp.co.wssj.iungo.utils.Logger;
import jp.co.wssj.iungo.widget.NonSwipeableViewPager;

/**
 * Created by HieuPT on 6/6/2017.
 */

public abstract class PagerFragment<V extends IFragmentView, P extends FragmentPresenter<V>> extends PagedFragment<V, P>
        implements IPagerFragmentCallback {

    private View mWrapperTabLayout;

    private TabLayout mTabLayout;

    private NonSwipeableViewPager mViewPager;

    private List<PagedFragment> mFragments;

    private List<ViewPager.OnPageChangeListener> mOnPageChangeListeners;

    private final ViewPager.OnPageChangeListener mInternalOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {

        private static final int INVALID_POSITION = -1;

        private int mPreviousPosition = INVALID_POSITION;

        @Override
        public void onPageSelected(int position) {
            if (position < getFragmentsSize()) {
                Logger.d(getLogTag(), "#onPageSelected: " + position);
                List<PagedFragment> fragments = getFragments();
                if (fragments != null) {
                    PagedFragment fragment = fragments.get(position);
                    if (fragment != null && fragment.isAttached()) {
                        fragment.refresh();
                        fragment.notifyPageSelected();
                        if (mPreviousPosition != INVALID_POSITION && mPreviousPosition != position) {
                            PagedFragment previousFragment = fragments.get(mPreviousPosition);
                            previousFragment.notifyPageUnselected();
                        }
                    }
                }
                mPreviousPosition = position;
            }
        }
    };

    @Override
    public void onAttachFragment(Fragment childFragment) {
        Logger.d(getLogTag(), "#onAttachFragment");
        if (childFragment instanceof PagedFragment) {
            ((PagedFragment) childFragment).setPagerFragmentCallback(this);
        }
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_view_pager;
    }

    @Override
    protected void initViews(View rootView) {
        mFragments = initFragments();
        mWrapperTabLayout = rootView.findViewById(R.id.wrapper_tab_layout);
        mTabLayout = (TabLayout) rootView.findViewById(R.id.manager_tab_layout);
        mViewPager = (NonSwipeableViewPager) rootView.findViewById(R.id.manager_pager);
        mViewPager.setAdapter(new FragmentPagerAdapter(getActivityContext(), getChildFragmentManager(), mFragments));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initAction() {
        addOnPageChangeListener(mInternalOnPageChangeListener);
    }

    protected void callOnPageSelected(int selectedPage) {
        mInternalOnPageChangeListener.onPageSelected(selectedPage);
    }

    public NonSwipeableViewPager getViewPager() {
        return mViewPager;
    }

    public TabLayout getTabLayout() {
        return mTabLayout;
    }

    public void setShowTabLayout(boolean isShown) {
        if (mWrapperTabLayout != null) {
            mWrapperTabLayout.setVisibility(isShown ? View.VISIBLE : View.GONE);
        }
    }

    public void setSwipeable(boolean isSwipeable) {
        if (mViewPager != null) {
            mViewPager.setSwipeable(isSwipeable);
        }
    }

    public int getSelectedPage() {
        if (mViewPager != null) {
            return mViewPager.getCurrentItem();
        }
        return -1;
    }

    public PagedFragment getSelectedFragment() {
        int selectedPage = getSelectedPage();
        if (selectedPage != -1) {
            return mFragments.get(selectedPage);
        }
        return null;
    }

    @Override
    public boolean onBackPressed() {
        PagedFragment fragment = getSelectedFragment();
        return fragment != null && fragment.onBackPressed();
    }

    @Override
    public void setSelectedPage(int position) {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position);
        }
    }

    public void setSelectedPageByItemId(int itemId) {
        if (mFragments != null) {
            for (int i = 0; i < getFragmentsSize(); i++) {
                PagedFragment fragment = mFragments.get(i);
                if (fragment.getNavigationBottomId() == itemId) {
                    setSelectedPage(i);
                    break;
                }
            }
        }
    }

    public void addOnTabSelectedListener(TabLayout.OnTabSelectedListener listener) {
        if (mTabLayout != null && listener != null) {
            mTabLayout.addOnTabSelectedListener(listener);
        }
    }

    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        if (mViewPager != null && listener != null) {
            if (mOnPageChangeListeners == null) {
                mOnPageChangeListeners = new ArrayList<>();
            }
            mOnPageChangeListeners.add(listener);
            mViewPager.addOnPageChangeListener(listener);
        }
    }

    protected void dispatchOnPageSelected(int position) {
        if (mOnPageChangeListeners != null) {
            for (ViewPager.OnPageChangeListener listener : mOnPageChangeListeners) {
                if (listener != null) {
                    listener.onPageSelected(position);
                }
            }
        }
    }

    public List<PagedFragment> getFragments() {
        if (mFragments != null) {
            return Collections.unmodifiableList(mFragments);
        }
        return null;
    }

    protected int getFragmentsSize() {
        if (mFragments != null) {
            return mFragments.size();
        }
        return 0;
    }

    protected abstract List<PagedFragment> initFragments();
}
