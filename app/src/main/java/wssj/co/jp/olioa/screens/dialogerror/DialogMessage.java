package wssj.co.jp.olioa.screens.dialogerror;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.screens.base.BaseDialog;
import wssj.co.jp.olioa.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 21/6/2017.
 */

public class DialogMessage extends BaseDialog<IDialogErrorView, DialogErrorPresenter> implements IDialogErrorView, View.OnClickListener {

    private TextView mTextTitle, mTextContent;

    private TextView mYes, mCancel;

    private View midLine;

    private IOnClickListener mCallback;

    public DialogMessage(@NonNull Context context, IOnClickListener callback) {
        super(context, R.style.DialogTheme);
        mCallback = callback;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_message);
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
        mYes = (TextView) findViewById(R.id.tvYes);
        midLine = findViewById(R.id.mid_line);
        mCancel = (TextView) findViewById(R.id.tvCancel);
    }

    public void initAction() {
        mYes.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    public void setTitle(String title) {
        mTextTitle.setText(title);
    }

    public void initData(String content, String yes, String cancel) {
        mTextContent.setText(content);
        mYes.setText(yes);
        mCancel.setText(cancel);
    }

    public void initData(String content, String yes) {
        mTextContent.setText(content);
        mYes.setText(yes);
        mCancel.setVisibility(View.GONE);
        midLine.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvYes:
                dismissDialogView();
                if (mCallback != null) {
                    mCallback.buttonYesClick();
                }
                break;
            case R.id.tvCancel:
                dismissDialogView();
                if (mCallback != null){
                    mCallback.buttonCancelClick();
                }
                break;
        }
    }

    public interface IOnClickListener {

        void buttonYesClick();

        void buttonCancelClick();

    }
}
