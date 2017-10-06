package jp.co.wssj.iungo.screens;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import io.github.rockerhieu.emojicon.EmojiconEditText;
import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.timeline.CommentResponse;
import jp.co.wssj.iungo.screens.comment.CommentPresenter;
import jp.co.wssj.iungo.screens.timeline.adapter.CommentAdapter;
import jp.co.wssj.iungo.screens.transition.TransitionAdapter;
import jp.co.wssj.iungo.utils.Utils;
import jp.co.wssj.iungo.widget.CirclePageIndicator;
import jp.co.wssj.iungo.widget.CustomViewPager;

/**
 * Created by Nguyen Huu Ta on 4/10/2017.
 */

public class CommentActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.postponeEnterTransition(this);
        supportPostponeEnterTransition();
        setContentView(R.layout.comment_activity);
        Intent intent = getIntent();
        ArrayList<String> list = intent.getStringArrayListExtra("LIST_URL");
        int positionClick = intent.getIntExtra("POSITION", 0);
        initView();
        initData(list, positionClick);
//        ImageView imageView = (ImageView) findViewById(R.id.glide_activity_b_image);
//        Intent intent = getIntent();
//        ArrayList<String> list = intent.getStringArrayListExtra("LIST_URL");
//        int positionClick = intent.getIntExtra("POSITION", 0);
//        if (list != null) {
//
//            String url = list.get(positionClick);
//            Glide.with(this)
//                    .load(url)
//                    .dontAnimate()
//                    .listener(new RequestListener<String, GlideDrawable>() {
//
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            supportStartPostponedEnterTransition();
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            supportStartPostponedEnterTransition();
//                            return false;
//                        }
//                    })
//                    .into(imageView);
//        }
    }

    public void initView() {
        mViewPagePhoto = (CustomViewPager) findViewById(R.id.vpPhotoTimeline);
        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mListViewComment = (ListView) findViewById(R.id.lvComment);
        mButtonSendComment = (ImageView) findViewById(R.id.tvSendComment);
        mProgressSend = (ProgressBar) findViewById(R.id.progressSend);
        mInputComment = (EmojiconEditText) findViewById(R.id.etComment);
        mLayoutViewPager = (RelativeLayout) findViewById(R.id.layoutViewPager);

        mIndicator.setFillColor(ContextCompat.getColor(this, R.color.colorMain));
        mIndicator.setPageColor(ContextCompat.getColor(this, R.color.white));
        mIndicator.setRadius(Utils.convertDpToPixel(this, 4));
        mIndicator.setStrokeWidth(0);
    }

    private void initData(ArrayList<String> listUrlImage, int currentItem) {
        if (listUrlImage != null && listUrlImage.size() > 0) {
            mLayoutViewPager.setVisibility(View.VISIBLE);
            TransitionAdapter animalPagerAdapter = new TransitionAdapter(getSupportFragmentManager(), listUrlImage);
            mViewPagePhoto.setAdapter(animalPagerAdapter);
            mViewPagePhoto.setCurrentItem(currentItem);
            mIndicator.setViewPager(mViewPagePhoto);
        } else {
            mLayoutViewPager.setVisibility(View.GONE);
        }
    }

    public void back(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
    }
}
