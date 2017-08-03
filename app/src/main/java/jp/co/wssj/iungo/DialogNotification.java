package jp.co.wssj.iungo;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.screens.pushnotification.PushNotificationAdapter;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Utils;
import jp.co.wssj.iungo.widget.EasyDialog;

/**
 * Created by Nguyen Huu Ta on 1/8/2017.
 */

public class DialogNotification {

    private TextView mTextNoItem;

    private ListView mListViewNotification;

    private EasyDialog mEasyDialog;

    private PushNotificationAdapter mPushNotificationAdapter;

    private Activity mActivity;

    private List<NotificationMessage> mListNotification;

    private IOnItemClick mCallback;

    public DialogNotification(Activity activity, List<NotificationMessage> listNotification, ImageView viewAttached) {
        mActivity = activity;
        mListNotification = listNotification;
        initDialog(viewAttached);
    }

    public void initDialog(ImageView viewAttached) {
        mEasyDialog = new EasyDialog(mActivity)
                .setGravity(EasyDialog.GRAVITY_BOTTOM)
                .setBackgroundColor(mActivity.getResources().getColor(android.R.color.white))
                .setLocationByAttachedView(viewAttached)
                .setAnimationTranslationShow(EasyDialog.DIRECTION_X, Constants.DURATION_DIALOG_NOTIFICATION, mActivity.getResources().getDisplayMetrics().widthPixels, 0)
                .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, Constants.DURATION_DIALOG_NOTIFICATION, 0, -mActivity.getResources().getDisplayMetrics().widthPixels)
                .setTouchOutsideDismiss(true)
                .setMatchParent(false)
                .setMargin(Utils.convertDpToPixel(mActivity, Constants.MARGIN_LEFT), 0, Utils.convertDpToPixel(mActivity, Constants.MARGIN_RIGHT), Utils.convertDpToPixel(mActivity, Constants.MARGIN_BOTTOM))
                .setOutsideColor(mActivity.getResources().getColor(R.color.outside_color_trans));

    }

    public void addListNotification(final int page, final int totalPage, List<NotificationMessage> list) {
        mListViewNotification = mEasyDialog.getListView();
        mTextNoItem = mEasyDialog.getTextNoItem();
        if (mPushNotificationAdapter == null) {
            mListNotification = new ArrayList<>();
            mPushNotificationAdapter = new PushNotificationAdapter(mActivity, R.layout.item_push_notification, mListNotification);
            mListViewNotification.setAdapter(mPushNotificationAdapter);
        }
        if (page == 1) {
            mListNotification.clear();
            mListNotification.addAll(list);
        } else {
            mListNotification.addAll(list);
        }
        mPushNotificationAdapter.notifyDataSetChanged();
        checkListEmpty();
        mPushNotificationAdapter.setListenerEndOfListView(new PushNotificationAdapter.IEndOfListView() {

            @Override
            public void onEndOfListView() {
                if (page < totalPage && mCallback != null) {
                    mCallback.endOfListView(page + 1, Constants.LIMIT);
                }
            }
        });

        mListViewNotification.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mEasyDialog.dismiss();
                NotificationMessage message = (NotificationMessage) parent.getAdapter().getItem(position);
                if (mCallback != null) {
                    checkListEmpty();
                    mCallback.onItemClick(message);
                    mListNotification.remove(position);
                    mPushNotificationAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public Dialog getDialog() {
        if (mEasyDialog != null) {
            return mEasyDialog.getDialog();
        }
        return null;
    }

    public boolean getDialogShowing() {
        return getDialog() == null ? false : getDialog().isShowing();
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

    public void addNotification(NotificationMessage notificationMessage) {
        if (mListNotification != null) {
            mListNotification.add(0, notificationMessage);
            mPushNotificationAdapter.notifyDataSetChanged();
        }
    }

    public void show() {
        mEasyDialog.show();
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

    public interface IOnItemClick {

        void onItemClick(NotificationMessage message);

        void endOfListView(int page, int limit);
    }
}
