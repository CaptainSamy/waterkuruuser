package wssj.co.jp.point.screens.memomanager.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import wssj.co.jp.point.R;

/**
 * Created by Nguyen Huu Ta on 10/7/2017.
 */

public class ImageAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;

    private final List<String> mObjects;

    public ImageAdapter(@NonNull Context context, @NonNull List<String> objects) {
        super(context, 0);
        mInflater = LayoutInflater.from(context);
        mObjects = objects;
    }

    @Override
    public int getCount() {
        if (mObjects != null)
            return mObjects.size();
        return 0;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        if (position < mObjects.size()) {
            return mObjects.get(position);
        } else {
            return null;
        }
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder vh;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_image_memo_config, parent, false);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
//        vh.bindData(getContext(), getItem(position));
        return view;
    }

    private static class ViewHolder {

        private ImageView mImage;

        ViewHolder(View view) {
            mImage = (ImageView) view.findViewById(R.id.imageConfig);
        }

        private void bindData(Context context, String string) {

        }

    }
}