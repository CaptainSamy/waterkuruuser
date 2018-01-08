package jp.co.wssj.iungo.screens.coupone;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.profile.ProfileStoreFragment;
import jp.co.wssj.iungo.screens.pushnotification.pushpagecontainer.PushNotificationPageAdapter;
import android.view.View;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;
import jp.co.wssj.iungo.screens.calender.CalenderPresenter;
import jp.co.wssj.iungo.screens.calender.ICalenderView;

/**
 * Created by thang on 12/29/2017.
 */

public class CouponeFragment extends BaseFragment<ICouponeView, CouponePresenter> implements ICouponeView {

    private static String TAG = "CouponeFragment";
    private String[] mTitles = {"未利用", "利用済み"};
    private SegmentTabLayout mTabLayout;
    private ViewPager mViewPager;
    private CouponeAdapter mcouponeAdapter;


    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_COUPONE;
    }

        public static CouponeFragment newInstance(Bundle args) {
        CouponeFragment fragment = new CouponeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_coupone;
    }

    @Override
    protected CouponePresenter onCreatePresenter(ICouponeView view) {
        return new CouponePresenter(view);
    }

    @Override
    protected ICouponeView onCreateView() {
        return this;
    }

    @Override
    public String getAppBarTitle() {
        return "クーポン";
    }

    @Override
    protected void initViews(View rootView) {
        super.initViews(rootView);
        mViewPager = (ViewPager) rootView.findViewById(R.id.push_pager);
        mTabLayout = (SegmentTabLayout) rootView.findViewById(R.id.tablayout);

    }

    @Override
    protected void initData() {

        mcouponeAdapter = new CouponeAdapter(getChildFragmentManager(), getActivityContext());
        mTabLayout.setTabData(mTitles);
        mViewPager.setAdapter(mcouponeAdapter);
    }
    @Override
    protected void initAction() {
        super.initAction();
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {

            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
