package wssj.co.jp.olioa.screens.qa;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.menu.QAResponse;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;
import wssj.co.jp.olioa.screens.qa.adapter.QAAdapter;
import wssj.co.jp.olioa.screens.qadetail.QADetailFragment;
import wssj.co.jp.olioa.utils.Constants;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class QAFragment extends BaseFragment<IQAView, QAPresenter> implements IQAView {

    private static String TAG = "QAFragment";

    private ListView mListViewQA;

    private QAAdapter mAdapter;

    private List<QAResponse.ListQAData.QAData> mListQA;

    private int mCurrentPage, mTotalPage;

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
    public int getNavigationMenuId() {
        return R.id.menu_question_answer;
    }

    @Override
    protected void initViews(View rootView) {
        mListViewQA = (ListView) rootView.findViewById(R.id.listViewQA);
    }

    @Override
    protected void initData() {
        if (mListQA == null) {
            mListQA = new ArrayList<>();
            mAdapter = new QAAdapter(getActivityContext(), mListQA, new QAAdapter.IEndOfListView() {

                @Override
                public void onEndOfListView() {
                    if (mCurrentPage < mTotalPage) {
                        getPresenter().getListQA(mCurrentPage + 1, Constants.LIMIT);
                    }
                }
            });
            getPresenter().getListQA(Constants.INIT_PAGE, Constants.LIMIT);
        }
        mListViewQA.setAdapter(mAdapter);
    }

    @Override
    protected void initAction() {
        mListViewQA.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(QADetailFragment.KEY_QA, mListQA.get(position));
                getActivityCallback().displayScreen(IMainView.FRAGMENT_QA_DETAIL, true, true, bundle);
            }
        });
    }

    @Override
    public void onGetListQASuccess(final int currentPage, final int totalPage, List<QAResponse.ListQAData.QAData> data) {
        mCurrentPage = currentPage;
        mTotalPage = totalPage;
        mListQA.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetListQAFailure(String message) {
        showToast(message);
    }
}
