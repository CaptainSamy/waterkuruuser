package wssj.co.jp.olioa.widget;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

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
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) this.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }
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
