package wssj.co.jp.olioa.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.apache.commons.lang.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.screens.comment.CommentFragment;
import wssj.co.jp.olioa.model.entities.VideoObject;
import wssj.co.jp.olioa.screens.IActivityCallback;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.PlayVideoActivity;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 6/10/2017.
 */

public class ViewContainerImages extends LinearLayout implements View.OnClickListener {

    private LayoutInflater mLayoutInflate;

//    private PhotoTimelineDialog mDialogPhoto;

    List<String> mListUrlImage = new ArrayList<>();

    List<String> listUrlVideo = new ArrayList<>();

    private ImageView mImageView1;

    private IActivityCallback mActivityCallback;

    private int mTimelineId;

    private String mTextStoreName;

    public ViewContainerImages(Context context, int timelineId, LinearLayout layoutComment, TextView numberComment, IActivityCallback activityCallback, String textStoreName) {
        super(context);
        mLayoutInflate = LayoutInflater.from(context);
        mTextStoreName = textStoreName;
        mTimelineId = timelineId;
        mActivityCallback = activityCallback;
        setOrientation(VERTICAL);
        layoutComment.setOnClickListener(this);
        numberComment.setOnClickListener(this);
    }

    public ViewContainerImages(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public ViewContainerImages(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void clearData() {
        if (mListUrlImage != null && listUrlVideo != null) {
            mListUrlImage.clear();
            listUrlVideo.clear();

        }
    }

    public void setListUrl(List<String> listUrl) {
        if (listUrl != null) {
            for (String url : listUrl) {
                if (url.contains(Constants.KEY_VIDEO)) {
                    listUrlVideo.add(url);
                } else {
                    mListUrlImage.add(url);
                }
            }
        }
        if (mListUrlImage.size() > 0) {
            displayListImages(mListUrlImage);
        }
        if (listUrlVideo.size() > 0) {
            displayListVideo(listUrlVideo);
        }
    }

    /*
    * display image
    *
    * */
    private void displayListImages(List<String> images) {
        switch (images.size()) {
            case 1:
                ImageView viewOneImages = (ImageView) mLayoutInflate.inflate(R.layout.one_image, null);
                viewOneImages.setOnClickListener(this);
                mImageView1 = viewOneImages;
                fillImage(images.get(0), viewOneImages);
                addView(viewOneImages);
                break;
            case 2:
                View viewTwoImages = mLayoutInflate.inflate(R.layout.two_images, null);
                fillTwoImages(viewTwoImages, images.get(0), images.get(1));
                addView(viewTwoImages);
                break;
            case 3:
                View viewThreeImages = mLayoutInflate.inflate(R.layout.three_images, null);
                fillThreeImages(viewThreeImages, images.get(0), images.get(1), images.get(2));
                addView(viewThreeImages);
                break;
            case 4:
                View viewFourImages = mLayoutInflate.inflate(R.layout.four_images, null);
                fillFourImages(viewFourImages, images.get(0), images.get(1), images.get(2), images.get(3));
                addView(viewFourImages);
                break;
            case 5:
                View viewFiveImages = mLayoutInflate.inflate(R.layout.five_images, null);
                fillFiveImages(viewFiveImages, images.get(0), images.get(1), images.get(2), images.get(3), images.get(4));
                addView(viewFiveImages);
                break;
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                View viewMoreFiveImages = mLayoutInflate.inflate(R.layout.more_five_images, null);
                fillFiveImages(viewMoreFiveImages, images.get(0), images.get(1), images.get(2), images.get(3), images.get(4));
                TextView viewRestImage = (TextView) viewMoreFiveImages.findViewById(R.id.tvNumberRestImages);
                int restImages = images.size() - 5;
                String numberRestImage = getContext().getResources().getString(R.string.number_rest_image, String.valueOf(restImages));
                viewRestImage.setText(numberRestImage);
                addView(viewMoreFiveImages);
                break;
        }
    }

    private void fillImage(String urlImage, final ImageView imageView) {
        Glide.with(getContext())
                .load(urlImage)
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
                        imageView.setTag(R.id.shared_drawable, resource);
                    }
                });
    }

    private void fillTwoImages(View view, String urlImage1, String urlImage2) {
        ImageView image1 = (ImageView) view.findViewById(R.id.image1);
        image1.setOnClickListener(this);
        mImageView1 = image1;
        fillImage(urlImage1, image1);
        ImageView image2 = (ImageView) view.findViewById(R.id.image2);
        fillImage(urlImage2, image2);
        image2.setOnClickListener(this);
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

    /*
    *
    * display video    *
    * */
    private VideoObject mVideoObject;

    private void displayListVideo(List<String> videos) {
        String urlVideo = videos.get(0);
        String[] splitVideo = urlVideo.split("\\" + Constants.KEY_SPLIT_FRAME_VIDEO);
        if (splitVideo.length == 3) {
            String textDuration;
            if (NumberUtils.isNumber(splitVideo[2])) {
                int duration = Integer.parseInt(splitVideo[2]);
                textDuration = Utils.convertDuration(duration);
            } else {
                textDuration = splitVideo[2];
            }


            mVideoObject = new VideoObject(splitVideo[0], splitVideo[1], textDuration);
            View viewVideo = mLayoutInflate.inflate(R.layout.item_video, null);
            ImageView frameVideo = (ImageView) viewVideo.findViewById(R.id.frameVideo);
            TextView tvDuration = (TextView) viewVideo.findViewById(R.id.tvDurationVideo);
            tvDuration.setVisibility(VISIBLE);
            tvDuration.setText(mVideoObject.getDuration());
            frameVideo.setOnClickListener(this);
            fillImage(mVideoObject.getFrameVideo(), frameVideo);
            addView(viewVideo);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image1:
                commentFragment(0, v);
                break;
            case R.id.image2:
                commentFragment(1, v);
                break;
            case R.id.image3:
                commentFragment(2, v);
                break;
            case R.id.image4:
                commentFragment(3, v);
                break;
            case R.id.image5:
                commentFragment(4, v);
                break;
            case R.id.frameVideo:
                Intent intent = new Intent(getContext(), PlayVideoActivity.class);
                intent.putExtra(PlayVideoActivity.KEY_URL_VIDEO, mVideoObject.getUrlVideo());
                getContext().startActivity(intent);
                break;
            case R.id.tvNumberComment:
            case R.id.layoutComment:
                if (mListUrlImage != null && mListUrlImage.size() > 0) {
                    commentFragment(0, mImageView1);
                } else {
                    commentFragment(0, null);
                }
                break;
        }
    }

    private void commentFragment(int positionClick, View sharedView) {
        if (mActivityCallback != null) {
            if (sharedView != null) {
                String urlImage = mListUrlImage.get(positionClick);
                String fileName = urlImage.substring(urlImage.lastIndexOf("/") + 1);
                ViewCompat.setTransitionName(sharedView, fileName);
            }
            Bundle bundle = new Bundle();
            bundle.putInt(CommentFragment.KEY_TIME_LIKE_ID, mTimelineId);
            bundle.putInt(CommentFragment.KEY_ITEM_POSITION, positionClick);
            bundle.putString(CommentFragment.KEY_STORE_NAME, mTextStoreName);
            bundle.putStringArrayList(CommentFragment.KEY_LIST_ITEMS, (ArrayList<String>) mListUrlImage);
            mActivityCallback.displayScreen(IMainView.FRAGMENT_COMMENT, true, true, bundle, sharedView);

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

}
