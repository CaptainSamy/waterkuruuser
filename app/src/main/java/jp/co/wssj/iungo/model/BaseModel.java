package jp.co.wssj.iungo.model;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;

/**
 * Created by HieuPT on 3/22/2017.
 */

public abstract class BaseModel {

    private interface IWorkerTaskFinishListener {

        void onFinished(WorkerTask<?> workerTask);
    }

    private static final String TAG = "BaseModel";

    private final Context mContext;

    private final List<WorkerTask<?>> mWorkerTaskList;

    public BaseModel(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }
        mContext = context;
        mWorkerTaskList = new ArrayList<>();
    }

    protected <T> void runOnWorkerThread(IWorkerTask<T> action) {
        WorkerTask<T> workerTask = new WorkerTask<>(action, new IWorkerTaskFinishListener() {

            @Override
            public void onFinished(WorkerTask<?> workerTask) {
                mWorkerTaskList.remove(workerTask);
            }
        });
        mWorkerTaskList.add(workerTask);
        workerTask.execute();
    }

    public void resumeWorkerTask() {
        if (!mWorkerTaskList.isEmpty()) {
            Logger.d(TAG, "#resumeWorkerTask");
            WorkerTask<?> tempTask;
            for (int i = 0; i < mWorkerTaskList.size(); i++) {
                tempTask = mWorkerTaskList.get(i);
                if (tempTask != null) {
                    tempTask.cancel(true);
                    mWorkerTaskList.set(i, new WorkerTask<>(tempTask));
                }
            }
            for (WorkerTask<?> workerTask : mWorkerTaskList) {
                if (workerTask != null) {
                    workerTask.execute();
                }
            }
        }
    }

    public void stopWorkerTask() {
        if (!mWorkerTaskList.isEmpty()) {
            Logger.d(TAG, "#stopWorkerTask");
            for (WorkerTask<?> workerTask : mWorkerTaskList) {
                if (workerTask != null) {
                    workerTask.cancel(true);
                }
            }
        }
    }

    public void release() {
        stopWorkerTask();
        mWorkerTaskList.clear();
    }

    public Context getContext() {
        return mContext;
    }

    public String getStringResource(int id) {
        try {
            Context context = getContext();
            if (context != null) {
                return context.getString(id);
            }
        } catch (Resources.NotFoundException e) {
            return Constants.EMPTY_STRING;
        }
        return Constants.EMPTY_STRING;
    }

    protected interface IWorkerTask<T> {

        T doWork();

        void onFinish(T result);
    }

    private static final class WorkerTask<Result> extends AsyncTask<Void, Void, Result> {

        private final IWorkerTask<Result> mAction;

        private final IWorkerTaskFinishListener mFinishListener;

        private WorkerTask(IWorkerTask<Result> action, IWorkerTaskFinishListener listener) {
            mAction = action;
            mFinishListener = listener;
        }

        private WorkerTask(WorkerTask<Result> workerTask) {
            mAction = workerTask.mAction;
            mFinishListener = workerTask.mFinishListener;
        }

        @Override
        protected Result doInBackground(Void... params) {
            if (mAction != null) {
                return mAction.doWork();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Result result) {
            if (mAction != null) {
                mAction.onFinish(result);
            }
            if (mFinishListener != null) {
                mFinishListener.onFinished(this);
            }
        }
    }

}