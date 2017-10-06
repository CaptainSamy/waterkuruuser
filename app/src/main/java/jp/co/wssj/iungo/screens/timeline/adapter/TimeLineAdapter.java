package jp.co.wssj.iungo.screens.timeline.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    public static final String TAG = "TimeLineAdapter";

    private static final int NOT_SHOW_IMAGE = 0;

    private static final int SHOW_IMAGE = 1;

    private List<TimeLineResponse.TimeLineData.ListTimeline> mListTimeLine;

    private TimeLinePresenter mPresenter;

    private IRefreshTimeline mRefreshTimeline;

    private IActivityCallback mActivityCallback;

    private RecyclerView mRecycleView;

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
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecycleView = recyclerView;
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

        private ImageView mImageStore, mImageSmile;

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

        private ImageView mImagePhoto1;

        public TimeLineHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            mLayoutInflate = LayoutInflater.from(mContext);
            mImageStore = (ImageView) itemView.findViewById(R.id.ivStore);
            mTextLike = (TextView) itemView.findViewById(R.id.tvLike);
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
            mStoreName.setText(mTimeLine.getManagerName());
            showIconLike();
            if (getItemViewType() == SHOW_IMAGE) {
                mLayoutContainerImages.setVisibility(View.VISIBLE);
                mLayoutContainerImages.removeAllViews();
                try {
                    mListUrlImage = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(mTimeLine.getImages());
                    int length = jsonArray.length();
                    for (int i = 0; i < length; i++) {
                        mListUrlImage.add(jsonArray.getString(i));
                    }
                    switch (length) {
                        case 1:
                            ImageView viewOneImages = (ImageView) mLayoutInflate.inflate(R.layout.one_image, null);
                            mImagePhoto1 = viewOneImages;
                            viewOneImages.setOnClickListener(this);
                            fillImage(jsonArray.getString(0), viewOneImages);
                            mLayoutContainerImages.addView(viewOneImages);
                            break;
                        case 2:
                            View viewTwoImages = mLayoutInflate.inflate(R.layout.two_images, null);
                            fillTwoImages(viewTwoImages, jsonArray.getString(0), jsonArray.getString(1));
                            mLayoutContainerImages.addView(viewTwoImages);
                            break;
                        case 3:
                            View viewThreeImages = mLayoutInflate.inflate(R.layout.three_images, null);
                            fillThreeImages(viewThreeImages, jsonArray.getString(0), jsonArray.getString(1), jsonArray.getString(2));
                            mLayoutContainerImages.addView(viewThreeImages);
                            break;
                        case 4:
                            View viewFourImages = mLayoutInflate.inflate(R.layout.four_images, null);
                            fillFourImages(viewFourImages, jsonArray.getString(0), jsonArray.getString(1), jsonArray.getString(2), jsonArray.getString(3));
                            mLayoutContainerImages.addView(viewFourImages);
                            break;
                        case 5:
                            View viewFiveImages = mLayoutInflate.inflate(R.layout.five_images, null);
                            fillFiveImages(viewFiveImages, jsonArray.getString(0), jsonArray.getString(1), jsonArray.getString(2), jsonArray.getString(3), jsonArray.getString(4));
                            mLayoutContainerImages.addView(viewFiveImages);
                            break;
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
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
                mLayoutContainerImages.setVisibility(View.GONE);
            }
            Utils.fillImageRound(mContext, mTimeLine.getImageStore(), mImageStore);
            mTime.setText(Utils.distanceTimes(mTimeLine.getCreated()));
            if (TextUtils.isEmpty(mTimeLine.getMessages())) {
                mContent.setVisibility(View.GONE);
            } else {
                mContent.setVisibility(View.VISIBLE);
                mContent.setText(mTimeLine.getMessages());
            }
            mLayoutLike.setOnClickListener(this);
//            mLayoutLike.setOnLongClickListener(new View.OnLongClickListener() {
//
//                @Override
//                public boolean onLongClick(View v) {
//                    Logger.d(TAG, "onLongClick");
//                    mReactionFacebook.show();
//                    CustomLinearLayoutManager layoutManager = (CustomLinearLayoutManager) mRecycleView.getLayoutManager();
//                    layoutManager.setScrollable(false);
//                    return true;
//                }
//            });
//            mLayoutLike.setOnTouchListener(new View.OnTouchListener() {
//
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (event.getAction() == MotionEvent.ACTION_UP) {
//                        if (mReactionFacebook.getVisibility() == View.VISIBLE) {
//                            mReactionFacebook.dismiss();
//                            CustomLinearLayoutManager layoutManager = (CustomLinearLayoutManager) mRecycleView.getLayoutManager();
//                            layoutManager.setScrollable(true);
//                            Logger.d(TAG, "ACTION_UP && onLongClick");
//                            return mReactionFacebook.onTouchEvent(event);
//                        }
//                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                        if (mReactionFacebook.getVisibility() == View.VISIBLE) {
//                            return mReactionFacebook.onTouchEvent(event);
//                        }
//
//                    }
//                    return false;
//                }
//            });
            mLayoutComment.setOnClickListener(this);
            mReactionFacebook.setItemIconLikeClick(new ReactionView.IListenerClickIconLike() {

                @Override
                public void onItemClick(int likeId) {
                    boolean isExitsLike = false;
                    if (likeId != mTimeLine.getStatusLikeId()) {
                        if (mTimeLine.getStatusLikeId() == 0) {
                            mTimeLine.setNumberLike(mTimeLine.getNumberLike() + 1);
                        } else {
                            isExitsLike = true;
                        }
                        mTimeLine.setStatusLikeId(likeId);
                        for (TimeLineResponse.TimeLineData.ListTimeline.Like like : mListLike) {
                            if (likeId == like.getLikeId()) {
                                isExitsLike = true;
                                break;
                            }
                        }
                        if (!isExitsLike || mListLike.size() == 0) {
                            TimeLineResponse.TimeLineData.ListTimeline.Like like = new TimeLineResponse.TimeLineData.ListTimeline.Like(likeId);
                            mListLike.add(like);
                        }
                        showIconLike();
                        likeTimeline(mTimeLine.getId(), likeId, mTimeLine.getStatusLikeId(), 1);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layoutLike:
                    if (mTimeLine.getStatusLikeId() == 0) {
                        if (mReactionFacebook.getVisibility() == View.VISIBLE) {
                            mReactionFacebook.dismiss();
                        } else {
                            mReactionFacebook.show();
                        }
                    } else {
                        int typeLike = 0;
                        int newLike = 0;
                        likeTimeline(mTimeLine.getId(), newLike, mTimeLine.getStatusLikeId(), typeLike);
                        mImageSmile.setImageDrawable(getIconLike(newLike));
                        mTextLike.setText(getStringLike(newLike));
                        mTimeLine.setStatusLikeId(newLike);
                        mTimeLine.setNumberLike(mTimeLine.getNumberLike() - 1);
                        showIconLike();
                    }
                    break;
                case R.id.layoutComment:
                    if (mListUrlImage != null && mListUrlImage.size() > 0) {
                        commentFragment(0, mImagePhoto1);
                    } else {
                        commentFragment(0, null);
                    }
                    break;
                case R.id.image1:
                    commentFragment(0, (ImageView) v);
                    break;
                case R.id.image2:
                    commentFragment(1, (ImageView) v);
                    break;
                case R.id.image3:
                    commentFragment(2, (ImageView) v);
                    break;
                case R.id.image4:
                    commentFragment(3, (ImageView) v);
                    break;
                case R.id.image5:
                    commentFragment(4, (ImageView) v);
                    break;
            }
        }

        private void commentFragment(int positionClick, ImageView imageView) {
            if (mActivityCallback != null) {
                if (imageView != null) {
                    String urlImage = mListUrlImage.get(positionClick);
                    String fileName = urlImage.substring(urlImage.lastIndexOf("/") + 1);
                    ViewCompat.setTransitionName(imageView, fileName);
                }
                mRefreshTimeline.onRefreshTimeline(imageView);
                Bundle bundle = new Bundle();
                bundle.putInt(CommentFragment.KEY_TIME_LIKE_ID, mTimeLine.getId());
                bundle.putInt(CommentFragment.KEY_ITEM_POSITION, positionClick);
                bundle.putStringArrayList(CommentFragment.KEY_LIST_ITEMS, (ArrayList<String>) mListUrlImage);
                mActivityCallback.displayScreen(IMainView.FRAGMENT_COMMENT, true, true, bundle);

//                mActivityCallback.onComment(imageView, mListUrlImage, positionClick);

//                ViewCompat.setTransitionName(imageView, "simple_activity_transition");
//                Intent intent = new Intent(mContext, CommentActivity.class);
//                ActivityOptionsCompat options = ActivityOptionsCompat.
//                        makeSceneTransitionAnimation((Activity) mContext,
//                                imageView,
//                                ViewCompat.getTransitionName(imageView));
//                intent.putStringArrayListExtra("LIST_URL", (ArrayList<String>) mListUrlImage);
//                intent.putExtra("POSITION", positionClick);
//                mContext.startActivity(intent, options.toBundle());

            }
        }

        private void likeTimeline(int timelineId, final int newLikeId, int newLineId, int typeLike) {
            mPresenter.likeTimeline(timelineId, newLikeId, newLineId, typeLike, new TimeLinePresenter.IOnLikeCallback() {

                @Override
                public void onLikeSuccess(String message) {
                    Logger.d("likeTimeline", "onCommentSuccess ");
                }

                @Override
                public void onLikeFailure(String message) {
                    Logger.d("likeTimeline", "onCommentFailure ");
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
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
            mImageSmile.setImageDrawable(getIconLike(mTimeLine.getStatusLikeId()));
            mTextLike.setText(getStringLike(mTimeLine.getStatusLikeId()));
            if (mTimeLine.getNumberLike() == 0 && mTimeLine.getCommentNumber() == 0) {
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
            if (mListLike != null && mListLike.size() > 0) {
                int myLike = mTimeLine.getStatusLikeId();
                if (mListLike.size() == 1) {
                    if (myLike == 0) {
                        mImage1.setImageDrawable(getIconLike(mListLike.get(0).getLikeId()));
                    } else {
                        mImage1.setImageDrawable(getIconLike(myLike));
                    }
                    mImage1.setVisibility(View.VISIBLE);
                    mImage2.setVisibility(View.GONE);
                    mImage3.setVisibility(View.GONE);
                } else if (mListLike.size() == 2) {
                    int likeId1 = mListLike.get(0).getLikeId();
                    int likeId2 = mListLike.get(1).getLikeId();
                    if (myLike == 0) {
                        mImage1.setImageDrawable(getIconLike(likeId1));
                        mImage2.setImageDrawable(getIconLike(likeId2));
                    } else {
                        int chooseLike = (likeId1 == myLike) ? likeId2 : likeId1;
                        mImage1.setImageDrawable(getIconLike(myLike));
                        mImage2.setImageDrawable(getIconLike(chooseLike));
                    }
                    mImage1.setVisibility(View.VISIBLE);
                    mImage2.setVisibility(View.VISIBLE);
                    mImage3.setVisibility(View.GONE);
                } else {
                    int likeId1 = mListLike.get(0).getLikeId();
                    int likeId2 = mListLike.get(1).getLikeId();
                    int likeId3 = mListLike.get(2).getLikeId();
                    if (myLike == 0) {
                        mImage1.setImageDrawable(getIconLike(likeId1));
                        mImage2.setImageDrawable(getIconLike(likeId2));
                        mImage3.setImageDrawable(getIconLike(likeId3));
                    } else {
                        mImage1.setImageDrawable(getIconLike(myLike));
                        int chooseLike2 = (myLike != likeId1) ? likeId1 : likeId2;
                        int chooseLike3 = (myLike != likeId3) ? likeId3 : likeId2;
                        mImage2.setImageDrawable(getIconLike(chooseLike2));
                        mImage3.setImageDrawable(getIconLike(chooseLike3));
                    }
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
                    .placeholder(R.drawable.ic_add_image)
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
                            Logger.d("fillImage", "onResourceReady");
                            imageView.setImageDrawable(resource);
                            imageView.setTag(R.id.shared_drawable, resource);
                        }
                    });
        }

        private void fillTwoImages(View view, String urlImage1, String urlImage2) {
            ImageView image1 = (ImageView) view.findViewById(R.id.image1);
            mImagePhoto1 = image1;
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

        void onRefreshTimeline(ImageView view);
    }

    public void setRefreshTimeline(IRefreshTimeline refreshTimeline) {
        this.mRefreshTimeline = refreshTimeline;
    }
}
