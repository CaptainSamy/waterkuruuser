package jp.co.wssj.iungo.screens.timeline.timelinedetail;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.timeline.TimeLineResponse;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.timeline.ITimeLineView;
import jp.co.wssj.iungo.screens.timeline.TimeLinePresenter;
import jp.co.wssj.iungo.screens.timeline.adapter.TimeLineDetailAdapter;
import jp.co.wssj.iungo.widget.ILoadMoreListener;
import jp.co.wssj.iungo.widget.KenBurnsView;
import jp.co.wssj.iungo.widget.LoadMoreRecyclerView;

/**
 * Created by Nguyen Huu Ta on 13/9/2017.
 */

public class TimeLineDetailFragment extends BaseFragment<ITimeLineView, TimeLinePresenter>
        implements ITimeLineView, View.OnClickListener, ILoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "TimeLineDetailFragment";

    public static final String KEY_USER_MANAGER_ID = "user_manager_id";

    public static final String KEY_IMAGE_STORE = "image_store";

    public static final String KEY_STORE_NAME = "store_name";

    private LoadMoreRecyclerView mRecycleTimeLine;

//    private SwipeRefreshLayout mRefreshLayout;

    private TimeLineDetailAdapter mAdapter;

    private int mIsPullDown = 0;

    private int mUserManagerId;

    private int mHeaderHeight;

    private int mMinHeaderTranslation;

    private KenBurnsView mHeaderPicture;

    private ImageView mHeaderLogo, ivLogin;

    private View mHeader;

    private AccelerateDecelerateInterpolator mSmoothInterpolator;

    private TextView mTitleActionBar, tvActionBar;

    private RectF mRect1 = new RectF();

    private RectF mRect2 = new RectF();

    private RelativeLayout mLayoutActionBar;

    public static TimeLineDetailFragment newInstance(Bundle args) {

        TimeLineDetailFragment fragment = new TimeLineDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_time_line_detail;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_TIMELINE_DETAIL;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_screen_timeline);
    }

    @Override
    protected TimeLinePresenter onCreatePresenter(ITimeLineView view) {
        return new TimeLinePresenter(view);
    }

    @Override
    protected boolean isRetainState() {
        return true;
    }

    @Override
    public boolean isDisplayNavigationButton() {
        return true;
    }

    @Override
    public int getNavigationBottomId() {
        return R.id.navigation_timeline;
    }

    @Override
    protected ITimeLineView onCreateView() {
        return this;
    }

    @Override
    public boolean isDisplayActionBar() {
        return false;
    }

    @Override
    protected void initViews(View rootView) {
//        mRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshLayout);
        mRecycleTimeLine = (LoadMoreRecyclerView) rootView.findViewById(R.id.rcTimeLine);
        mRecycleTimeLine.setLayoutManager(new LinearLayoutManager(getActivityContext(), LinearLayoutManager.VERTICAL, false));
        mRecycleTimeLine.setNestedScrollingEnabled(false);
        mRecycleTimeLine.setHasFixedSize(true);
//        mRecycleTimeLine.addItemDecoration(new DividerItemDecoration(getActivityContext(), DividerItemDecoration.VERTICAL));

        mSmoothInterpolator = new AccelerateDecelerateInterpolator();
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mHeader = rootView.findViewById(R.id.header);
        mHeaderPicture = (KenBurnsView) rootView.findViewById(R.id.header_picture);
        mHeaderPicture.setResourceIds(R.drawable.picture0);
        mHeaderLogo = (ImageView) rootView.findViewById(R.id.header_logo);
        mTitleActionBar = (TextView) rootView.findViewById(R.id.titleActionBar);
        ivLogin = (ImageView) rootView.findViewById(R.id.ivLogin);
        tvActionBar = (TextView) rootView.findViewById(R.id.tvActionBar);
        mLayoutActionBar = (RelativeLayout) rootView.findViewById(R.id.layoutActionBar);

        ViewTreeObserver viewTreeObserver = mLayoutActionBar.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                mLayoutActionBar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mMinHeaderTranslation = -mHeaderHeight + mLayoutActionBar.getMeasuredHeight();

            }
        });
    }

    private void setupListView() {

        mRecycleTimeLine.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int scrollY = getScrollY();
                mHeader.setTranslationY(Math.max(-scrollY, mMinHeaderTranslation));
                float ratio = clamp(mHeader.getTranslationY() / mMinHeaderTranslation, 0.0f, 1.0f);
                interpolate(mHeaderLogo, ivLogin, mSmoothInterpolator.getInterpolation(ratio));
                interpolateTitleActionBar(mTitleActionBar, tvActionBar, mSmoothInterpolator.getInterpolation(ratio));
            }
        });
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }

    private void interpolate(View view1, View view2, float interpolation) {
        getOnScreenRect(mRect1, view1);
        getOnScreenRect(mRect2, view2);

        float scaleX = 1.0F + interpolation * (mRect2.width() / mRect1.width() - 1.0F);
        float scaleY = 1.0F + interpolation * (mRect2.height() / mRect1.height() - 1.0F);
        float translationX = 0.5F * (interpolation * (mRect2.left + mRect2.right - mRect1.left - mRect1.right));
        float translationY = 0.5F * (interpolation * (mRect2.top + mRect2.bottom - mRect1.top - mRect1.bottom));

        view1.setTranslationX(translationX);
        view1.setTranslationY(translationY - mHeader.getTranslationY());
        view1.setScaleX(scaleX);
        view1.setScaleY(scaleY);
    }

    private RectF mRect3 = new RectF();

    private RectF mRect4 = new RectF();

    private void interpolateTitleActionBar(View view1, View view2, float interpolation) {
        getOnScreenRect(mRect3, view1);
        getOnScreenRect(mRect4, view2);

        float scaleX = 1.0F + interpolation * (mRect4.width() / mRect3.width() - 1.0F);
        float scaleY = 1.0F + interpolation * (mRect4.height() / mRect3.height() - 1.0F);
        float translationX = 0.5F * (interpolation * (mRect4.left + mRect4.right - mRect3.left - mRect3.right));
        float translationY = 0.5F * (interpolation * (mRect4.top + mRect4.bottom - mRect3.top - mRect3.bottom));

        view1.setTranslationX(translationX);
        view1.setTranslationY(translationY - mHeader.getTranslationY());
        view1.setScaleX(scaleX);
        view1.setScaleY(scaleY);
    }

    private RectF getOnScreenRect(RectF rect, View view) {
        rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        return rect;
    }

    public int getScrollY() {
        View c = mRecycleTimeLine.getChildAt(0);
        if (c == null) {
            return 0;
        }
        LinearLayoutManager layoutManager = ((LinearLayoutManager) mRecycleTimeLine.getLayoutManager());
        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = mAdapter.getHeader().getHeight();
        }

        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    @Override
    protected void initAction() {
        mRecycleTimeLine.setOnLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        Bundle args = getArguments();
        if (args != null) {
            mUserManagerId = args.getInt(KEY_USER_MANAGER_ID);
            String imageStore = args.getString(KEY_IMAGE_STORE);
            String storeName = args.getString(KEY_STORE_NAME);
            if (mAdapter == null) {
                mAdapter = new TimeLineDetailAdapter(new ArrayList<TimeLineResponse.TimeLineData.ListTimeline>(), getPresenter(), getActivityCallback());
                setRefresh(true);
                mAdapter.setImageStore(imageStore);
                mAdapter.setStoreName(storeName);
                getPresenter().getTimelineDetail(mUserManagerId, 0);
            }
        }
        mRecycleTimeLine.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onRefresh() {
        mIsPullDown = 1;
        getPresenter().getTimelineDetail(mUserManagerId, 0);
    }

    @Override
    public void onLoadMore() {
        setRefresh(true);
        getPresenter().getTimelineDetail(mUserManagerId, mAdapter.getLastTimelineId());
    }

    @Override
    public void onGetTimelineSuccess(TimeLineResponse.TimeLineData timeLineData) {
        setRefresh(false);
        mRecycleTimeLine.notifyLoadComplete();
        if (timeLineData != null && timeLineData.getListTimeline().size() > 0) {
            showTextNoItem(false, null);
            List<TimeLineResponse.TimeLineData.ListTimeline> listTimeline = timeLineData.getListTimeline();
            mAdapter.refreshList(listTimeline, mIsPullDown);
            mIsPullDown = 0;
        } else {
            if (timeLineData != null && timeLineData.getListTimeline().size() == 0) {
                mRecycleTimeLine.setEndOfData(true);
            }
            if (mAdapter.getItemCount() == 0) {
                showTextNoItem(true, getString(R.string.no_timeline));
            }
        }
        setupListView();
    }

    @Override
    public void onGetTimelineFailure(String message) {
        setRefresh(false);
    }

    private void setRefresh(boolean fresh) {
//        mRefreshLayout.setRefreshing(fresh);
    }
}
