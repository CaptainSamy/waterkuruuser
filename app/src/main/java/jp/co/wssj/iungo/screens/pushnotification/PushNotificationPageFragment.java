package jp.co.wssj.iungo.screens.pushnotification;

import android.support.v4.view.ViewPager;
import android.view.View;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;

/**
 * Created by tuanle on 10/16/17.
 */

public class PushNotificationPageFragment extends BaseFragment<IPushNotificationPageView, PushNotificationPagePresenter> implements IPushNotificationPageView {

    private ViewPager mViewPager;
    private PushNotificationPageAdapter mAdapter;

    @Override
    protected void initViews(View rootView) {
        super.initViews(rootView);
        mViewPager = (ViewPager) rootView.findViewById(R.id.push_pager);
    }

    @Override
    protected void initData() {
        mAdapter = new PushNotificationPageAdapter(getChildFragmentManager(), getActivityContext());
        mViewPager.setAdapter(mAdapter);
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
