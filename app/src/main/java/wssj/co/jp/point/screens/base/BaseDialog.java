package wssj.co.jp.point.screens.base;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Window;

import wssj.co.jp.point.R;

/**
 * Created by HieuPT on 4/10/2017.
 */

public abstract class BaseDialog<V extends IDialogView, P extends BasePresenter<V>> extends Dialog implements IDialogView {

    private final P mPresenter;

    public BaseDialog(@NonNull Context context) {
        this(context, 0);
    }

    private ProgressDialog mProgressDialog;

    public BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mPresenter = onCreatePresenter(onCreateView());
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setCancelable(false);
    }

    @Override
    public void onAttachedToWindow() {
        mPresenter.onViewAttached();
    }

    @Override
    public void onDetachedFromWindow() {
        mPresenter.onViewDetached();
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    public void showProgress() {
        if (mProgressDialog != null && mPresenter.isViewAttached()) {
            mProgressDialog.show();
        }
    }

    public void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing() && mPresenter.isViewAttached()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void dismissDialogView() {
        dismiss();
    }

    protected P getPresenter() {
        return mPresenter;
    }

    protected abstract P onCreatePresenter(V view);

    protected abstract V onCreateView();

    private class ProgressDialog extends Dialog {

        private ProgressDialog(Context context) {
            super(context);
            setCancelable(false);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_progress);
            Window window = getWindow();
            if (window != null) {
                getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }
        }
    }
}
