package wssj.co.jp.olioa.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by HieuPT on 6/5/2017.
 */

public class NonSwipeableViewPager extends ViewPager {

    private boolean mIsSwipeable = true;

    public NonSwipeableViewPager(Context context) {
        super(context);
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSwipeable(boolean isSwipeable) {
        mIsSwipeable = isSwipeable;
    }

    public boolean isSwipeable() {
        return mIsSwipeable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mIsSwipeable && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mIsSwipeable && super.onTouchEvent(event);
    }
}
