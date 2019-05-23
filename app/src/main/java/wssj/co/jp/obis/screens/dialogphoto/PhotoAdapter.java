package wssj.co.jp.obis.screens.dialogphoto;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

import wssj.co.jp.obis.R;
import wssj.co.jp.obis.widget.ImageRoundCorners;

/**
 * Created by Nguyen Huu Ta on 19/5/2017.
 */

public class PhotoAdapter extends PagerAdapter {

    Context mContext;

    LayoutInflater mLayoutInflater;

    List<Integer> listPhoto;

    public PhotoAdapter(Context context, List<Integer> drawables) {
        mContext = context;
        listPhoto = drawables;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listPhoto.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_photo, container, false);

        ImageRoundCorners imageView = (ImageRoundCorners) itemView.findViewById(R.id.imageView);
        imageView.setImageResource(listPhoto.get(position));
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
