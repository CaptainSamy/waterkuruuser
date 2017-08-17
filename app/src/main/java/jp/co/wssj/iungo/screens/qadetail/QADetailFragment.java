package jp.co.wssj.iungo.screens.qadetail;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.menu.QAResponse;
import jp.co.wssj.iungo.screens.IMainView;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class QADetailFragment extends jp.co.wssj.iungo.screens.base.BaseFragment<QADetailView, QADetailPresenter> implements QADetailView {

    private static String TAG = "QADetailFragment";

    public static final String KEY_QA = "KEY_QA";

    private TextView mTextQuestion;

    private WebView mWebAnswer;

    public static QADetailFragment newInstance(Bundle args) {
        QADetailFragment fragment = new QADetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_QA_DETAIL;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.item_answer;
    }

    @Override
    protected QADetailPresenter onCreatePresenter(QADetailView view) {
        return new QADetailPresenter(view);
    }

    @Override
    protected QADetailView onCreateView() {
        return this;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_screen_qa);
    }

    @Override
    protected void initViews(View rootView) {
        mTextQuestion = (TextView) rootView.findViewById(R.id.question);
        mWebAnswer = (WebView) rootView.findViewById(R.id.itemAnswer);
        mWebAnswer.getSettings().setJavaScriptEnabled(true);
        mWebAnswer.getSettings().setBuiltInZoomControls(true);
        mWebAnswer.getSettings().setDisplayZoomControls(false);
    }

    @Override
    protected void initAction() {
        super.initAction();
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            QAResponse.ListQAData.QAData data = (QAResponse.ListQAData.QAData) bundle.get(KEY_QA);
            if (data != null) {
                mTextQuestion.setText(data.getQuestion());

                mWebAnswer.loadDataWithBaseURL(null, data.getAnswer(), "text/html", "UTF-8", null);

            }
        }
    }
}
