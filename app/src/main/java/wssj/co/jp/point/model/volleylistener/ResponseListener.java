package wssj.co.jp.point.model.volleylistener;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import wssj.co.jp.point.utils.Logger;

/**
 * Created by HieuPT on 5/18/2017.
 */

public class ResponseListener<T> implements Response.Listener<T>, Response.ErrorListener {

    private final Response.Listener<T> mListener;

    private final Response.ErrorListener mErrorListener;

    private final String mTag;

    private final String mCaller;

    public ResponseListener(String tag, String caller, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        mTag = tag;
        mCaller = caller;
        mListener = listener;
        mErrorListener = errorListener;
    }

    @Override
    public void onResponse(T response) {
        Logger.d(mTag, "#" + mCaller + " -> onResponse");
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Logger.d(mTag, mCaller + " -> onErrorResponse");
        if (mErrorListener != null) {
            mErrorListener.onErrorResponse(error);
        }
    }
}
