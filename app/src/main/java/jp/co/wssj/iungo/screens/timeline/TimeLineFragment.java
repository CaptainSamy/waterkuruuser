package jp.co.wssj.iungo.screens.timeline;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.timeline.TimeLineResponse;
import jp.co.wssj.iungo.screens.base.PagedFragment;
import jp.co.wssj.iungo.screens.timeline.adapter.TimeLineAdapter;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;
import jp.co.wssj.iungo.widget.ILoadMoreListener;
import jp.co.wssj.iungo.widget.LoadMoreRecyclerView;

/**
 * Created by Nguyen Huu Ta on 13/9/2017.
 */

public class TimeLineFragment extends PagedFragment<ITimeLineView, TimeLinePresenter>
        implements ITimeLineView, View.OnClickListener, ILoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "TimeLineFragment";

    private LoadMoreRecyclerView mRecycleTimeLine;

    private SwipeRefreshLayout mRefreshLayout;

    private TimeLineAdapter mAdapter;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_time_line;
    }

    @Override
    public String getPageTitle(Context context) {
        return getString(context, R.string.title_screen_timeline);
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
    public int getNavigationBottomId() {
        return R.id.navigation_timeline;
    }

    @Override
    protected ITimeLineView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        mRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshLayout);
        mRecycleTimeLine = (LoadMoreRecyclerView) rootView.findViewById(R.id.rcTimeLine);
        mRecycleTimeLine.setLayoutManager(new LinearLayoutManager(getActivityContext(), LinearLayoutManager.VERTICAL, false));
        mRecycleTimeLine.setNestedScrollingEnabled(false);
        mRecycleTimeLine.setHasFixedSize(true);
//        mRecycleTimeLine.addItemDecoration(new DividerItemDecoration(getActivityContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void initAction() {
        mRefreshLayout.setOnRefreshListener(this);
        mRecycleTimeLine.setOnLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        if (mAdapter == null) {
            mAdapter = new TimeLineAdapter(new ArrayList<TimeLineResponse.TimeLineData.ListTimeline>(), getPresenter(), getActivityCallback());
            setRefresh(true);
            getPresenter().getTimeline(Constants.INIT_PAGE);
        }
        mRecycleTimeLine.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onRefresh() {
        getPresenter().getTimeline(Constants.INIT_PAGE);
    }

    @Override
    public void onLoadMore(int pageNumber) {
        Logger.d(TAG, "onLoadMore " + pageNumber);
        setRefresh(true);
        getPresenter().getTimeline(pageNumber);
    }

    @Override
    public void onGetTimelineSuccess(TimeLineResponse.TimeLineData timeLineData) {
        setRefresh(false);
        if (timeLineData != null && timeLineData.getListTimeline().size() > 0) {
            int page = timeLineData.getPage();
            mRecycleTimeLine.setTotalPage(timeLineData.getTotalPage());
            mRecycleTimeLine.setCurrentPage(page);
            List<TimeLineResponse.TimeLineData.ListTimeline> listTimeline = timeLineData.getListTimeline();
            mAdapter.refreshList(listTimeline, page);
        }
    }

    @Override
    public void onGetTimelineFailure(String message) {
        setRefresh(false);
    }

    private void setRefresh(boolean fresh) {
        mRefreshLayout.setRefreshing(fresh);
    }
}
