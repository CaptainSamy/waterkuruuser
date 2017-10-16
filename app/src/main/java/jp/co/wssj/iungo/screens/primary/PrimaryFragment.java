package jp.co.wssj.iungo.screens.primary;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.screens.IActivityCallback;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.PagedFragment;
import jp.co.wssj.iungo.screens.base.PagerFragment;
import jp.co.wssj.iungo.screens.chat.StoreFollowFragment;
import jp.co.wssj.iungo.screens.listservicecompany.ListServiceCompanyFragment;
import jp.co.wssj.iungo.screens.timeline.TimeLineFragment;
import jp.co.wssj.iungo.utils.Constants;

/**
 * Created by HieuPT on 6/5/2017.
 */

public class PrimaryFragment extends PagerFragment<IPrimaryView, PrimaryPresenter> implements IPrimaryView {

    public static final String KEY_SCREEN_ID = "KEY_SCREEN_ID";

    public static final int SCREEN_LIST_SERVICE_COMPANY = 0;

//    public static final int SCREEN_HOME = 1;

    public static final int SCREEN_TIMELINE = 1;

    public static final int SCREEN_STORE_FOLLOW = 2;

    private static final String TAG = "PrimaryFragment";

    public static PrimaryFragment newInstance(Bundle bundle) {
        PrimaryFragment fragment = new PrimaryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public String getAppBarTitle() {
        if (getFragments() != null) {
            int selectedPage = getSelectedPage();
            if (selectedPage != -1) {
                PagedFragment fragment = getFragments().get(selectedPage);
                return fragment.getPageTitle(getActivityContext());
            }
        }
        return Constants.EMPTY_STRING;
    }

    @Override
    public boolean isDisplayNavigationButton() {
        return false;
    }

    @Override
    public boolean isGlobal() {
        return true;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_PRIMARY;
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return true;
    }

    @Override
    protected boolean isRetainState() {
        return true;
    }

    @Override
    protected PrimaryPresenter onCreatePresenter(IPrimaryView view) {
        return new PrimaryPresenter(view);
    }

    @Override
    protected IPrimaryView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        super.initViews(rootView);
        setSwipeable(false);
        setShowTabLayout(false);
        getViewPager().setOffscreenPageLimit(getFragmentsSize());
        getViewPager().setCurrentItem(SCREEN_TIMELINE);
    }

    @Override
    public boolean onBackPressed() {
        if (getSelectedPage() != SCREEN_TIMELINE) {
            setSelectedPage(SCREEN_TIMELINE);
            return true;
        }
        return super.onBackPressed();
    }

    @Override
    protected List<PagedFragment> initFragments() {
        List<PagedFragment> fragments = new ArrayList<>();
        fragments.add(new ListServiceCompanyFragment());
//        fragments.add(new HomeFragment());
        fragments.add(new TimeLineFragment());
        fragments.add(new StoreFollowFragment());
        return fragments;
    }



    @Override
    protected void initAction() {
        super.initAction();
        addOnPageChangeListener(new PrimaryPageChangeListener(getFragments(), getActivityCallback()));
        Bundle bundle = getArguments();
        int extraScreenId = SCREEN_TIMELINE;
        if (bundle != null) {
            extraScreenId = bundle.getInt(KEY_SCREEN_ID, SCREEN_TIMELINE);
        }
        final int selectedPage = extraScreenId;
        getViewPager().post(new Runnable() {

            @Override
            public void run() {
                dispatchOnPageSelected(selectedPage);
            }
        });
    }

    private final class PrimaryPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        private final List<PagedFragment> mFragments;

        private final IActivityCallback mActivityCallback;

        private PrimaryPageChangeListener(List<PagedFragment> fragments, IActivityCallback activityCallback) {
            mFragments = fragments;
            mActivityCallback = activityCallback;
        }

        @Override
        public void onPageSelected(int position) {
            if (position < getFragmentsSize()) {
                PagedFragment fragment = mFragments.get(position);
                if (mActivityCallback != null) {
                    mActivityCallback.setSelectedNavigationBottom(fragment.getNavigationBottomId());
                    mActivityCallback.setAppBarTitle(fragment.getPageTitle(getActivityContext()));
                }
            }
        }
    }
}
