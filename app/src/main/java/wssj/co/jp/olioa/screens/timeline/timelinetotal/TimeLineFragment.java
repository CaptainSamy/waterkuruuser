package wssj.co.jp.olioa.screens.timeline.timelinetotal;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.timeline.TimeLineResponse;
import wssj.co.jp.olioa.screens.timeline.ITimeLineView;
import wssj.co.jp.olioa.screens.timeline.TimeLinePresenter;
import wssj.co.jp.olioa.screens.timeline.adapter.TimeLineAdapter;
import wssj.co.jp.olioa.widget.ILoadMoreListener;
import wssj.co.jp.olioa.widget.LoadMoreRecyclerView;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;

/**
 * Created by Nguyen Huu Ta on 13/9/2017.
 */

public class TimeLineFragment extends BaseFragment<ITimeLineView, TimeLinePresenter>
        implements ITimeLineView, View.OnClickListener, ILoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "TimeLineDetailFragment";

    private LoadMoreRecyclerView mRecycleTimeLine;

    private SwipeRefreshLayout mRefreshLayout;

    private TimeLineAdapter mAdapter;

    private int mIsPullDown = 0;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_time_line;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_TIMELINE;
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
    public boolean isDisplayBackButton() {
        return false;
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
            mAdapter.setIsTimelineDetail(true);
            setRefresh(true);
            getPresenter().getTimeline(0);
        }
        mRecycleTimeLine.setAdapter(mAdapter);
    }

    @Override
    public boolean onBackPressed() {
        getActivityCallback().finishActivity();
        return true;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onRefresh() {
        mIsPullDown = 1;
        getPresenter().getTimeline(0);
    }

    @Override
    public void onLoadMore(int page) {
        setRefresh(true);
        getPresenter().getTimeline(mAdapter.getLastTimelineId());
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
        if (mAdapter.getItemCount() == 0) {
            showTextNoItem(true, getString(R.string.no_timeline));
        } else {
            showTextNoItem(false, null);
        }
    }

    @Override
    public void onGetTimelineFailure(String message) {
        mRecycleTimeLine.notifyLoadComplete();
        setRefresh(false);
    }

    private void setRefresh(boolean fresh) {
        mRefreshLayout.setRefreshing(fresh);
    }
}
