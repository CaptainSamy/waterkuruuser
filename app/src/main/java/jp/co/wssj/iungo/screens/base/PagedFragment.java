package jp.co.wssj.iungo.screens.base;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;
import jp.co.wssj.iungo.utils.Utils;

/**
 * Created by HieuPT on 6/5/2017.
 */

public abstract class PagedFragment<V extends IFragmentView, P extends FragmentPresenter<V>> extends BaseFragment<V, P> {

    public interface IOnPageSelectChangeListener {

        void onPageSelected();

        void onPageUnselected();
    }

    private IPagerFragmentCallback mPagerFragmentCallback;

    private List<IOnPageSelectChangeListener> mOnPageSelectedListeners;

    public String getPageTitle(Context context) {
        return Constants.EMPTY_STRING;
    }

    public void refresh() {
    }

    @Override
    public boolean isGlobal() {
        return false;
    }

    @Override
    public int getFragmentId() {
        return 0;
    }

    public void addOnPageSelectChangeListener(IOnPageSelectChangeListener listener) {
        if (mOnPageSelectedListeners == null) {
            mOnPageSelectedListeners = new ArrayList<>();
        }
        if (listener != null) {
            for (IOnPageSelectChangeListener onPageSelectChangeListener : mOnPageSelectedListeners) {
                if (onPageSelectChangeListener == listener) {
                    return;
                }
            }
            mOnPageSelectedListeners.add(listener);
        }
    }

    public void removeOnPageSelectChangeListener(IOnPageSelectChangeListener listener) {
        if (mOnPageSelectedListeners != null && listener != null) {
            mOnPageSelectedListeners.remove(listener);
        }
    }

    public void clearOnPageSelectChangeListener() {
        if (mOnPageSelectedListeners != null) {
            mOnPageSelectedListeners.clear();
        }
    }

    public void notifyPageSelected() {
        Logger.d(getLogTag(), "#notifyPageSelected");
        if (mOnPageSelectedListeners != null) {
            for (IOnPageSelectChangeListener listener : mOnPageSelectedListeners) {
                listener.onPageSelected();
            }
        }
    }

    public void notifyPageUnselected() {
        Logger.d(getLogTag(), "#notifyPageUnselected");
        if (mOnPageSelectedListeners != null) {
            for (IOnPageSelectChangeListener listener : mOnPageSelectedListeners) {
                listener.onPageUnselected();
            }
        }
    }

    protected IPagerFragmentCallback getPagerFragmentCallback() {
        return mPagerFragmentCallback;
    }

    public void setPagerFragmentCallback(@NonNull IPagerFragmentCallback callback) {
        Utils.requireNonNull(callback, "PagerFragmentCallback must not be null");
        mPagerFragmentCallback = callback;
    }
}
