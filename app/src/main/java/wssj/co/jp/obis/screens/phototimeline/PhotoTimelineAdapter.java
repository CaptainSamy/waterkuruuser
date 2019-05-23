package wssj.co.jp.obis.screens.phototimeline;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;

import wssj.co.jp.obis.R;
import wssj.co.jp.obis.widget.ImageRoundCorners;

/**
 * Created by Nguyen Huu Ta on 19/5/2017.
 */

public class PhotoTimelineAdapter extends PagerAdapter {

    private final LayoutInflater mLayoutInflater;

    private List<String> mListRestImages;

    private Context mContext;

    public PhotoTimelineAdapter(Context context, List<String> restImages) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mListRestImages = new ArrayList<>();
        mListRestImages.addAll(restImages);
    }

    public void refreshPhoto(List<String> restImages) {
        if (mListRestImages != null) {
            mListRestImages.clear();
            mListRestImages.addAll(restImages);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mListRestImages == null ? 0 : mListRestImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View rootView = mLayoutInflater.inflate(R.layout.item_photo, container, false);
        ImageRoundCorners imageView = (ImageRoundCorners) rootView.findViewById(R.id.imageView);
        if (mListRestImages != null) {
            fillImage(mListRestImages.get(position), imageView);
        }
        container.addView(rootView);
        return rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private void fillImage(String urlImage, final ImageView imageView) {
        if (!TextUtils.isEmpty(urlImage)) {
            Glide.with(mContext)
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
                        }
                    });
        }
    }
}
