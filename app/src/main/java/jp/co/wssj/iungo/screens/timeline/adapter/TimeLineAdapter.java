package jp.co.wssj.iungo.screens.timeline.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.timeline.TimeLineResponse;
import jp.co.wssj.iungo.screens.IActivityCallback;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.comment.CommentFragment;
import jp.co.wssj.iungo.screens.timeline.TimeLinePresenter;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;
import jp.co.wssj.iungo.utils.Utils;
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

    private IRefreshTimeline mRefreshTimeline;

    private IActivityCallback mActivityCallback;

    public TimeLineAdapter(List<TimeLineResponse.TimeLineData.ListTimeline> listTimeline, TimeLinePresenter presenter, IActivityCallback activityCallback) {
        mListTimeLine = listTimeline;
        mPresenter = presenter;
        mActivityCallback = activityCallback;
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

    public class TimeLineHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Context mContext;

        private ExpandableTextView mContent;

        private ImageView mImageStore, mImageTimeline, mImageSmile;

        private TextView mTextLike, mStoreName, mNumberLike;

        private ImageView mImage1, mImage2, mImage3;

        private ReactionView mReactionFacebook;

        private LinearLayout mLayoutLike, mLayoutComment;

        private TextView mNumberComment, mTime;

        private TimeLineResponse.TimeLineData.ListTimeline.Timeline mTimeLine;

        private List<TimeLineResponse.TimeLineData.ListTimeline.Like> mListLike;

        public TimeLineHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            mImageStore = (ImageView) itemView.findViewById(R.id.ivStore);
            mTextLike = (TextView) itemView.findViewById(R.id.tvLike);
            mImageTimeline = (ImageView) itemView.findViewById(R.id.ivTimeLine);
            mNumberComment = (TextView) itemView.findViewById(R.id.tvNumberComment);
            mContent = (ExpandableTextView) itemView.findViewById(R.id.tvContent);
            mImageSmile = (ImageView) itemView.findViewById(R.id.ivSmile);
            mLayoutLike = (LinearLayout) itemView.findViewById(R.id.layoutLike);
            mLayoutComment = (LinearLayout) itemView.findViewById(R.id.layoutComment);
            mTime = (TextView) itemView.findViewById(R.id.tvTime);
            mReactionFacebook = (ReactionView) itemView.findViewById(R.id.reactionFacebook);
            mStoreName = (TextView) itemView.findViewById(R.id.tvStoreName);
            mNumberLike = (TextView) itemView.findViewById(R.id.tvNumberLike);
            mImage1 = (ImageView) itemView.findViewById(R.id.image1);
            mImage2 = (ImageView) itemView.findViewById(R.id.image2);
            mImage3 = (ImageView) itemView.findViewById(R.id.image3);
        }

        public void bind(final TimeLineResponse.TimeLineData.ListTimeline listTimeline, final int position) {
            mTimeLine = listTimeline.getTimeline();
            mListLike = listTimeline.getLikes();

            mImageSmile.setImageDrawable(getIconLike(mTimeLine.getStatusLikeId()));
            mTextLike.setText(getStringLike(mTimeLine.getStatusLikeId()));
            mStoreName.setText(mTimeLine.getManagerName());
            if (mListLike != null && mListLike.size() > 0) {
                if (mListLike.size() == 1) {
                    TimeLineResponse.TimeLineData.ListTimeline.Like like = mListLike.get(0);
                    mImage1.setImageDrawable(getIconLike(like.getLikeId()));
                    mImage1.setVisibility(View.VISIBLE);
                } else if (mListLike.size() == 2) {
                    mImage1.setImageDrawable(getIconLike(mListLike.get(0).getLikeId()));
                    mImage2.setImageDrawable(getIconLike(mListLike.get(1).getLikeId()));
                    mImage1.setVisibility(View.VISIBLE);
                    mImage2.setVisibility(View.VISIBLE);

                } else {
                    mImage3.setImageDrawable(getIconLike(mListLike.get(2).getLikeId()));
                    mImage2.setImageDrawable(getIconLike(mListLike.get(1).getLikeId()));
                    mImage1.setImageDrawable(getIconLike(mListLike.get(0).getLikeId()));
                    mImage3.setVisibility(View.VISIBLE);
                    mImage2.setVisibility(View.VISIBLE);
                    mImage1.setVisibility(View.VISIBLE);
                }

                mNumberLike.setText(String.valueOf(mTimeLine.getNumberLike()));
            }
            String urlImage = mTimeLine.getImages();
            if (getItemViewType() == SHOW_IMAGE) {
                try {
                    JSONArray jsonArray = new JSONArray(mTimeLine.getImages());
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
            Utils.fillImageRound(mContext, urlImage, mImageStore);
            mTime.setText(Utils.distanceTimes(mTimeLine.getCreated()));
            mContent.setText(mTimeLine.getMessages());
            final String numberComment = mContent.getResources().getString(R.string.number_comment, String.valueOf(mTimeLine.getCommentNumber()));
            mNumberComment.setText(numberComment);
            mLayoutLike.setOnClickListener(this);
            mLayoutComment.setOnClickListener(this);
            mReactionFacebook.setItemIconLikeClick(new ReactionView.IListenerClickIconLike() {

                @Override
                public void onItemClick(int likeId) {
                    Logger.d("onItemClick", "likeId " + likeId);
                    likeTimeline(mTimeLine.getId(), likeId, mTimeLine.getStatusLikeId(), 1);
                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layoutLike:
                    if (mReactionFacebook.getVisibility() == View.VISIBLE) {
                        mReactionFacebook.dismiss();
                    } else {
                        mReactionFacebook.show();
                    }
                    break;
                case R.id.layoutComment:
                    if (mActivityCallback != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(CommentFragment.KEY_TIME_LIKE_ID, mTimeLine.getId());
                        mActivityCallback.displayScreen(IMainView.FRAGMENT_COMMENT, true, true, bundle);
                    }
                    break;
            }
        }

        private void likeTimeline(int timelineId, final int newLikeId, int newLineId, int typeLike) {
            mPresenter.likeTimeline(timelineId, newLikeId, newLineId, typeLike, new TimeLinePresenter.IOnLikeCallback() {

                @Override
                public void onLikeSuccess(String message) {
                    Logger.d("likeTimeline", "onCommentSuccess ");
                    mTextLike.setText(getStringLike(newLikeId));
                    mImageSmile.setImageDrawable(getIconLike(newLikeId));
                    if (mRefreshTimeline != null) {
                        mRefreshTimeline.onRefreshTimeline();
                    }
                }

                @Override
                public void onLikeFailure(String message) {
                    Logger.d("likeTimeline", "onCommentFailure ");
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

    public interface IRefreshTimeline {

        void onRefreshTimeline();
    }

    public void setRefreshTimeline(IRefreshTimeline refreshTimeline) {
        this.mRefreshTimeline = refreshTimeline;
    }
}
