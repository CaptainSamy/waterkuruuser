package wssj.co.jp.olioa.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by HieuPT on 6/1/2017.
 */

public class MarginItemDecoration extends RecyclerView.ItemDecoration {

    private final int mSpace;

    public MarginItemDecoration(int space) {
        mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = mSpace;
    }
}
