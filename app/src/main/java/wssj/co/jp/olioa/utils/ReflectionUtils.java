package wssj.co.jp.olioa.utils;

import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;

import java.lang.reflect.Field;

/**
 * Created by HieuPT on 10/10/2017.
 */

public class ReflectionUtils {

    public static final String TAG = "ReflectionUtils";

    public static void setBottomNavigationViewShiftingMode(BottomNavigationView view, boolean shifting) {
        if (view != null) {
            try {
                Field menuViewField = BottomNavigationView.class.getDeclaredField("mMenuView");
                if (menuViewField != null) {
                    menuViewField.setAccessible(true);
                    BottomNavigationMenuView menuView = (BottomNavigationMenuView) menuViewField.get(view);
                    if (menuView != null) {
                        Field shiftingModeField = BottomNavigationMenuView.class.getDeclaredField("mShiftingMode");
                        if (shiftingModeField != null) {
                            shiftingModeField.setAccessible(true);
                            shiftingModeField.setBoolean(menuView, shifting);
                            for (int i = 0; i < menuView.getChildCount(); i++) {
                                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                                item.setShiftingMode(shifting);
                                item.setChecked(item.getItemData().isChecked());
                            }
                        }
                    }
                }
            } catch (NoSuchFieldException e) {
                Logger.e(TAG, "NoSuchFieldException: " + e.getMessage());
            } catch (IllegalAccessException e) {
                Logger.e(TAG, "IllegalAccessException: " + e.getMessage());
            }
        }
    }

    private ReflectionUtils() {
        //no instance
    }
}
