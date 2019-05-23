package wssj.co.jp.obis.screens.splash.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import wssj.co.jp.obis.BuildConfig;
import wssj.co.jp.obis.R;
import wssj.co.jp.obis.model.auth.CheckVersionAppResponse;
import wssj.co.jp.obis.screens.IActivityCallback;
import wssj.co.jp.obis.screens.IMainView;
import wssj.co.jp.obis.screens.MainActivity;
import wssj.co.jp.obis.screens.base.BaseDialog;

/**
 * Created by Nguyen Huu Ta on 7/8/2017.
 */

public class DialogAskUpdate extends BaseDialog<IDialogUpdateView, DialogUpdatePresenter> implements IDialogUpdateView, View.OnClickListener {

    public static final String STATUS_RUNNING = "running";

    public static final String STATUS_PREPARE = "prepare";

    public static final String STATUS_MAINTAIN = "maintain";

    private TextView mButtonUpdate, mButtonLater, mButtonMaintain;

    private TextView mTextContentUpdate;

    private LinearLayout mLayoutButtonUpdate;

    private IActivityCallback mActivityCallback;

    private Context mContext;

    private CheckVersionAppResponse.CheckVersionAppData mInfoUpdate;

    public DialogAskUpdate(@NonNull Context context, CheckVersionAppResponse.CheckVersionAppData response, final IActivityCallback activityCallback) {
        super(context);
        mContext = context;
        mInfoUpdate = response;
        mActivityCallback = activityCallback;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.layout_dialog_ask_update);
        initView();
        initAction();
        initData();
    }

    @Override
    protected DialogUpdatePresenter onCreatePresenter(IDialogUpdateView view) {
        return new DialogUpdatePresenter(view);
    }

    @Override
    protected IDialogUpdateView onCreateView() {
        return this;
    }

    private void initView() {
        mTextContentUpdate = (TextView) findViewById(R.id.tvContentUpdate);
        mButtonUpdate = (TextView) findViewById(R.id.tvUpdate);
        mButtonLater = (TextView) findViewById(R.id.tvLater);
        mButtonMaintain = (TextView) findViewById(R.id.tvMaintain);
        mLayoutButtonUpdate = (LinearLayout) findViewById(R.id.layoutButtonUpdate);
    }

    private void initAction() {
        mButtonUpdate.setOnClickListener(this);
        mButtonLater.setOnClickListener(this);
        mButtonMaintain.setOnClickListener(this);

    }

    private String mStatus;

    private void initData() {
        if (mInfoUpdate != null) {
            if (mInfoUpdate.getServerInfo() != null) {
                mStatus = mInfoUpdate.getServerInfo().getStatus();
                switch (mStatus) {
                    case STATUS_RUNNING:
                        statusUpdate(mInfoUpdate.isHasUpdate());
                        break;
                    case STATUS_PREPARE:
                        mTextContentUpdate.setText(mInfoUpdate.getServerInfo().getMessage());
                        statusUpdate(mInfoUpdate.isHasUpdate());
                        break;
                    case STATUS_MAINTAIN:
                        mTextContentUpdate.setText(mInfoUpdate.getServerInfo().getMessage());
                        statusUpdate(false);
                        break;
                    default:
                        break;

                }
            }
        }
    }

    private void statusUpdate(boolean haUpdate) {
        if (haUpdate) {
            mButtonMaintain.setVisibility(View.GONE);
            mLayoutButtonUpdate.setVisibility(View.VISIBLE);
        } else {
            mButtonMaintain.setVisibility(View.VISIBLE);
            mLayoutButtonUpdate.setVisibility(View.GONE);
        }
    }

    public void showDialog() {
        show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvUpdate:
                dismiss();
                final String appPackageName = BuildConfig.APPLICATION_ID; // getPackageName() from Context or Activity object
                try {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException ex) {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                if (mContext != null && mContext instanceof MainActivity) {
                    ((MainActivity) mContext).finish();
                }
                break;
            case R.id.tvLater:
                dismiss();
                if (mInfoUpdate.getVersionInfo().isRequired()) {
                    if (mContext != null && mContext instanceof MainActivity) {
                        ((MainActivity) mContext).finish();
                    }
                } else {
                    mActivityCallback.displayScreen(IMainView.FRAGMENT_TIMELINE, false, false);
                }
                break;
            case R.id.tvMaintain:
                dismiss();
                if (mStatus.equals(STATUS_MAINTAIN)) {
                    if (mContext != null && mContext instanceof MainActivity) {
                        ((MainActivity) mContext).finish();
                    }
                } else if (mStatus.equals(STATUS_PREPARE)) {
                    mActivityCallback.displayScreen(IMainView.FRAGMENT_TIMELINE, false, false);
                }
                break;

        }
    }
}
