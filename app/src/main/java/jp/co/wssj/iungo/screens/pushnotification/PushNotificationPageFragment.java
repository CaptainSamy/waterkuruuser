package jp.co.wssj.iungo.screens.pushnotification;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;

/**
 * Created by tuanle on 10/16/17.
 */

public class PushNotificationPageFragment extends BaseFragment<IPushNotificationPageView, PushNotificationPagePresenter> implements IPushNotificationPageView {

    private ViewPager mViewPager;
    private PushNotificationPageAdapter mAdapter;
    private String[] mTitles = {"すべて", "お気に入り", "アンケート"};
    private SegmentTabLayout mTabLayout;

    @Override
    protected void initViews(View rootView) {
        super.initViews(rootView);
        mViewPager = (ViewPager) rootView.findViewById(R.id.push_pager);
        mTabLayout = (SegmentTabLayout) rootView.findViewById(R.id.tablayout);
    }

    @Override
    protected void initData() {
        mAdapter = new PushNotificationPageAdapter(getChildFragmentManager(), getActivityContext());
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
}
