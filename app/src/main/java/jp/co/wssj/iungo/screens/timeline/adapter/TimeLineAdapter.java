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
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.timeline.TimeLineResponse;
import jp.co.wssj.iungo.screens.IActivityCallback;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.comment.CommentFragment;
import jp.co.wssj.iungo.screens.phototimeline.PhotoTimelineDialog;
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

    private static final int NOT_SHOW_IMAGE = 0;

    private static final int SHOW_IMAGE = 1;

    private static final int ONE_IMAGE = 1;

    private static final int TWO_IMAGES = 2;

    private static final int THREE_IMAGES = 3;

    private static final int FOUR_IMAGES = 4;

    private static final int FIVE_IMAGES = 5;

    private List<TimeLineResponse.TimeLineData.ListTimeline> mListTimeLine;

    private TimeLinePresenter mPresenter;

    private IRefreshTimeline mRefreshTimeline;

    private IActivityCallback mActivityCallback;

    public TimeLineAdapter(List<TimeLineResponse.TimeLineData.ListTimeline> listTimeline, TimeLinePresenter presenter, IActivityCallback activityCallback) {
        mListTimeLine = listTimeline;
        mPresenter = presenter;
        mActivityCallback = activityCallback;
    }

    public void refreshList(List<TimeLineResponse.TimeLineData.ListTimeline> listTimeline, int page) {
        if (listTimeline != null && mListTimeLine != null) {
            if (page == Constants.INIT_PAGE) {
                mListTimeLine.clear();
            }
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

        private LinearLayout mLayoutLike, mLayoutComment, mLayoutNumberLike;

        private ViewGroup mLayoutContainerImages;

        private TextView mNumberComment, mTime;

        private LayoutInflater mLayoutInflate;

        private TimeLineResponse.TimeLineData.ListTimeline.Timeline mTimeLine;

        private List<TimeLineResponse.TimeLineData.ListTimeline.Like> mListLike;

        private List<String> mListUrlImage;

        private PhotoTimelineDialog mDialogPhoto;

        public TimeLineHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            mLayoutInflate = LayoutInflater.from(mContext);
            mImageStore = (ImageView) itemView.findViewById(R.id.ivStore);
            mTextLike = (TextView) itemView.findViewById(R.id.tvLike);
            mImageTimeline = (ImageView) itemView.findViewById(R.id.ivTimeLine);
            mNumberComment = (TextView) itemView.findViewById(R.id.tvNumberComment);
            mContent = (ExpandableTextView) itemView.findViewById(R.id.tvContent);
            mLayoutNumberLike = (LinearLayout) itemView.findViewById(R.id.layoutNumberLike);
            mImageSmile = (ImageView) itemView.findViewById(R.id.ivSmile);
            mLayoutLike = (LinearLayout) itemView.findViewById(R.id.layoutLike);
            mLayoutComment = (LinearLayout) itemView.findViewById(R.id.layoutComment);
            mLayoutContainerImages = (ViewGroup) itemView.findViewById(R.id.layoutContainerImages);
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
            if (mDialogPhoto == null) {
                mDialogPhoto = new PhotoTimelineDialog(mContext);
            }
            if (mTimeLine.getCommentNumber() == 0 && mTimeLine.getNumberLike() == 0) {
                mLayoutNumberLike.setVisibility(View.GONE);
            } else {
                mLayoutNumberLike.setVisibility(View.VISIBLE);
            }
            if (mTimeLine.getCommentNumber() > 0) {
                final String numberComment = mContent.getResources().getString(R.string.number_comment, String.valueOf(mTimeLine.getCommentNumber()));
                mNumberComment.setText(numberComment);
                mNumberComment.setVisibility(View.VISIBLE);
            } else {
                mNumberComment.setVisibility(View.GONE);
            }
            mImageSmile.setImageDrawable(getIconLike(mTimeLine.getStatusLikeId()));
            mTextLike.setText(getStringLike(mTimeLine.getStatusLikeId()));
            mStoreName.setText(mTimeLine.getManagerName());
            showIconLike();
            if (getItemViewType() == SHOW_IMAGE) {
                mLayoutContainerImages.removeAllViews();
                try {
                    mListUrlImage = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(mTimeLine.getImages());
                    int length = jsonArray.length();
                    for (int i = 0; i < length; i++) {
                        mListUrlImage.add(jsonArray.getString(i));
                    }
                    switch (length) {
                        case ONE_IMAGE:
                            mImageTimeline.setVisibility(View.GONE);
                            ImageView viewOneImages = (ImageView) mLayoutInflate.inflate(R.layout.one_image, null);
                            viewOneImages.setOnClickListener(this);
                            fillImage(jsonArray.getString(0), viewOneImages);
                            mLayoutContainerImages.addView(viewOneImages);
                            break;
                        case TWO_IMAGES:
                            mImageTimeline.setVisibility(View.GONE);
                            View viewTwoImages = mLayoutInflate.inflate(R.layout.two_images, null);
                            fillTwoImages(viewTwoImages, jsonArray.getString(0), jsonArray.getString(1));
                            mLayoutContainerImages.addView(viewTwoImages);
                            break;
                        case THREE_IMAGES:
                            mImageTimeline.setVisibility(View.GONE);
                            View viewThreeImages = mLayoutInflate.inflate(R.layout.three_images, null);
                            fillThreeImages(viewThreeImages, jsonArray.getString(0), jsonArray.getString(1), jsonArray.getString(2));
                            mLayoutContainerImages.addView(viewThreeImages);
                            break;
                        case FOUR_IMAGES:
                            mImageTimeline.setVisibility(View.GONE);
                            View viewFourImages = mLayoutInflate.inflate(R.layout.four_images, null);
                            fillFourImages(viewFourImages, jsonArray.getString(0), jsonArray.getString(1), jsonArray.getString(2), jsonArray.getString(3));
                            mLayoutContainerImages.addView(viewFourImages);
                            break;
                        case FIVE_IMAGES:
                            mImageTimeline.setVisibility(View.GONE);
                            View viewFiveImages = mLayoutInflate.inflate(R.layout.five_images, null);
                            fillFiveImages(viewFiveImages, jsonArray.getString(0), jsonArray.getString(1), jsonArray.getString(2), jsonArray.getString(3), jsonArray.getString(4));
                            mLayoutContainerImages.addView(viewFiveImages);
                            break;
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                            mImageTimeline.setVisibility(View.GONE);
                            View viewMoreFiveImages = mLayoutInflate.inflate(R.layout.more_five_images, null);
                            fillFiveImages(viewMoreFiveImages, jsonArray.getString(0), jsonArray.getString(1), jsonArray.getString(2), jsonArray.getString(3), jsonArray.getString(4));
                            TextView viewRestImage = (TextView) viewMoreFiveImages.findViewById(R.id.tvNumberRestImages);
                            int restImages = length - 5;
                            String numberRestImage = mContext.getResources().getString(R.string.number_rest_image, String.valueOf(restImages));
                            viewRestImage.setText(numberRestImage);
                            mLayoutContainerImages.addView(viewMoreFiveImages);
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                mImageTimeline.setVisibility(View.GONE);
            }
            Utils.fillImageRound(mContext, Constants.EMPTY_STRING, mImageStore);
            mTime.setText(Utils.distanceTimes(mTimeLine.getCreated()));
            mContent.setText(mTimeLine.getMessages());
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
                case R.id.image1:
                    mDialogPhoto.showImages(mListUrlImage, 0);
                    break;
                case R.id.image2:
                    mDialogPhoto.showImages(mListUrlImage, 1);
                    break;
                case R.id.image3:
                    mDialogPhoto.showImages(mListUrlImage, 2);
                    break;
                case R.id.image4:
                    mDialogPhoto.showImages(mListUrlImage, 3);
                    break;
                case R.id.image5:
                    mDialogPhoto.showImages(mListUrlImage, 4);
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
                    like = mContext.getString(R.string.like);

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

        private void showIconLike() {
            if (mListLike != null && mListLike.size() > 0) {
                if (mListLike.size() == 1) {
                    TimeLineResponse.TimeLineData.ListTimeline.Like like = mListLike.get(0);
                    mImage1.setImageDrawable(getIconLike(like.getLikeId()));
                    mImage1.setVisibility(View.VISIBLE);
                    mImage2.setVisibility(View.GONE);
                    mImage3.setVisibility(View.GONE);
                } else if (mListLike.size() == 2) {
                    mImage1.setImageDrawable(getIconLike(mListLike.get(0).getLikeId()));
                    mImage2.setImageDrawable(getIconLike(mListLike.get(1).getLikeId()));
                    mImage1.setVisibility(View.VISIBLE);
                    mImage2.setVisibility(View.VISIBLE);
                    mImage3.setVisibility(View.GONE);
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
        }

        private void fillImage(String url, final ImageView imageView) {
            Glide.with(mContext)
                    .load(url)
                    .error(mContext.getResources().getDrawable(R.drawable.image_time_line))
                    .placeholder(R.drawable.ic_add_image)
                    .skipMemoryCache(false)
                    .into(new SimpleTarget<GlideDrawable>() {

                        @Override
                        public void onLoadStarted(Drawable placeholder) {
                            imageView.setImageDrawable(placeholder);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            imageView.setImageDrawable(errorDrawable);
                        }

                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            imageView.setImageDrawable(resource);
                        }
                    });
        }

        private void fillTwoImages(View view, String urlImage1, String urlImage2) {
            ImageView image1 = (ImageView) view.findViewById(R.id.image1);
            image1.setOnClickListener(this);
            fillImage(urlImage1, image1);
            ImageView image2 = (ImageView) view.findViewById(R.id.image2);
            image2.setOnClickListener(this);
            fillImage(urlImage2, image2);
        }

        private void fillThreeImages(View view, String urlImage1, String urlImage2, String urlImage3) {
            fillTwoImages(view, urlImage1, urlImage2);
            ImageView image3 = (ImageView) view.findViewById(R.id.image3);
            image3.setOnClickListener(this);
            fillImage(urlImage3, image3);
        }

        private void fillFourImages(View view, String urlImage1, String urlImage2, String urlImage3, String urlImage4) {
            fillThreeImages(view, urlImage1, urlImage2, urlImage3);
            ImageView image4 = (ImageView) view.findViewById(R.id.image4);
            image4.setOnClickListener(this);
            fillImage(urlImage4, image4);
        }

        private void fillFiveImages(View view, String urlImage1, String urlImage2, String urlImage3, String urlImage4, String urlImage5) {
            fillFourImages(view, urlImage1, urlImage2, urlImage3, urlImage4);
            ImageView image5 = (ImageView) view.findViewById(R.id.image5);
            image5.setOnClickListener(this);
            fillImage(urlImage5, image5);
        }
    }

    public interface IRefreshTimeline {

        void onRefreshTimeline();
    }

    public void setRefreshTimeline(IRefreshTimeline refreshTimeline) {
        this.mRefreshTimeline = refreshTimeline;
    }
}
