package jp.co.wssj.iungo.screens.pushnotification.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.database.DBManager;
import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.model.pushnotification.ContentPushResponse;
import jp.co.wssj.iungo.model.pushnotification.QuestionNaireResponse;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.QuestionNaireActivity;
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

    private TextView mButtonRating, mButtonQuestionNaire;

    private NotificationMessage mNotificationMessage;

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
        mButtonQuestionNaire = (TextView) rootView.findViewById(R.id.buttonQuestionNaire);
        mTextLike = (TextView) rootView.findViewById(R.id.tvLike);
        mTextUnlike = (TextView) rootView.findViewById(R.id.tvUnlike);

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
        mButtonQuestionNaire.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivityContext(), QuestionNaireActivity.class);
                intent.putExtra(QuestionNaireActivity.KEY_QUESTION_NAIRE, mCode);
                startActivity(intent);
            }
        });
        mTextLike.setOnClickListener(this);
        mTextUnlike.setOnClickListener(this);
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
                mTitle.setText(mNotificationMessage.getTitle().trim());
                mBody.getSettings().setJavaScriptEnabled(true);
                mBody.getSettings().setBuiltInZoomControls(true);
                mBody.getSettings().setDisplayZoomControls(false);
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
                            mButtonQuestionNaire.setVisibility(View.VISIBLE);
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
                    mBody.loadDataWithBaseURL(null, mNotificationMessage.getMessage(), "text/html", "UTF-8", null);
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
        if (TextUtils.isEmpty(mCode)) {
            mButtonQuestionNaire.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetQuestionNaireFailure() {
        mButtonQuestionNaire.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvLike:
                updateStatusLike(1);
                if (mNotificationMessage != null) {
                    mNotificationMessage.setIsLike(1);
                    mDatabase.likePush(mNotificationMessage.getPushId(), 1);
                }
                break;
            case R.id.tvUnlike:
                updateStatusLike(0);
                if (mNotificationMessage != null) {
                    mNotificationMessage.setIsLike(0);
                    mDatabase.likePush(mNotificationMessage.getPushId(), 0);
                }
                break;
        }
    }

    public void updateStatusLike(int status) {
        switch (status) {
            case 0:
                mTextLike.setVisibility(View.VISIBLE);
                mTextUnlike.setVisibility(View.GONE);
                break;
            case 1:
                mTextLike.setVisibility(View.GONE);
                mTextUnlike.setVisibility(View.VISIBLE);
                break;
        }
    }

}
