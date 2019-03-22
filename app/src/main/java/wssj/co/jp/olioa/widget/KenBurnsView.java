package wssj.co.jp.olioa.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.utils.Utils;

/**
 * Created by f.laurent on 21/11/13.
 */
public class KenBurnsView extends FrameLayout {

    private static final String TAG = "KenBurnsView";

    private ImageView mImageBackground;

    public KenBurnsView(Context context) {
        this(context, null);
    }

    public KenBurnsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KenBurnsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setResourceIds(int resourceIds) {
        mImageBackground.setImageResource(resourceIds);
    }

    public void fillImage(String url) {
        Utils.fillImageQuantityOrigin(getContext(), url, mImageBackground, R.drawable.no_image_background);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = inflate(getContext(), R.layout.view_kenburns, this);
        mImageBackground = (ImageView) view.findViewById(R.id.image0);
    }
}
