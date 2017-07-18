package jp.co.wssj.iungo.screens.dialogerror;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.screens.base.BaseDialog;

/**
 * Created by Nguyen Huu Ta on 21/6/2017.
 */

public class DialogError extends BaseDialog<IDialogErrorView, DialogErrorPresenter> implements IDialogErrorView, View.OnClickListener {

    private TextView mTextTitle, mTextContent;

    private TextView mTryAgain, mCancel;

    private IOnClickListener mCallback;

    public DialogError(@NonNull Context context, IOnClickListener callback) {
        super(context, R.style.DialogTheme);
        mCallback = callback;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_error);
        initView();
        initAction();
    }

    @Override
    protected DialogErrorPresenter onCreatePresenter(IDialogErrorView view) {
        return new DialogErrorPresenter(view);
    }

    @Override
    protected IDialogErrorView onCreateView() {
        return this;
    }

    public void initView() {
        mTextTitle = (TextView) findViewById(R.id.tvTitle);
        mTextContent = (TextView) findViewById(R.id.tvContent);
        mTryAgain = (TextView) findViewById(R.id.tvTryAgain);
        mCancel = (TextView) findViewById(R.id.tvCancel);
    }

    public void initAction() {
        mTryAgain.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTryAgain:
                dismissDialogView();
                mCallback.onTryAgainClick();
                break;
            case R.id.tvCancel:
                dismissDialogView();
                break;
        }
    }

    public interface IOnClickListener {

        void onTryAgainClick();

    }
}
