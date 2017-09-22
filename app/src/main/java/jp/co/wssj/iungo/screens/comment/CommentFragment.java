package jp.co.wssj.iungo.screens.comment;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.rockerhieu.emojicon.EmojiconEditText;
import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.timeline.CommentResponse;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.timeline.adapter.CommentAdapter;
import jp.co.wssj.iungo.utils.Constants;

/**
 * Created by Nguyen Huu Ta on 21/9/2017.
 */

public class CommentFragment extends BaseFragment<ICommentView, CommentPresenter> implements ICommentView {

    private static final String TAG = "CommentFragment";

    public static final String KEY_TIME_LIKE_ID = "timeline_id";

    private CommentAdapter mAdapterComment;

    private List<CommentResponse.CommentData.ListComment> mListComment;

    private ListView mListViewComment;

    private ImageView mButtonSendComment;

    private EmojiconEditText mInputComment;

    private ProgressBar mProgressSend;

    private CommentPresenter mPresenter;

    private int mTimelineId;

    public static CommentFragment newInstance(Bundle b) {
        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_COMMENT;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_comment;
    }

    @Override
    public String getAppBarTitle() {
        return "Comment";
    }

    @Override
    protected CommentPresenter onCreatePresenter(ICommentView view) {
        mPresenter = new CommentPresenter(view);
        return mPresenter;
    }

    @Override
    protected ICommentView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        mListViewComment = (ListView) rootView.findViewById(R.id.lvComment);
        mButtonSendComment = (ImageView) rootView.findViewById(R.id.tvSendComment);
        mProgressSend = (ProgressBar) rootView.findViewById(R.id.progressSend);
        mInputComment = (EmojiconEditText) rootView.findViewById(R.id.etComment);
    }

    @Override
    protected void initAction() {
        mInputComment.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mButtonSendComment.setEnabled(false);
                    mButtonSendComment.setImageDrawable(getResources().getDrawable(R.drawable.icon_send));
                } else {
                    mButtonSendComment.setEnabled(true);
                    mButtonSendComment.setImageDrawable(getResources().getDrawable(R.drawable.icon_send_enable));
                }
            }
        });
        mButtonSendComment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String commentString = mInputComment.getText().toString();
                if (!TextUtils.isEmpty(commentString)) {
                    statusButtonSend(mButtonSendComment, mProgressSend);
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            mPresenter.addComment(mTimelineId, StringEscapeUtils.escapeJava(commentString));
                        }
                    }, 1000);
                }
            }
        });
    }

    @Override
    protected void initData() {
        if (mAdapterComment == null) {
            mListComment = new ArrayList<>();
            mAdapterComment = new CommentAdapter(getActivityContext(), mListComment, mPresenter);
            mListViewComment.setAdapter(mAdapterComment);
        }
        if (getArguments() != null) {
            mTimelineId = getArguments().getInt(KEY_TIME_LIKE_ID);
            getPresenter().getListComment(mTimelineId);
        }
    }

    private Handler mHandle = new Handler();

    private Runnable mRunAble = new Runnable() {

        @Override
        public void run() {
            mHandle.postDelayed(mRunAble, Constants.TIME_DELAY_GET_LIST_CHAT);
            getPresenter().getListComment(mTimelineId);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mHandle.postDelayed(mRunAble, Constants.TIME_DELAY_GET_LIST_CHAT);
    }

    @Override
    public void onCommentSuccess(String message) {
        statusButtonSend(mButtonSendComment, mProgressSend);
        mInputComment.setText(Constants.EMPTY_STRING);
        mProgressSend.setVisibility(View.GONE);
        getPresenter().getListComment(mTimelineId);
    }

    @Override
    public void onCommentFailure(String message) {
        statusButtonSend(mButtonSendComment, mProgressSend);
        mProgressSend.setVisibility(View.GONE);
    }

    @Override
    public void onGetListCommentSuccess(CommentResponse.CommentData commentData) {
        if (mListComment != null) {
            mListComment.clear();
            mListComment.addAll(commentData.getListComment());
            Collections.reverse(mListComment);
            mAdapterComment.notifyDataSetChanged();
        }

    }

    @Override
    public void onGetListCommentFailure(String message) {
        showToast(message);
    }

    private void statusButtonSend(View buttonSend, View progress) {
        if (buttonSend.getVisibility() == View.VISIBLE) {
            buttonSend.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.VISIBLE);
        } else {
            buttonSend.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHandle.removeCallbacks(mRunAble);
    }
}
