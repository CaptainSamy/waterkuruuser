package wssj.co.jp.olioa.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by Nguyen Huu Ta on 26/5/2017.
 */

public class ImageRoundCorners extends android.support.v7.widget.AppCompatImageView {

    private float radius = 18.0f;

    private Path path;

    private RectF rect;

    public ImageRoundCorners(Context context) {
        super(context);
        init();
    }

    public ImageRoundCorners(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageRoundCorners(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        path = new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        path.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }

}
