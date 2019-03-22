package wssj.co.jp.olioa;

import android.app.Activity;
import android.app.Dialog;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wssj.co.jp.olioa.model.entities.PushNotification;
import wssj.co.jp.olioa.model.firebase.NotificationMessage;
import wssj.co.jp.olioa.screens.pushnotification.adapter.PushNotificationAdapter;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.utils.Utils;
import wssj.co.jp.olioa.widget.EasyDialog;
import wssj.co.jp.olioa.widget.ILoadMoreListView;
import wssj.co.jp.olioa.widget.LoadMoreListView;

/**
 * Created by Nguyen Huu Ta on 1/8/2017.
 */

public class DialogNotification implements SwipeRefreshLayout.OnRefreshListener {

    private TextView mTextNoItem;

    private SwipeRefreshLayout mRefreshLayout;

    private LoadMoreListView mListViewNotification;

    private EasyDialog mEasyDialog;

    private PushNotificationAdapter mPushNotificationAdapter;

    private Activity mActivity;

    private List<PushNotification> mListNotification;

    private IOnItemClick mCallback;

    private long mLastClickTime = 0;

    public DialogNotification(Activity activity, ImageView viewAttached, ILoadMoreListView callback) {
        mActivity = activity;
        initDialog(viewAttached, callback);
    }

    private void initDialog(ImageView viewAttached, ILoadMoreListView callback) {
        mEasyDialog = new EasyDialog(mActivity, callback)
                .setGravity(EasyDialog.GRAVITY_BOTTOM)
                .setBackgroundColor(mActivity.getResources().getColor(android.R.color.white))
                .setLocationByAttachedView(viewAttached)
                .setAnimationTranslationShow(EasyDialog.DIRECTION_X, Constants.DURATION_DIALOG_NOTIFICATION, mActivity.getResources().getDisplayMetrics().widthPixels, 0)
                //.setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, Constants.DURATION_DIALOG_NOTIFICATION, 0, -mActivity.getResources().getDisplayMetrics().widthPixels)
                .setTouchOutsideDismiss(true)
                .setMatchParent(false)
                .setMargin(Utils.convertDpToPixel(mActivity, Constants.MARGIN_LEFT), 0, Utils.convertDpToPixel(mActivity, Constants.MARGIN_RIGHT), Utils.convertDpToPixel(mActivity, Constants.MARGIN_BOTTOM))
                .setOutsideColor(mActivity.getResources().getColor(R.color.outside_color_trans));

        mListViewNotification = mEasyDialog.getListView();
        mRefreshLayout = mEasyDialog.getRefreshLayout();
        mTextNoItem = mEasyDialog.getTextNoItem();

    }

    public void addListNotification(List<PushNotification> list, int... totalPage) {
        int page = 0;
        if (totalPage.length > 0) {
            page = totalPage[0];
            mListViewNotification.setTotalPage(page);
            mListViewNotification.setCurrentPage(0);
        }
        if (mPushNotificationAdapter == null) {
            mListNotification = new ArrayList<>();
            mPushNotificationAdapter = new PushNotificationAdapter(mActivity, mListNotification);
            mListViewNotification.setAdapter(mPushNotificationAdapter);
        }
        if (page == 0) {
            mListNotification.clear();
        }
        mListNotification.addAll(list);
        mPushNotificationAdapter.notifyDataSetChanged();
        checkListEmpty();
        mListViewNotification.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                mEasyDialog.dismiss();
//                NotificationMessage message = (NotificationMessage) parent.getAdapter().getItem(position);
//                if (mCallback != null) {
//                    checkListEmpty();
//                    mCallback.onItemClick(message);
//                }
            }
        });
    }

    public Dialog getDialog() {
        if (mEasyDialog != null) {
            return mEasyDialog.getDialog();
        }
        return null;
    }

    public void checkListEmpty() {
        if (mListNotification != null && mListNotification.size() > 0) {
            mListViewNotification.setVisibility(View.VISIBLE);
            mTextNoItem.setVisibility(View.GONE);
        } else {
            mListViewNotification.setVisibility(View.INVISIBLE);
            mTextNoItem.setVisibility(View.VISIBLE);
        }
    }

    public void show() {
        mEasyDialog.show();
        mPushNotificationAdapter.notifyDataSetChanged();
    }

    public SwipeRefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }

    public void dismiss() {
        mEasyDialog.dismiss();
    }

    public void clearData() {
        if (mListNotification != null) {
            mListNotification.clear();
        }
    }

    public void setmCallback(IOnItemClick mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public void onRefresh() {
        mCallback.onRefresh();
    }

    public interface IOnItemClick {

        void onItemClick(NotificationMessage message);

        void onRefresh();
    }
}
