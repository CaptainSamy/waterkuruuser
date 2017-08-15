package jp.co.wssj.iungo.screens.splash.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import jp.co.wssj.iungo.BuildConfig;
import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.screens.IActivityCallback;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.MainActivity;
import jp.co.wssj.iungo.screens.base.BaseDialog;

/**
 * Created by Nguyen Huu Ta on 7/8/2017.
 */

public class DialogAskUpdate extends BaseDialog<IDialogUpdateView, DialogUpdatePresenter> implements IDialogUpdateView, View.OnClickListener {

    private TextView mButtonCancel, mButtonUpdate, mButtonLater;

    private IActivityCallback mActivityCallback;

    private Context mContext;

    private boolean mIsRequired;

    public DialogAskUpdate(@NonNull Context context, boolean isRequired, final IActivityCallback activityCallback) {
        super(context);
        mContext = context;
        mIsRequired = isRequired;
        mActivityCallback = activityCallback;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.layout_dialog_ask_update);
        initView();
        initAction();
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
        mButtonCancel = (TextView) findViewById(R.id.tvCancel);
        mButtonUpdate = (TextView) findViewById(R.id.tvUpdate);
        mButtonLater = (TextView) findViewById(R.id.tvLater);
    }

    private void initAction() {
        mButtonCancel.setOnClickListener(this);
        mButtonUpdate.setOnClickListener(this);
        mButtonLater.setOnClickListener(this);

    }

    public void showDialog() {
        show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                if (mContext != null && mContext instanceof MainActivity) {
                    ((MainActivity) mContext).finish();
                }
                dismiss();
                break;
            case R.id.tvUpdate:
                final String appPackageName = BuildConfig.APPLICATION_ID; // getPackageName() from Context or Activity object
                try {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException ex) {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                dismiss();
                if (mContext != null && mContext instanceof MainActivity) {
                    ((MainActivity) mContext).finish();
                }
                break;
            case R.id.tvLater:
                dismiss();
                if (mIsRequired) {
                    if (mContext != null && mContext instanceof MainActivity) {
                        ((MainActivity) mContext).finish();
                    }
                } else {
                    mActivityCallback.displayScreen(IMainView.FRAGMENT_HOME, false, false, null);
                }
                break;

        }
    }
}
