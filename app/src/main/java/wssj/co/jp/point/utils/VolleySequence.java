package wssj.co.jp.point.utils;

import android.os.Handler;
import android.os.Looper;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import wssj.co.jp.point.App;

public final class VolleySequence {

    private static final String TAG = "VolleySequence";

    private static VolleySequence sInstance;

    public synchronized static VolleySequence getInstance() {
        if (sInstance == null) {
            sInstance = new VolleySequence();
        }
        return sInstance;
    }

    private boolean mIsRunning;

    private final RequestQueue mVolleyRequestQueue;

    private final LinkedList<DelayedRequest> mDelayedQueue;

    private final Map<Request<?>, DelayedRequest> mRequestMap;

    private final Handler mHandler;

    private VolleySequence() {
        mHandler = new Handler(Looper.getMainLooper());
        mRequestMap = new HashMap<>();
        mDelayedQueue = new LinkedList<>();
        mVolleyRequestQueue = Volley.newRequestQueue(App.getInstance());
        mVolleyRequestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                removeRequestOnFinish(request);
                if (!mDelayedQueue.isEmpty()) {
                    final DelayedRequest nextRequest = mDelayedQueue.peek();
                    mHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            mVolleyRequestQueue.add(nextRequest.mRequest);
                        }
                    }, nextRequest.mTimeDelay);
                } else {
                    mIsRunning = false;
                }
            }
        });
    }

    private void removeRequestOnFinish(Request<?> request) {
        DelayedRequest currentDelayedRequest = mRequestMap.remove(request);
        if (currentDelayedRequest != null) {
            mDelayedQueue.remove(currentDelayedRequest);
        }
    }

    public void addRequestToFrontQueue(Request<?> request) {
        addRequestToFrontQueue(request, 0);
    }

    public void addRequestToFrontQueue(Request<?> request, int delayMillis) {
        synchronized (this) {
            Logger.i(TAG, "#addRequest: " + request);
            if (request != null) {
                request.setRetryPolicy(new DefaultRetryPolicy(request.getTimeoutMs(), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                DelayedRequest delayedRequest = new DelayedRequest(request, delayMillis);
                mDelayedQueue.addFirst(delayedRequest);
                mRequestMap.put(request, delayedRequest);
                if (!mIsRunning) {
                    start();
                }
            }
        }
    }

    public void addRequest(Request<?> request) {
        request.setRetryPolicy(new DefaultRetryPolicy(5000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        addRequest(request, 0);
    }

    /**
     * Add new request to queue.
     *
     * @param request     Request object
     * @param delayMillis delay time before execute this request in millisecond. Will be ignored if there is no request on queue
     */
    public void addRequest(Request<?> request, int delayMillis) {
        synchronized (this) {
            Logger.i(TAG, "#addRequest: " + request);
            if (request != null) {
                request.setRetryPolicy(new DefaultRetryPolicy(request.getTimeoutMs(), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                DelayedRequest delayedRequest = new DelayedRequest(request, delayMillis);
                mDelayedQueue.add(delayedRequest);
                mRequestMap.put(request, delayedRequest);
                if (!mIsRunning) {
                    start();
                }
            }
        }
    }

    public void removeRequest(Request<?> request) {
        synchronized (this) {
            Logger.i(TAG, "#removeRequest: " + request);
            if (request != null) {
                DelayedRequest delayedRequest = mRequestMap.remove(request);
                if (delayedRequest != null) {
                    mDelayedQueue.remove(delayedRequest);
                }
            }
        }
    }

    public void start() {
        synchronized (this) {
            if (!mIsRunning) {
                DelayedRequest peekRequest = mDelayedQueue.peek();
                if (peekRequest != null) {
                    mIsRunning = true;
                    mVolleyRequestQueue.add(peekRequest.mRequest);
                }
            }
        }
    }

    public void stop() {
        synchronized (this) {
            if (mIsRunning) {
                if (mVolleyRequestQueue != null) {
                    mVolleyRequestQueue.stop();
                }
                mHandler.removeCallbacksAndMessages(null);
                mIsRunning = false;
            }
        }
    }

    public void release() {
        synchronized (this) {
            Logger.i(TAG, "#release");
            stop();
            mDelayedQueue.clear();
        }
    }

    public int getRemainRequestCount() {
        return mDelayedQueue.size();
    }

    private class DelayedRequest {

        private Request<?> mRequest;

        private int mTimeDelay;

        private DelayedRequest(Request<?> request, int timeDelay) {
            mRequest = request;
            mTimeDelay = timeDelay;
        }
    }

}