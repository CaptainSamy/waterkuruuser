package jp.co.wssj.iungo.screens.qa;

import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.menu.QAResponse;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.qa.adapter.QAAdapter;
import jp.co.wssj.iungo.utils.Constants;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class QAFragment extends BaseFragment<IQAView, QAPresenter> implements IQAView {

    private static String TAG = "QAFragment";

    private ExpandableListView mListViewQA;

    private QAAdapter mAdapter;

    private List<QAResponse.ListQAData.QAData> mListQA;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return 0;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_qa;
    }

    @Override
    protected QAPresenter onCreatePresenter(IQAView view) {
        return new QAPresenter(view);
    }

    @Override
    protected IQAView onCreateView() {
        return this;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_screen_qa);
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return true;
    }

    @Override
    public int getNavigationMenuID() {
        return R.id.menu_question_answer;
    }

    @Override
    protected void initViews(View rootView) {
        mListViewQA = (ExpandableListView) rootView.findViewById(R.id.listViewQA);

    }

    @Override
    protected void initData() {
        getPresenter().getListQA(Constants.INIT_PAGE, Constants.LIMIT);
    }

    int previousItem = -1;

    @Override
    protected void initAction() {
        mListViewQA.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousItem) {
                    mListViewQA.collapseGroup(previousItem);
                }
                previousItem = groupPosition;
            }
        });
    }

    @Override
    public void onGetListQASuccess(final int currentPage, final int totalPage, List<QAResponse.ListQAData.QAData> data) {
        if (mAdapter == null) {
            mListQA = new ArrayList<>();
            mAdapter = new QAAdapter(getActivityContext(), mListQA, new QAAdapter.IEndOfListView() {

                @Override
                public void onEndOfListView() {
                    if (currentPage < totalPage) {
                        getPresenter().getListQA(currentPage + 1, Constants.LIMIT);
                    }
                }
            });
            mListViewQA.setAdapter(mAdapter);
        }
        mListQA.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetListQAFailure(String message) {
        showToast(message);
    }
}
