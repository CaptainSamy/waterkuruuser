package jp.co.wssj.iungo.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import jp.co.wssj.iungo.utils.Logger;

/**
 * Created by HieuPT on 7/26/2017.
 */

public class LoadMoreRecyclerView extends RecyclerView {

    private static final String TAG = "LoadMoreRecyclerView";

    private boolean mIsLoading;

    private ILoadMoreListener mListener;

    public LoadMoreRecyclerView(Context context) {
        this(context, null);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
                if (layoutManager != null) {
                    int totalItemCount = layoutManager.getItemCount();
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                    if (totalItemCount != 0 && lastVisibleItemPosition == totalItemCount - 1) {
                        Logger.d(TAG, "#end of recycle view");
                        if (!mIsLoading) {
                            mIsLoading = true;
                            if (mListener != null) {
                                Logger.d(TAG, "#onLoadMore");
                                mListener.onLoadMore();
                            }
                        }
                    }
                }
            }
        });
    }

    public void setOnLoadMoreListener(ILoadMoreListener listener) {
        mListener = listener;
    }

    public void notifyLoadComplete() {
        mIsLoading = false;
    }
}
