package wssj.co.jp.point.screens.scanner.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import wssj.co.jp.point.R;
import wssj.co.jp.point.model.checkin.ConfirmCheckInResponse;
import wssj.co.jp.point.model.checkin.InfoStoreResponse;
import wssj.co.jp.point.screens.IActivityCallback;
import wssj.co.jp.point.screens.IMainView;
import wssj.co.jp.point.screens.base.BaseDialog;
import wssj.co.jp.point.screens.waitstoreconfirm.WaitStoreConfirmFragment;
import wssj.co.jp.point.utils.Utils;
import wssj.co.jp.point.widget.ImageRoundCorners;

/**
 * Created by HieuPT on 5/17/2017.
 */

public class ConfirmCheckInDialog extends BaseDialog<IConfirmCheckInView, ConfirmCheckInPresenter> implements IConfirmCheckInView, View.OnClickListener {

    public static final String TAG = "ConfirmCheckInDialog";

    private TextView mStoreNameTextView;

    private TextView mOkButton, mCancelButton;

    private ImageRoundCorners mImageLogoCompany;

    private ProgressBar mProgressBar;

    private final IActivityCallback mActivityCallback;

    private String mCode;

    private String mStoreName;

    private IListenerDismissDialog mCallbackDismissDialog;

    public ConfirmCheckInDialog(@NonNull Context context, IActivityCallback activityCallback, IListenerDismissDialog dismissDialog) {
        super(context, R.style.DialogTheme);
        mActivityCallback = activityCallback;
        mCallbackDismissDialog = dismissDialog;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.dialog_confirm_check_in);
        initView();
        initAction();
    }

    private void initView() {
        mStoreNameTextView = (TextView) findViewById(R.id.tvNameStore);
        mOkButton = (TextView) findViewById(R.id.tvCheckIn);
        mCancelButton = (TextView) findViewById(R.id.tvCancelCheckIn);
        mImageLogoCompany = (ImageRoundCorners) findViewById(R.id.ivLogoCompany);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void initAction() {
        mOkButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
    }

    public void showDialog(String code) {
        if (!isShowing()) {
            mCode = code;
            mOkButton.setEnabled(false);
            if (!TextUtils.isEmpty(mCode)) {
                getPresenter().getInfoStoreByCode(mCode);
            }
            show();
        }
    }

    @Override
    public void onGetInfoStoreSuccess(InfoStoreResponse.InfoStoreData data) {
        mOkButton.setEnabled(true);
        if (data != null) {
            mStoreName = data.getStoreName();
            mStoreNameTextView.setText(mStoreName);
            Utils.loadImage(getContext(), data.getLogoCompany(), mImageLogoCompany);
            showView();
        }
    }

    @Override
    public void onGetInfoStoreFailure(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        mCancelButton.callOnClick();
    }

    private void showView() {
        mProgressBar.setVisibility(View.GONE);
        mImageLogoCompany.setVisibility(View.VISIBLE);
        mStoreNameTextView.setVisibility(View.VISIBLE);
    }

    @Override
    protected ConfirmCheckInPresenter onCreatePresenter(IConfirmCheckInView view) {
        return new ConfirmCheckInPresenter(view);
    }

    @Override
    protected IConfirmCheckInView onCreateView() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCheckIn:
                showProgress();
                getPresenter().onOkButtonClicked(mCode);
                break;
            case R.id.tvCancelCheckIn:
                getPresenter().onCancelButtonClicked();
                mCallbackDismissDialog.onDismissDialog();
                break;
        }
    }

    @Override
    public void displayWaitStoreConfirmScreen(ConfirmCheckInResponse.SessionData data) {
        Log.d(TAG, "displayWaitStoreConfirmScreen");
        hideProgress();
        if (mActivityCallback != null && data != null) {
            Bundle bundle = new Bundle();
            bundle.putString(WaitStoreConfirmFragment.KEY_STORE_NAME, mStoreName);
            bundle.putInt(WaitStoreConfirmFragment.KEY_NUMBER_PEOPLE, data.getNumberPeople());
            bundle.putLong(WaitStoreConfirmFragment.KEY_TIME_WAITING, data.getTimeWaiting());
            bundle.putInt(WaitStoreConfirmFragment.KEY_NUMBER_SESSION, data.getNumberSession());
            mActivityCallback.displayScreen(IMainView.FRAGMENT_WAIT_STORE_CONFIRM, true, false, bundle);
        }
    }

    @Override
    public void onConfirmFailure(String message) {
        hideProgress();
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public interface IListenerDismissDialog {

        void onDismissDialog();
    }
}
