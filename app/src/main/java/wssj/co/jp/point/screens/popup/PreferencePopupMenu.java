package wssj.co.jp.point.screens.popup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import wssj.co.jp.point.R;

/**
 * Created by HieuPT on 5/24/2017.
 */

public class PreferencePopupMenu extends PopupMenu implements IPopupMenuView {

    private final Context mContext;

    private OnMenuItemClickListener mOnMenuItemClickListener;

    public PreferencePopupMenu(@NonNull Context context, @NonNull View anchor) {
        super(context, anchor);
        mContext = context;
        PopupMenuPresenter presenter = new PopupMenuPresenter(this);
        presenter.inflateMenu();
        super.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return mOnMenuItemClickListener != null && mOnMenuItemClickListener.onMenuItemClick(item);
            }
        });
    }

    @Override
    public Context getViewContext() {
        return mContext;
    }

    public void setMenuItemClickListener(OnMenuItemClickListener listener) {
        mOnMenuItemClickListener = listener;
    }

    @Override
    public void inflateMenu(int idView) {
        inflate(R.menu.drawable_menu);
        switch (idView) {
            case R.id.menu_push_notification:
                getMenu().setGroupVisible(R.id.menu_push_notification, true);
                break;
            case R.id.menu_logout:
                getMenu().setGroupVisible(R.id.navigation_menu, true);
                break;

            default:

                break;
        }
    }
}
