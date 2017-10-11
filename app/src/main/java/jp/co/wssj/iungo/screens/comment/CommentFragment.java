package jp.co.wssj.iungo.screens.comment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

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
import jp.co.wssj.iungo.screens.transition.TransitionAdapter;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;
import jp.co.wssj.iungo.utils.Utils;
import jp.co.wssj.iungo.widget.CirclePageIndicator;
import jp.co.wssj.iungo.widget.CustomViewPager;

/**
 * Created by Nguyen Huu Ta on 21/9/2017.
 */

public class CommentFragment extends BaseFragment<ICommentView, CommentPresenter> implements ICommentView {

    private static final String TAG = "CommentFragment";

    public static final String KEY_TIME_LIKE_ID = "timeline_id";

    public static final String KEY_ITEM_POSITION = "item_position";

    public static final String KEY_LIST_ITEMS = "list_items";

    private CustomViewPager mViewPagePhoto;

    private CirclePageIndicator mIndicator;

    private ListView mListViewComment;

    private ImageView mButtonSendComment;

    private EmojiconEditText mInputComment;

    private RelativeLayout mLayoutViewPager;

    private ProgressBar mProgressSend;

    private CommentPresenter mPresenter;

    private CommentAdapter mAdapterComment;

    private List<CommentResponse.CommentData.ListComment> mListComment;

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
        return getString(R.string.title_screen_comment);
    }

    @Override
    protected CommentPresenter onCreatePresenter(ICommentView view) {
        mPresenter = new CommentPresenter(view);
        return mPresenter;
    }

    @Override
    public boolean isDisplayActionBar() {
        return false;
    }

    @Override
    protected ICommentView onCreateView() {
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int currentItem = getArguments().getInt(KEY_ITEM_POSITION);
        ArrayList<String> listUrlImage = getArguments().getStringArrayList(KEY_LIST_ITEMS);

        if (listUrlImage != null && listUrlImage.size() > 0) {
            mLayoutViewPager.setVisibility(View.VISIBLE);
            TransitionAdapter animalPagerAdapter = new TransitionAdapter(getChildFragmentManager(), listUrlImage);
            mViewPagePhoto.setAdapter(animalPagerAdapter);
            mViewPagePhoto.setCurrentItem(currentItem);
            mIndicator.setViewPager(mViewPagePhoto);
            if (listUrlImage.size() == 1) {
                mIndicator.setVisibility(View.GONE);
            } else {
                mIndicator.setVisibility(View.VISIBLE);
            }
        } else {
            mLayoutViewPager.setVisibility(View.GONE);
        }


//        ImageView imageView = (ImageView) view.findViewById(R.id.ivPhoto);
//        String urlImage = listUrlImage.get(0);
//        ViewCompat.setTransitionName(imageView, urlImage.substring(urlImage.lastIndexOf("/") + 1));
//        Glide.with(this)
//                .load(urlImage)
//                .dontAnimate()
//                .listener(new RequestListener<String, GlideDrawable>() {
//
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        startPostponedEnterTransition();
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        startPostponedEnterTransition();
//                        return false;
//                    }
//                })
//                .into(imageView);
    }
//        if (listUrlImage != null && listUrlImage.size() > 0) {
//            mLayoutViewPager.setVisibility(View.VISIBLE);
//            TransitionAdapter animalPagerAdapter = new TransitionAdapter(getChildFragmentManager(), listUrlImage);
//            mViewPagePhoto.setAdapter(animalPagerAdapter);
//            mViewPagePhoto.setCurrentItem(currentItem);
//            mIndicator.setViewPager(mViewPagePhoto);
//        } else {
//            mLayoutViewPager.setVisibility(View.GONE);
//        }

    @Override
    protected void initViews(View rootView) {
        mViewPagePhoto = (CustomViewPager) rootView.findViewById(R.id.vpPhotoTimeline);
        mIndicator = (CirclePageIndicator) rootView.findViewById(R.id.indicator);
        mListViewComment = (ListView) rootView.findViewById(R.id.lvComment);
        mButtonSendComment = (ImageView) rootView.findViewById(R.id.tvSendComment);
        mProgressSend = (ProgressBar) rootView.findViewById(R.id.progressSend);
        mInputComment = (EmojiconEditText) rootView.findViewById(R.id.etComment);
        mLayoutViewPager = (RelativeLayout) rootView.findViewById(R.id.layoutViewPager);

        mIndicator.setFillColor(ContextCompat.getColor(getContext(), R.color.colorMain));
        mIndicator.setPageColor(ContextCompat.getColor(getContext(), R.color.white));
        mIndicator.setRadius(Utils.convertDpToPixel(getContext(), 4));
        mIndicator.setStrokeWidth(0);
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
        if (mListComment != null && commentData.getListComment().size() > 0) {
            mListComment.clear();
            mListComment.addAll(commentData.getListComment());
            Collections.reverse(mListComment);
            mAdapterComment.notifyDataSetChanged();
        } else {
            showTextNoItem("no comment", mListViewComment);
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
    public void onStop() {
        super.onStop();
        Logger.d(TAG, "removeCallbacks ");
        mHandle.removeCallbacks(mRunAble);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
