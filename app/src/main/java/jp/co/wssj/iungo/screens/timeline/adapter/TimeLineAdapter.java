package jp.co.wssj.iungo.screens.timeline.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.timeline.CommentResponse;
import jp.co.wssj.iungo.model.timeline.TimeLineResponse;
import jp.co.wssj.iungo.screens.timeline.TimeLinePresenter;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;
import jp.co.wssj.iungo.utils.Utils;
import jp.co.wssj.iungo.widget.ExpandableListView;
import jp.co.wssj.iungo.widget.ExpandableTextView;
import jp.co.wssj.iungo.widget.likefacebook.ReactionView;

/**
 * Created by Nguyen Huu Ta on 13/9/2017.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineHolder> {

    private static final int SHOW_IMAGE = 0;

    private static final int NOT_SHOW_IMAGE = 1;

    private List<TimeLineResponse.TimeLineData.ListTimeline> mListTimeLine;

    private TimeLinePresenter mPresenter;

    public TimeLineAdapter(List<TimeLineResponse.TimeLineData.ListTimeline> listTimeline, TimeLinePresenter presenter) {
        mListTimeLine = listTimeline;
        mPresenter = presenter;
    }

    public void refreshList(List<TimeLineResponse.TimeLineData.ListTimeline> listTimeline) {
        if (listTimeline != null && mListTimeLine != null) {
            mListTimeLine.clear();
            mListTimeLine.addAll(listTimeline);
            notifyDataSetChanged();
        }
    }

    public TimeLineResponse.TimeLineData.ListTimeline getItemTimeLine(int position) {
        if (mListTimeLine != null && mListTimeLine.size() > 0) {
            return mListTimeLine.get(position);
        }
        return null;
    }

    @Override
    public TimeLineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_time_line, parent, false);
        return new TimeLineHolder(context, view);
    }

    @Override
    public void onBindViewHolder(TimeLineHolder holder, int position) {
        TimeLineResponse.TimeLineData.ListTimeline response = getItemTimeLine(position);
        if (response != null) {
            holder.bind(getItemTimeLine(position), position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        TimeLineResponse.TimeLineData.ListTimeline.Timeline timeline = getItemTimeLine(position).getTimeline();
        if (timeline != null) {
            return TextUtils.isEmpty(timeline.getImages()) ? NOT_SHOW_IMAGE : SHOW_IMAGE;
        }
        return NOT_SHOW_IMAGE;
    }

    @Override
    public int getItemCount() {
        return mListTimeLine.size();
    }

    public class TimeLineHolder extends RecyclerView.ViewHolder {

        private Context mContext;

        private ExpandableTextView mContent;

        private ImageView mImageStore, mImageTimeline, mImageSmile;

        private TextView mTextLike;

        private ReactionView mReactionFacebook;

        private ExpandableListView mListViewComment;

        private RelativeLayout mLayoutComment;

        private LinearLayout mButtonComment;

        private TextView mNumberComment, mButtonSendComment, mTime;

        private EditText mInputComment;

        private ProgressBar mProgressListView, mProgressSend;

        private CommentAdapter mAdapterComment;

        private List<CommentResponse.CommentData.ListComment> mListComment;

        public TimeLineHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            mImageStore = (ImageView) itemView.findViewById(R.id.ivStore);
            mTextLike = (TextView) itemView.findViewById(R.id.tvLike);
            mImageTimeline = (ImageView) itemView.findViewById(R.id.ivTimeLine);
            mNumberComment = (TextView) itemView.findViewById(R.id.tvNumberComment);
            mContent = (ExpandableTextView) itemView.findViewById(R.id.tvContent);
            mImageSmile = (ImageView) itemView.findViewById(R.id.ivSmile);
            mButtonComment = (LinearLayout) itemView.findViewById(R.id.buttomComment);
            mListViewComment = (ExpandableListView) itemView.findViewById(R.id.lvComment);
            mLayoutComment = (RelativeLayout) itemView.findViewById(R.id.layoutComment);
            mButtonSendComment = (TextView) itemView.findViewById(R.id.tvSendComment);
            mInputComment = (EditText) itemView.findViewById(R.id.etComment);
            mProgressListView = (ProgressBar) itemView.findViewById(R.id.progressBarListView);
            mProgressSend = (ProgressBar) itemView.findViewById(R.id.progressSend);
            mTime = (TextView) itemView.findViewById(R.id.tvTime);
            mReactionFacebook = (ReactionView) itemView.findViewById(R.id.reactionFacebook);
        }

        public void bind(final TimeLineResponse.TimeLineData.ListTimeline listTimeline, final int position) {
            final TimeLineResponse.TimeLineData.ListTimeline.Timeline timeline = listTimeline.getTimeline();
            final List<TimeLineResponse.TimeLineData.ListTimeline.Like> likes = listTimeline.getLikes();

            mTextLike.setText(getStringLike(timeline.getStatusLikeId()));
            mImageSmile.setImageDrawable(getIconLike(timeline.getStatusLikeId()));

            if (mAdapterComment == null) {
                mListComment = new ArrayList<>();
                mAdapterComment = new CommentAdapter(mContext, mListComment);
                mListViewComment.setAdapter(mAdapterComment);
            }
            String urlImage = timeline.getImages();
            if (getItemViewType() == SHOW_IMAGE) {
                try {
                    JSONArray jsonArray = new JSONArray(timeline.getImages());
                    if (jsonArray != null && jsonArray.length() > 0) {
                        urlImage = jsonArray.getString(0);
                        Glide.with(mContext)
                                .load(urlImage)
                                .error(mContent.getResources().getDrawable(R.drawable.image_time_line))
                                .skipMemoryCache(false)
                                .into(mImageTimeline);
                    }
                } catch (JSONException e) {
                    Glide.with(mContext)
                            .load(urlImage)
                            .error(mContent.getResources().getDrawable(R.drawable.image_time_line))
                            .skipMemoryCache(false)
                            .into(mImageTimeline);
                    e.printStackTrace();
                }

            } else {
                mImageTimeline.setVisibility(View.GONE);
            }
            Utils.fillImageTimeline(mContext, urlImage, mImageStore);
            mTime.setText(Utils.distanceTimes(timeline.getCreated()));
            mContent.setText(timeline.getMessages());
            final String numberComment = mContent.getResources().getString(R.string.number_comment, String.valueOf(timeline.getCommentNumber()));
            mNumberComment.setText(numberComment);
            mButtonComment.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mLayoutComment.getVisibility() == View.GONE) {
                        mLayoutComment.setVisibility(View.VISIBLE);
                        getListComment(timeline);
                    } else {
                        mLayoutComment.setVisibility(View.GONE);
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
                                mPresenter.addComment(timeline.getId(), commentString, new TimeLinePresenter.IOnCommentCallback() {

                                    @Override
                                    public void onCommentSuccess(String message) {
                                        statusButtonSend(mButtonSendComment, mProgressSend);
                                        timeline.setCommentNumber(timeline.getCommentNumber() + 1);
                                        final String numberComment = mContent.getResources().getString(R.string.number_comment, String.valueOf(timeline.getCommentNumber()));
                                        mNumberComment.setText(numberComment);
                                        mInputComment.setText(Constants.EMPTY_STRING);
                                        mProgressSend.setVisibility(View.GONE);
                                        getListComment(timeline);
                                    }

                                    @Override
                                    public void onCommentFailure(String message) {
                                        statusButtonSend(mButtonSendComment, mProgressSend);
                                        mProgressSend.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }, 1000);

                    }
                }
            });

            mImageSmile.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (mReactionFacebook.getVisibility() == View.VISIBLE) {
                        mReactionFacebook.dismiss();
                    } else {
                        mReactionFacebook.show();
                    }
                }
            });
            mReactionFacebook.setItemIconLikeClick(new ReactionView.IListenerClickIconLike() {

                @Override
                public void onItemClick(int likeId) {
                    Logger.d("onItemClick", "likeId " + likeId);
                    likeTimeline(timeline.getId(), likeId, 1);
                }
            });
        }

        private void likeTimeline(int timelineId, final int likeId, int typeLike) {
            mPresenter.likeTimeline(timelineId, likeId, typeLike, new TimeLinePresenter.IOnLikeTimelineCallback() {

                @Override
                public void onCommentSuccess(String message) {
                    Logger.d("likeTimeline", "onCommentSuccess ");
                    mTextLike.setText(getStringLike(likeId));
                    mImageSmile.setImageDrawable(getIconLike(likeId));
                }

                @Override
                public void onCommentFailure(String message) {
                    Logger.d("likeTimeline", "onCommentFailure ");
                }
            });
        }

        private void getListComment(TimeLineResponse.TimeLineData.ListTimeline.Timeline response) {
            mProgressListView.setVisibility(View.VISIBLE);
            mPresenter.getListComment(response.getId(), new TimeLinePresenter.IOnGetListCommentCallback() {

                @Override
                public void onGetListCommentSuccess(CommentResponse.CommentData commentData) {
                    mProgressListView.setVisibility(View.GONE);
                    if (commentData != null) {
                        mListComment.clear();
                        mListComment.addAll(commentData.getListComment());
                        Collections.reverse(mListComment);
                        mAdapterComment.notifyDataSetChanged();
                    }
                }

                @Override
                public void onGetListCommentFailure(String message) {
                    mProgressListView.setVisibility(View.GONE);
                }
            });
        }

        private String getStringLike(int likeID) {
            String like;
            switch (likeID) {
                case 1:
                    like = Constants.Like.LIKE;
                    break;
                case 2:
                    like = Constants.Like.LOVE;
                    break;
                case 3:
                    like = Constants.Like.HAHA;
                    break;
                case 4:
                    like = Constants.Like.WOW;
                    break;
                case 5:
                    like = Constants.Like.CRY;
                    break;
                case 6:
                    like = Constants.Like.ANGRY;
                    break;
                default:
                    like = "Thích";

            }
            return like;
        }

        private Drawable getIconLike(int likeID) {
            Drawable like;
            switch (likeID) {
                case 1:
                    like = mContext.getResources().getDrawable(R.drawable.like);
                    break;
                case 2:
                    like = mContext.getResources().getDrawable(R.drawable.love);
                    break;
                case 3:
                    like = mContext.getResources().getDrawable(R.drawable.haha);
                    break;
                case 4:
                    like = mContext.getResources().getDrawable(R.drawable.wow);
                    break;
                case 5:
                    like = mContext.getResources().getDrawable(R.drawable.cry);
                    break;
                case 6:
                    like = mContext.getResources().getDrawable(R.drawable.angry);
                    break;
                default:
                    like = mContext.getResources().getDrawable(R.drawable.icon_smile);
            }
            return like;
        }

    }

    private void statusButtonSend(View buttonSend, View progress) {
        if (buttonSend.getVisibility() == View.VISIBLE) {
            buttonSend.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
        } else {
            buttonSend.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        }
    }

}
