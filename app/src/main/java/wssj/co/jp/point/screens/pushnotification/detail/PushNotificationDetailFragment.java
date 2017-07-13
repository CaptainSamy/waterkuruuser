package wssj.co.jp.point.screens.pushnotification.detail;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import wssj.co.jp.point.R;
import wssj.co.jp.point.model.firebase.NotificationMessage;
import wssj.co.jp.point.screens.IMainView;
import wssj.co.jp.point.screens.base.BaseFragment;
import wssj.co.jp.point.screens.dialograting.DialogRating;
import wssj.co.jp.point.utils.Constants;
import wssj.co.jp.point.utils.Logger;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationDetailFragment extends BaseFragment<IPushNotificationDetailView, PushNotificationDetailPresenter> implements IPushNotificationDetailView {

    public static final String TAG = "PushNotificationDetailFragment";

    public static final String NOTIFICATION_ARG = "notification";

    public static final String NOTIFICATION_SHOW_RATING = "show_rating";

    private TextView mTitle, mBody, mTime;

    private TextView mButtonRating;

    private NotificationMessage mNotificationMessage;

    public static PushNotificationDetailFragment newInstance(Bundle bundle) {
        PushNotificationDetailFragment fragment = new PushNotificationDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getLogTag() {
        return "PushNotificationDetailFragment";
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
        mBody = (TextView) rootView.findViewById(R.id.body_notification);
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
        if (bundle != null) {
            mNotificationMessage = (NotificationMessage) bundle.getSerializable(NOTIFICATION_ARG);
            boolean isShowRating = bundle.getInt(NOTIFICATION_SHOW_RATING) == 1 ? true : false;
            if (mNotificationMessage != null) {
                mTitle.setText(mNotificationMessage.getTitle());
                mBody.setText(mNotificationMessage.getMessage());
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(mNotificationMessage.getPushTime());
                String time = String.format(Locale.getDefault(), "%02d", calendar.get(Calendar.HOUR_OF_DAY))
                        + ":" + String.format(Locale.getDefault(), "%02d", calendar.get(Calendar.MINUTE))
                        + "   " + String.format(Locale.getDefault(), "%04d", calendar.get(Calendar.YEAR))
                        + "-" + String.format(Locale.getDefault(), "%02d", calendar.get(Calendar.MONTH) + 1)
                        + "-" + String.format(Locale.getDefault(), "%02d", calendar.get(Calendar.DAY_OF_MONTH));
                mTime.setText(time);
                Logger.d("TAG", mNotificationMessage.getAction());
                switch (mNotificationMessage.getAction()) {
                    case Constants.PushNotification.TYPE_NOTIFICATION:
                        mButtonRating.setVisibility(View.GONE);
                        break;
                    case Constants.PushNotification.TYPE_REMIND:
                        mButtonRating.setVisibility(View.GONE);
                        break;
                    case Constants.PushNotification.TYPE_REQUEST_REVIEW:
                        mButtonRating.setVisibility(View.VISIBLE);
                        showRating(isShowRating);
                        break;
                    default:
                        mButtonRating.setVisibility(View.GONE);
                        break;
                }
            }
        }
    }

    public void showRating(boolean isShowRating) {
        if (isShowRating && mNotificationMessage != null) {
            DialogRating dialogRating = new DialogRating(getContext(), new DialogRating.IOnClickListenerRating() {

                @Override
                public void onButtonRating(float rating, String note, int currentPosition) {
                    mButtonRating.setVisibility(View.GONE);
                }
            });
            dialogRating.show(0, mNotificationMessage.getStampId());
        }
    }
}
