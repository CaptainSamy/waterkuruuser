package jp.co.wssj.iungo.screens.pushnotification.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.model.pushnotification.ContentPushResponse;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.dialograting.DialogRating;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Utils;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationDetailFragment extends BaseFragment<IPushNotificationDetailView, PushNotificationDetailPresenter> implements IPushNotificationDetailView {

    public static final String TAG = "PushNotificationDetailServiceCompanyFragment";

    public static final String NOTIFICATION_ARG = "notification";

    public static final String NOTIFICATION_SHOW_RATING = "show_rating";

    public static final String FLAG_FROM_ACTIVITY = "from_activity";

    private TextView mTitle, mTime;

    private WebView mBody;

    private TextView mButtonRating;

    private NotificationMessage mNotificationMessage;

    public static PushNotificationDetailFragment newInstance(Bundle bundle) {
        PushNotificationDetailFragment fragment = new PushNotificationDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getLogTag() {
        return "PushNotificationDetailServiceCompanyFragment";
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_PUSH_NOTIFICATION_DETAIL;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_notification_detail;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_push_detail);
    }

    @Override
    public boolean isDisplayIconNotification() {
        return false;
    }

    @Override
    protected PushNotificationDetailPresenter onCreatePresenter(IPushNotificationDetailView view) {
        return new PushNotificationDetailPresenter(view);
    }

    @Override
    protected IPushNotificationDetailView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        mTitle = (TextView) rootView.findViewById(R.id.title_notification);
        mBody = (WebView) rootView.findViewById(R.id.body_notification);
        mTime = (TextView) rootView.findViewById(R.id.time_notification);
        mButtonRating = (TextView) rootView.findViewById(R.id.buttonRating);
    }

    @Override
    protected void initAction() {
        super.initAction();
        mButtonRating.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showRating(true);
            }
        });
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        boolean isRequesApi = false;
        if (bundle != null) {
            mNotificationMessage = (NotificationMessage) bundle.getSerializable(NOTIFICATION_ARG);

            boolean isFromActivity = bundle.getBoolean(FLAG_FROM_ACTIVITY, false);
            boolean isShowRating = bundle.getInt(NOTIFICATION_SHOW_RATING) == 1 ? true : false;
            if (mNotificationMessage != null) {
                if (mNotificationMessage.getStatusRead() != Constants.STATUS_READ) {
                    List<Long> list = new ArrayList<>();
                    list.add(mNotificationMessage.getPushId());
                    getPresenter().setListPushUnRead(list, Constants.STATUS_READ);
                }
                mTitle.setText(mNotificationMessage.getTitle().trim());
                mBody.getSettings().setJavaScriptEnabled(true);
                mBody.getSettings().setBuiltInZoomControls(true);
                mBody.getSettings().setDisplayZoomControls(false);
                SpannableString spannableString = null;
                if (!TextUtils.isEmpty(mNotificationMessage.getAction())) {
                    switch (mNotificationMessage.getAction()) {
                        case Constants.PushNotification.TYPE_NOTIFICATION:
                            mButtonRating.setVisibility(View.GONE);
                            break;
                        case Constants.PushNotification.TYPE_REMIND:
                            mButtonRating.setVisibility(View.GONE);
                            isRequesApi = true;
                            spannableString = new SpannableString(mNotificationMessage.getMessage());
                            break;
                        case Constants.PushNotification.TYPE_REQUEST_REVIEW:
                            mButtonRating.setVisibility(View.VISIBLE);
                            isRequesApi = true;
                            showRating(isShowRating);
                            spannableString = new SpannableString(mNotificationMessage.getMessage());
                            break;
                        default:
                            mButtonRating.setVisibility(View.GONE);
                            break;
                    }
                }
                if (isFromActivity && !isRequesApi) {
                    getPresenter().getContentPush(mNotificationMessage.getPushId());
                } else {
                    if (spannableString != null) {
                        mBody.loadDataWithBaseURL("", Html.toHtml(spannableString), "text/html", "UTF-8", "");
                    } else {
                        mBody.loadDataWithBaseURL("", mNotificationMessage.getMessage(), "text/html", "UTF-8", "");
                    }
                }
                String time = Utils.convertLongToTime(mNotificationMessage.getPushTime(), isFromActivity);
                mTime.setText(time);

            }
        }
    }

    public void showRating(boolean isShowRating) {
        if (isShowRating && mNotificationMessage != null) {
            DialogRating dialogRating = new DialogRating(getActivityContext(), new DialogRating.IOnClickListenerRating() {

                @Override
                public void onButtonRating(float rating, String note, int currentPosition) {
                    mButtonRating.setVisibility(View.GONE);
                    getPresenter().updateActionPush(mNotificationMessage.getPushId());
                }
            });
            dialogRating.show(0, mNotificationMessage.getStampId());
        }
    }

    @Override
    public void onGetContentPushSuccess(ContentPushResponse.ContentPushData contentPushResponse) {
        if (contentPushResponse != null) {
//            Utils.formatHtml(mBody, contentPushResponse.getContent());
            mBody.loadDataWithBaseURL("", contentPushResponse.getContent(), "text/html", "UTF-8", "");
        }
    }

    @Override
    public void onGetContentPushFailure(String message) {
        showToast(message);
    }

    @Override
    public void onUpdateStatusPushSuccess() {
        Intent sentToActivity = new Intent();
        sentToActivity.setAction(Constants.ACTION_REFRESH_LIST_PUSH);
        LocalBroadcastManager.getInstance(getActivityContext()).sendBroadcast(sentToActivity);
    }
}
