package jp.co.wssj.iungo.screens.timeline;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.timeline.TimeLineResponse;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.timeline.adapter.TimeLineAdapter;

/**
 * Created by Nguyen Huu Ta on 13/9/2017.
 */

public class TimeLineFragment extends BaseFragment<ITimeLineView, TimeLinePresenter> implements ITimeLineView {

    private static final String TAG = "TimeLineFragment";

    private RecyclerView mRecycleTimeLine;

    private TimeLineAdapter mAdapter;

    private List<TimeLineResponse> mListTimeLine;

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
        return new TimeLinePresenter(view);
    }

    @Override
    protected ITimeLineView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        mRecycleTimeLine = (RecyclerView) rootView.findViewById(R.id.rcTimeLine);
        mRecycleTimeLine.setLayoutManager(new LinearLayoutManager(getActivityContext(), LinearLayoutManager.VERTICAL, false));
        mRecycleTimeLine.setNestedScrollingEnabled(false);
        mRecycleTimeLine.setHasFixedSize(true);
        mRecycleTimeLine.addItemDecoration(new DividerItemDecoration(getActivityContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void initData() {
        mListTimeLine = new ArrayList<>();
        TimeLineResponse response = new TimeLineResponse("However, there's some funny drawing stuff going on, or recycling, or something because while the animation occurs, the item below the one that slides off screen also gets deleted for some reason. The answer that the question asker eventually marked as correct is unfortunately an RTFM towards the whole of Android's source. I've looked through there, and I can't find the notifications pull-down in JellyBean which I'm trying to emulate.");
        mListTimeLine.add(response);
        response = new TimeLineResponse("This is content 2");
        mListTimeLine.add(response);
        response = new TimeLineResponse("This is content 3");
        mListTimeLine.add(response);
        response = new TimeLineResponse("This is content 4");
        mListTimeLine.add(response);
        response = new TimeLineResponse("This is content 5");
        mListTimeLine.add(response);
        mAdapter = new TimeLineAdapter(mListTimeLine);
        mRecycleTimeLine.setAdapter(mAdapter);
    }
}
