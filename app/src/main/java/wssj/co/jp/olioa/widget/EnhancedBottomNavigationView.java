package wssj.co.jp.olioa.widget;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.util.AttributeSet;

/**
 * Created by Nguyen Huu Ta on 18/10/2017.
 */

public class EnhancedBottomNavigationView extends BottomNavigationView {

    private OnNavigationItemSelectedListener mSelectedListener;

    public EnhancedBottomNavigationView(Context context) {
        this(context, null);
    }

    public EnhancedBottomNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EnhancedBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSelectedItemIdWithoutNotify(@IdRes int itemId) {
        final OnNavigationItemSelectedListener listener = mSelectedListener;
        setOnNavigationItemSelectedListener(null);
        setSelectedItemId(itemId);
        setOnNavigationItemSelectedListener(listener);
    }

    @Override
    public void setOnNavigationItemSelectedListener(@Nullable OnNavigationItemSelectedListener listener) {
        super.setOnNavigationItemSelectedListener(listener);
        mSelectedListener = listener;
    }
}
