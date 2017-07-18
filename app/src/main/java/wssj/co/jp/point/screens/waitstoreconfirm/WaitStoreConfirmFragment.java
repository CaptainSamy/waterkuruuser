package wssj.co.jp.point.screens.waitstoreconfirm;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import wssj.co.jp.point.R;
import wssj.co.jp.point.model.checkin.CheckInStatusResponse;
import wssj.co.jp.point.screens.IMainView;
import wssj.co.jp.point.screens.base.BaseFragment;
import wssj.co.jp.point.utils.Constants;
import wssj.co.jp.point.utils.Logger;
import wssj.co.jp.point.utils.Utils;
import wssj.co.jp.point.widget.CircularProgressBar;

/**
 * Created by HieuPT on 5/19/2017.
 */

public class WaitStoreConfirmFragment extends BaseFragment<IWaitStoreConfirmView, WaitStoreConfirmPresenter>
        implements IWaitStoreConfirmView {

    private static final String TAG = "WaitStoreConfirmFragment";

    public static final String KEY_STATUS_CHECK_IN = "KEY_STATUS_CHECK_IN";

    public static final String KEY_STORE_NAME = "KEY_STORE_NAME";

    public static final String KEY_NUMBER_PEOPLE = "KEY_NUMBER_PEOPLE";

    public static final String KEY_TIME_WAITING = "KEY_TIME_WAITING";

    public static final String KEY_NUMBER_SESSION = "KEY_NUMBER_SESSION";

    private CircularProgressBar mCircleProgress;

    private TextView mExpandWaitingText;

    private TextView mTextStoreName;

    private TextView mTextPositionCustomer, mTextNumberCustomer, mTextTimeWaiting;

    private LinearLayout mLayoutFinishWaiting, mLayoutInfo;

    private final Handler mHandler = new Handler();

    private String mStoreName;

    private int mNumberCustomer;

    private int mPositionCustomer;

    private long mTimeWaiting;

    private ImageView mImageStore;

    private boolean isAllowCheckIn;

    public static WaitStoreConfirmFragment newInstance(Bundle args) {
        WaitStoreConfirmFragment fragment = new WaitStoreConfirmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_WAIT_STORE_CONFIRM;
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return true;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_waiting_store_confirm_checkin);
    }

    @Override
    public int getMenuBottomID() {
        return MENU_HOME;
    }

    @Override
    public boolean isDisplayNavigationButton() {
        return false;
    }

    @Override
    public boolean isDisplayActionBar() {
        return true;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_wait_store_confirm;
    }

    @Override
    protected WaitStoreConfirmPresenter onCreatePresenter(IWaitStoreConfirmView view) {
        return new WaitStoreConfirmPresenter(view);
    }

    @Override
    protected IWaitStoreConfirmView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        mImageStore = (ImageView) rootView.findViewById(R.id.imageStore);
        mCircleProgress = (CircularProgressBar) rootView.findViewById(R.id.circle_progress);
        mTextPositionCustomer = (TextView) rootView.findViewById(R.id.tvPositionCustomer);
        mTextNumberCustomer = (TextView) rootView.findViewById(R.id.tvNumberCustomer);
        mTextTimeWaiting = (TextView) rootView.findViewById(R.id.tvTimeWaiting);
        mCircleProgress.setProgress(0.0f);
        final RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(5000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setFillEnabled(true);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mImageStore.startAnimation(rotateAnimation);
                mCircleProgress.repeatAnimation(-1);
                if (!TextUtils.isEmpty(mStatusCheckIn)) {
                    switch (mStatusCheckIn) {
                        case Constants.CheckInStatus.STATUS_WAIT_CONFIRM:
                            getPresenter().getCheckInStatus();
                            break;
                        case Constants.CheckInStatus.STATUS_CHECKED_IN:
                            isAllowCheckIn = true;
                            mCircleProgress.setRepeat(1);
                            break;
                        case Constants.CheckInStatus.STATUS_CHECKED_OUT:
                            isAllowCheckIn = true;
                            mCircleProgress.setRepeat(1);
                            break;
                    }
                }
            }
        }, 1000);
        mExpandWaitingText = (TextView) rootView.findViewById(R.id.expand_wait_text);
        mLayoutFinishWaiting = (LinearLayout) rootView.findViewById(R.id.finish_wait_layout);
        mLayoutInfo = (LinearLayout) rootView.findViewById(R.id.layoutInfo);
        mExpandWaitingText.animate().alpha(1.0f).setDuration(1000).setStartDelay(500);
        mTextStoreName = (TextView) rootView.findViewById(R.id.tvCardName);
    }

    @Override
    protected void initAction() {
        mCircleProgress.setAnimateListener(new CircularProgressBar.IOnAnimateListener() {

            @Override
            public void onAnimateDone() {
                if (isAdded()) {
                    Animation zoomInAnim = AnimationUtils.loadAnimation(getActivityContext(), R.anim.zoom_in);
                    zoomInAnim.setStartOffset(500);
                    mImageStore.setAnimation(zoomInAnim);
                    if (isAllowCheckIn) {
                        mLayoutInfo.setVisibility(View.GONE);
                        mExpandWaitingText.setVisibility(View.VISIBLE);
                        mExpandWaitingText.setText(getString(R.string.expand_waiting_confirm_prepare_finish));
                        mLayoutFinishWaiting.setVisibility(View.VISIBLE);
                        mLayoutFinishWaiting.animate().alpha(1.0f).setDuration(1000);
                        Animation zoomOutAnim = AnimationUtils.loadAnimation(getActivityContext(),
                                R.anim.zoom_out);
                        zoomOutAnim.setStartOffset(500);
                        mLayoutFinishWaiting.startAnimation(zoomOutAnim);
                        mHandler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                Bundle bundle = new Bundle();
                                bundle.putString(KEY_STORE_NAME, mStoreName);
                                getActivityCallback().displayScreen(IMainView.FRAGMENT_MANAGER_STAMP, true, false, bundle);
                            }
                        }, 2500);
                    } else {
                        getActivityCallback().displayScreen(IMainView.FRAGMENT_SCANNER, true, false);
                    }
                }
            }
        });
    }

    private String mStatusCheckIn;

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mStatusCheckIn = bundle.getString(KEY_STATUS_CHECK_IN);
            mStoreName = bundle.getString(KEY_STORE_NAME);
            mNumberCustomer = bundle.getInt(KEY_NUMBER_PEOPLE);
            mPositionCustomer = bundle.getInt(KEY_NUMBER_SESSION);
            mTimeWaiting = bundle.getLong(KEY_TIME_WAITING) - System.currentTimeMillis();
            mTextStoreName.setText(mStoreName == null ? Constants.EMPTY_STRING : mStoreName);

            mTextPositionCustomer.setText(String.valueOf(mPositionCustomer));
            mTextNumberCustomer.setText(getString(R.string.number_customer_waiting, String.valueOf(mNumberCustomer)));
            String time = Utils.convertLongToDate(mTimeWaiting);
            mTextTimeWaiting.setText(time);


        }
    }

    @Override
    public void recheckStatus(int delayTimeMs, CheckInStatusResponse.CheckInStatusData data) {
        Logger.d(TAG, "#recheckStatus");
        if (data != null) {
            mNumberCustomer = data.getNumberPeople();
            mPositionCustomer = data.getNumberSession();
            mTimeWaiting = data.getTimeWaiting() - System.currentTimeMillis();
            if (mTimeWaiting < 0) {
                mTimeWaiting = 0;
            }
            mTextPositionCustomer.setText(String.valueOf(mPositionCustomer));
            mTextNumberCustomer.setText(getString(R.string.number_customer_waiting, String.valueOf(mNumberCustomer)));
            String time = Utils.convertLongToDate(mTimeWaiting);
            mTextTimeWaiting.setText(time);
        }
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                getPresenter().getCheckInStatus();
            }
        }, delayTimeMs);
    }

    @Override
    public void displayScreenManageStamp(int serviceId) {
        Logger.d(TAG, "#displayScreenManageStamp");
        isAllowCheckIn = true;
        mCircleProgress.setRepeat(1);
    }

    @Override
    public void displayScreenScanCode() {
        Logger.d(TAG, "#displayScreenScanCode");
        isAllowCheckIn = false;
        mCircleProgress.setRepeat(1);
    }

    public void stopCheckingStatus() {
        mHandler.removeCallbacksAndMessages(null);
    }
}
