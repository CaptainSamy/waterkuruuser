package wssj.co.jp.obis.screens.memomanager.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

import wssj.co.jp.obis.R;
import wssj.co.jp.obis.model.memo.MemoDynamicResponse;

/**
 * Created by Nguyen Huu Ta on 10/7/2017.
 */

public class ImageAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;

    private final List<MemoDynamicResponse.UserMemoData.UserMemoValue.Value.Image> mObjects;

    int mNumberImage;

    public ImageAdapter(@NonNull Context context, @NonNull List<MemoDynamicResponse.UserMemoData.UserMemoValue.Value.Image> objects, int numberImages) {
        super(context, 0);
        mInflater = LayoutInflater.from(context);
        mObjects = objects;
        mNumberImage = numberImages;
    }

    @Override
    public int getCount() {
        return mNumberImage;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        if (position < mObjects.size()) {
            return mObjects.get(position).getUrlImage();
        } else {
            return null;
        }
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_image_memo_config, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.bindData(getContext(), getItem(position));
        return convertView;
    }

    private static class ViewHolder {

        private ImageView mImage;

        ViewHolder(View view) {
            mImage = (ImageView) view.findViewById(R.id.imageConfig);
        }

        private void bindData(Context context, String patch) {
            if (!TextUtils.isEmpty(patch)) {
                fillImage(patch, mImage, false, context);
            }
        }

        private void fillImage(String imgPath, final ImageView imageView, boolean fromLocal, Context context) {
            Glide.with(context)
                    .load(imgPath)
                    .placeholder(R.drawable.ic_add_image)
                    .skipMemoryCache(fromLocal)
                    .diskCacheStrategy(fromLocal ? DiskCacheStrategy.NONE : DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_add_image)
                    .into(new SimpleTarget<GlideDrawable>() {

                        @Override
                        public void onLoadStarted(Drawable placeholder) {
                            imageView.setEnabled(false);
                            imageView.setImageDrawable(placeholder);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            imageView.setEnabled(true);
                            imageView.setImageDrawable(errorDrawable);
                            imageView.setTag(R.id.shared_drawable, null);
                        }

                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            imageView.setEnabled(true);
                            imageView.setImageDrawable(resource);
                            imageView.setTag(R.id.shared_drawable, resource);
                        }
                    });
        }


    }
}