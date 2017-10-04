package jp.co.wssj.iungo.screens.timeline;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.timeline.TimeLineResponse;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.timeline.adapter.TimeLineAdapter;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;
import jp.co.wssj.iungo.widget.ILoadMoreListener;
import jp.co.wssj.iungo.widget.LoadMoreRecyclerView;

/**
 * Created by Nguyen Huu Ta on 13/9/2017.
 */

public class TimeLineFragment extends BaseFragment<ITimeLineView, TimeLinePresenter> implements ITimeLineView, View.OnClickListener, ILoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "TimeLineFragment";

    private LoadMoreRecyclerView mRecycleTimeLine;

    private SwipeRefreshLayout mRefreshLayout;

    private TimeLineAdapter mAdapter;

    private List<TimeLineResponse.TimeLineData.ListTimeline> mListTimeLine;

    private TimeLinePresenter mPresenter;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_TIME_LINE;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_time_line;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_screen_timeline);
    }

    @Override
    protected TimeLinePresenter onCreatePresenter(ITimeLineView view) {
        mPresenter = new TimeLinePresenter(view);
        return mPresenter;
    }

    @Override
    public int getMenuBottomID() {
        return MENU_TIME_LINE;
    }

    @Override
    public int getNavigationBottomId() {
        return R.id.navigation_timeline;
    }

    @Override
    public boolean isDisplayNavigationButton() {
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
            mListTimeLine = new ArrayList<>();
            mAdapter = new TimeLineAdapter(mListTimeLine, mPresenter, getActivityCallback());
            mAdapter.setRefreshTimeline(new TimeLineAdapter.IRefreshTimeline() {

                @Override
                public void onRefreshTimeline(ImageView view) {
//                    mPresenter.getTimeline(Constants.INIT_PAGE);
                    imageShares = view;
                }
            });
            setFresh(true);
            mPresenter.getTimeline(Constants.INIT_PAGE);
        }
        mRecycleTimeLine.setAdapter(mAdapter);
    }

    private ImageView imageShares;

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onRefresh() {
        mPresenter.getTimeline(Constants.INIT_PAGE);
    }

    @Override
    public void onLoadMore(int pageNumber) {
        Logger.d(TAG, "onLoadMore " + pageNumber);
        mPresenter.getTimeline(pageNumber);
    }

    @Override
    public void onGetTimelineSuccess(TimeLineResponse.TimeLineData timeLineData) {
        setFresh(false);
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
        setFresh(false);
    }

    private void setFresh(boolean fresh) {
        mRefreshLayout.setRefreshing(fresh);
    }

    public ImageView getImageShares() {
        return imageShares;
    }

}
