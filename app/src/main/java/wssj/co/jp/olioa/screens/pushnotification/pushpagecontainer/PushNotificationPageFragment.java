package wssj.co.jp.olioa.screens.pushnotification.pushpagecontainer;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;
import wssj.co.jp.olioa.utils.Constants;

/**
 * Created by tuanle on 10/16/17.
 */

public class PushNotificationPageFragment extends BaseFragment<IPushNotificationPageView, PushNotificationPagePresenter> implements IPushNotificationPageView {

    private ViewPager mViewPager;

    private PushNotificationPageAdapter mAdapter;

    private String[] mTitles = {"すべて","カレンダー", "お気に入り", "アンケート"};

    private SegmentTabLayout mTabLayout;

    public static PushNotificationPageFragment newInstance(Bundle args) {
        PushNotificationPageFragment fragment = new PushNotificationPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_push_notification_list);
    }

    @Override
    protected String getLogTag() {
        return "PushNotificationPageFragment";
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_PUSH_NOTIFICATION_PAGER;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_push_pager;
    }

    @Override
    protected PushNotificationPagePresenter onCreatePresenter(IPushNotificationPageView view) {
        return new PushNotificationPagePresenter(view);
    }

    @Override
    protected IPushNotificationPageView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        super.initViews(rootView);
        mViewPager = (ViewPager) rootView.findViewById(R.id.push_pager);
        mTabLayout = (SegmentTabLayout) rootView.findViewById(R.id.tablayout);
    }

    @Override
    protected void initData() {
        int serviceCompanyId = 0;
        if (getArguments() != null) {
            serviceCompanyId = getArguments().getInt(Constants.KEY_SERVICE_COMPANY_ID, 0);
        }
        mAdapter = new PushNotificationPageAdapter(getChildFragmentManager(), getActivityContext(), serviceCompanyId);
        mTabLayout.setTabData(mTitles);
        mViewPager.setAdapter(mAdapter);
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
