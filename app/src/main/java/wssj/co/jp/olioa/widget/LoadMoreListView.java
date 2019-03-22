package wssj.co.jp.olioa.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by HieuPT on 7/3/2017.
 */

public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {

    private boolean mIsLoading;

    private int mCurrentPage, mTotalPage;

    private ILoadMoreListView mListener;

    public LoadMoreListView(Context context) {
        this(context, null);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.listViewStyle);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnScrollListener(this);
    }

    public void setOnLoadMoreListener(ILoadMoreListView listener) {
        mListener = listener;
    }

    public void notifyLoadComplete() {
        mIsLoading = false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //interface method
    }

    public void setCurrentPage(int currentPage) {
        mCurrentPage = currentPage;
    }

    public void setTotalPage(int totalPage) {
        mTotalPage = totalPage;
    }

    public void resetState() {
        mCurrentPage = 0;
        mTotalPage = 0;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount != 0 && firstVisibleItem + visibleItemCount == totalItemCount) {
            if (mCurrentPage < (mTotalPage - 1) && !mIsLoading) {
                if (mListener != null) {
                    mIsLoading = true;
                    mCurrentPage++;
                    mListener.onLoadMore(mCurrentPage);
                }
            }
        }
    }
}
