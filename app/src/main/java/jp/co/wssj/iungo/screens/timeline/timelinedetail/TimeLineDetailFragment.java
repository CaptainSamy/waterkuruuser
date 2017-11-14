package jp.co.wssj.iungo.screens.timeline.timelinedetail;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import jp.co.wssj.iungo.utils.Logger;
import jp.co.wssj.iungo.utils.Utils;
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

    private KenBurnsView mImageBackground;

    private ImageView mAvatar, ivAvatarActionBar, mButtonBack;

    private View mHeader, mLine;

    private AccelerateDecelerateInterpolator mSmoothInterpolator;

    private TextView mStoreName, titleActionBar;

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
        mImageBackground = (KenBurnsView) rootView.findViewById(R.id.header_picture);
        mAvatar = (ImageView) rootView.findViewById(R.id.header_logo);
        mStoreName = (TextView) rootView.findViewById(R.id.titleActionBar);
        ivAvatarActionBar = (ImageView) rootView.findViewById(R.id.ivAvatarActionBar);
        titleActionBar = (TextView) rootView.findViewById(R.id.tvActionBar);
        mLayoutActionBar = (RelativeLayout) rootView.findViewById(R.id.layoutActionBar);
        mButtonBack = (ImageView) rootView.findViewById(R.id.ivBack);
        mLine = rootView.findViewById(R.id.line);
        ViewTreeObserver viewTreeObserver = mLayoutActionBar.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                mLayoutActionBar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mMinHeaderTranslation = -mHeaderHeight + mLayoutActionBar.getMeasuredHeight();

            }
        });
    }

    @Override
    protected void initAction() {
        mRecycleTimeLine.setOnLoadMoreListener(this);
        mButtonBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                backToPreviousScreen();
            }
        });
    }

    @Override
    protected void initData() {
        Bundle args = getArguments();
        if (args != null) {
            mUserManagerId = args.getInt(KEY_USER_MANAGER_ID);
            String urlImageStore = args.getString(KEY_IMAGE_STORE);
            String stringStoreName = args.getString(KEY_STORE_NAME);
            mStoreName.setText(stringStoreName);
            titleActionBar.setText(stringStoreName);
            Utils.fillImage(getActivityContext(), urlImageStore, mAvatar, R.drawable.icon_user);
            Utils.fillImage(getActivityContext(), urlImageStore, ivAvatarActionBar, R.drawable.icon_user);
            mImageBackground.fillImage(urlImageStore);
            if (mAdapter == null) {
                mAdapter = new TimeLineDetailAdapter(new ArrayList<TimeLineResponse.TimeLineData.ListTimeline>(), getPresenter(), getActivityCallback());
                setRefresh(true);
                mAdapter.setImageStore(urlImageStore);
                mAdapter.setStoreName(stringStoreName);
                getPresenter().getTimelineDetail(mUserManagerId, 0);
            }
        }
        mRecycleTimeLine.setAdapter(mAdapter);
    }

    private void setupListView() {

        mRecycleTimeLine.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int scrollY = getScrollY();
                if (scrollY >= -mMinHeaderTranslation) {
                    mLayoutActionBar.setBackgroundColor(getResources().getColor(R.color.colorBackground_Actionbar));
                    titleActionBar.setVisibility(View.VISIBLE);
                    ivAvatarActionBar.setVisibility(View.VISIBLE);

                    mImageBackground.setVisibility(View.INVISIBLE);
                    mStoreName.setVisibility(View.GONE);
                    mAvatar.setVisibility(View.GONE);

                    if (!mLine.isShown()) {
                        mLine.setVisibility(View.VISIBLE);
                    }
                } else {
                    mLayoutActionBar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    titleActionBar.setVisibility(View.INVISIBLE);
                    ivAvatarActionBar.setVisibility(View.INVISIBLE);

                    mImageBackground.setVisibility(View.VISIBLE);
                    mStoreName.setVisibility(View.VISIBLE);
                    mAvatar.setVisibility(View.VISIBLE);


                    if (mLine.isShown()) {
                        mLine.setVisibility(View.GONE);
                    }
                }
                Logger.d(TAG, "onScrolled " + scrollY + " / " + Math.max(-scrollY, mMinHeaderTranslation));
                mHeader.setTranslationY(Math.max(-scrollY, mMinHeaderTranslation));
                float ratio = clamp(mHeader.getTranslationY() / mMinHeaderTranslation, 0.0f, 1.0f);
                interpolate(mAvatar, ivAvatarActionBar, mSmoothInterpolator.getInterpolation(ratio));
                interpolateTitleActionBar(mStoreName, titleActionBar, mSmoothInterpolator.getInterpolation(ratio));
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
        if (mAdapter != null && mAdapter.getItemCount() > 1) {
            getPresenter().getTimelineDetail(mUserManagerId, mAdapter.getLastTimelineId());
        }
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
        }
        if (mAdapter.getItemCount() == 0) {
            showTextNoItem(true, getString(R.string.no_timeline));
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
