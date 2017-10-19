package jp.co.wssj.iungo.screens.pushnotification.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.database.DBManager;
import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.model.pushnotification.ContentPushResponse;
import jp.co.wssj.iungo.model.pushnotification.QuestionNaireResponse;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.dialograting.DialogRating;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Utils;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationDetailFragment extends BaseFragment<IPushNotificationDetailView, PushNotificationDetailPresenter> implements IPushNotificationDetailView, View.OnClickListener {

    public static final String TAG = "PushNotificationDetailServiceCompanyFragment";

    public static final String NOTIFICATION_ARG = "notification";

    public static final String FLAG_FROM_ACTIVITY = "from_activity";

    private TextView mTitle, mTime;

    private TextView mTextLike, mTextUnlike;

    private WebView mBody;

    private TextView mButtonRating;

    private NotificationMessage mNotificationMessage;

    private ImageView mImageLike;

    private LinearLayout mLayoutLike;

    DBManager mDatabase = DBManager.getInstance();

    public static PushNotificationDetailFragment newInstance(Bundle bundle) {
        PushNotificationDetailFragment fragment = new PushNotificationDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return true;
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
        mTextLike = (TextView) rootView.findViewById(R.id.tvLike);
        mTextUnlike = (TextView) rootView.findViewById(R.id.tvUnlike);
        mImageLike = (ImageView) rootView.findViewById(R.id.ivLike);
        mLayoutLike = (LinearLayout) rootView.findViewById(R.id.layoutLike);

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
        mLayoutLike.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mNotificationMessage = (NotificationMessage) bundle.getSerializable(NOTIFICATION_ARG);
            boolean isFromActivity = bundle.getBoolean(FLAG_FROM_ACTIVITY, false);
            if (mNotificationMessage != null) {
                if (mNotificationMessage.getStatusRead() != Constants.STATUS_READ) {
                    List<Long> list = new ArrayList<>();
                    list.add(mNotificationMessage.getPushId());
                    getPresenter().setListPushUnRead(list, Constants.STATUS_READ);
                }
                updateStatusLike(mNotificationMessage.isLike());

                WebSettings webSettings = mBody.getSettings();
                mTitle.setText(mNotificationMessage.getTitle().trim());
                webSettings.setJavaScriptEnabled(true);
                webSettings.setBuiltInZoomControls(true);
                webSettings.setDisplayZoomControls(false);
                if (!TextUtils.isEmpty(mNotificationMessage.getAction())) {
                    switch (mNotificationMessage.getAction()) {
                        case Constants.PushNotification.TYPE_NOTIFICATION:
                            mButtonRating.setVisibility(View.GONE);
                            break;
                        case Constants.PushNotification.TYPE_REMIND:
                            mButtonRating.setVisibility(View.GONE);
                            break;
                        case Constants.PushNotification.TYPE_REQUEST_REVIEW:
                            mButtonRating.setVisibility(View.VISIBLE);
                            showRating(true);
                            break;
                        case Constants.PushNotification.TYPE_QUESTION_NAIRE:
                            mButtonRating.setVisibility(View.GONE);
                            getPresenter().getQuestionNaire(mNotificationMessage.getPushId());
                            break;
                        default:
                            mButtonRating.setVisibility(View.GONE);
                            break;
                    }
                }
                if (isFromActivity) {
                    getPresenter().getContentPush(mNotificationMessage.getPushId());
                } else {
                    if (!TextUtils.equals(mNotificationMessage.getAction(), Constants.PushNotification.TYPE_QUESTION_NAIRE)) {
                        mBody.loadDataWithBaseURL(null, mNotificationMessage.getMessage(), "text/html", "UTF-8", null);
                    }
                }
                String time = Utils.convertLongToTime(mNotificationMessage.getPushTime(), isFromActivity);
                mTime.setText(time);

            }
        }
    }

    public String convertlink(String s) {
        // separete input by spaces ( URLs don't have spaces )
        String[] parts = s.split("\\s");
        String link = Constants.EMPTY_STRING;
        // Attempt to convert each item into an URL.
        for (String item : parts)
            try {
                URL url = new URL(item);
                // If possible then replace with anchor...
                link = "<a href=\"" + url + "\">" + url + "</a> ";
            } catch (MalformedURLException e) {
                // If there was an URL that was not it!...
                System.out.print(item + " ");
                link = s;
            }

        return link;
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
            mTitle.setText(contentPushResponse.getTitle());
            mBody.loadDataWithBaseURL(null, contentPushResponse.getContent(), "text/html", "UTF-8", null);
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

    private String mCode;

    @Override
    public void onGetQuestionNaireSuccess(QuestionNaireResponse response) {
        mCode = response.getData().getCode();

        mBody.getSettings().setLoadWithOverviewMode(true);
        mBody.getSettings().setUseWideViewPort(true);
        mBody.setWebViewClient(new WebViewClient());
        mBody.loadUrl(mCode);
    }

    @Override
    public void onGetQuestionNaireFailure() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutLike:
                int status = mTextLike.isShown() ? 1 : 0;
                updateStatusLike(status);
                if (mNotificationMessage != null) {
                    mNotificationMessage.setIsLike(status);
                    mDatabase.likePush(mNotificationMessage.getPushId(), status);
                }
                break;
        }
    }

    public void updateStatusLike(int status) {
        switch (status) {
            case 0:
                mTextLike.setVisibility(View.VISIBLE);
                mTextUnlike.setVisibility(View.GONE);
                mImageLike.setImageDrawable(getResources().getDrawable(R.drawable.like_default));
                break;
            case 1:
                mTextLike.setVisibility(View.GONE);
                mTextUnlike.setVisibility(View.VISIBLE);
                mImageLike.setImageDrawable(getResources().getDrawable(R.drawable.like_choosen));
                break;
        }
    }

}
