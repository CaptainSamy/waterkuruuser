package wssj.co.jp.olioa.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by HieuPT on 7/3/2017.
 */

public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {

    private boolean mIsLoading;

    private int mCurrentPage, mTotalPage;

    private ILoadMoreListener mListener;

    private ILoadMoreTopListener mListenerTop;

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

    public void setOnLoadMoreListener(ILoadMoreListener listener) {
        mListener = listener;
    }

    public void setOnLoadListenerTop(ILoadMoreTopListener mListenerTop) {
        this.mListenerTop = mListenerTop;
    }

    public void notifyLoadComplete() {
        mIsLoading = false;
    }

    public void stopLoadMore() {
        mIsLoading = true;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //interface method
    }

    public void setCurrentPage(int currentPage) {
        mCurrentPage = currentPage;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void reload() {
        mCurrentPage = 0;
        mIsLoading = false;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mListenerTop != null) {
            if (totalItemCount > 0) {
                if (firstVisibleItem == 0) {
                    View v = this.getChildAt(0);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset == 0) {
                        if (!mIsLoading && mListenerTop != null) {
                            mIsLoading = true;
                            mListenerTop.onLoadMoreTop(mCurrentPage);
                        }
                        return;
                    }
                }
            }
        }
        if (mListener != null) {
            if (totalItemCount != 0 && firstVisibleItem + visibleItemCount == totalItemCount) {
                if (!mIsLoading) {
                    mIsLoading = true;
                    mCurrentPage++;
                    mListener.onLoadMore(mCurrentPage);
                }
            }
        }
    }

}
