package wssj.co.jp.olioa.screens.dialogchoosen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.screens.base.BaseDialog;

/**
 * Created by Nguyen Huu Ta on 7/6/2017.
 */

public class DialogChoose extends BaseDialog<IDialogChooseView, DialogChoosePresenter>
        implements IDialogChooseView, View.OnClickListener {

    public interface IListenerChooseCallback {

        void onTakePhoto();

        void onChooseGallery();
    }

    private IListenerChooseCallback mCallback;

    private TextView mButtonTakePhoto, mButtonChooseGallery, mButtonCancel;

    public DialogChoose(@NonNull Context context) {
        super(context, R.style.DialogThemeChoose);
        setCancelable(false);
        setContentView(R.layout.dialog_choose);
        initView();
        initAction();
    }

    @Override
    protected DialogChoosePresenter onCreatePresenter(IDialogChooseView view) {
        return new DialogChoosePresenter(view);
    }

    @Override
    protected IDialogChooseView onCreateView() {
        return this;
    }

    private void initView() {
        mButtonTakePhoto = (TextView) findViewById(R.id.tvTakePhoto);
        mButtonChooseGallery = (TextView) findViewById(R.id.tvChooseGallery);
        mButtonCancel = (TextView) findViewById(R.id.tvCancelDialog);
    }

    private void initAction() {
        mButtonTakePhoto.setOnClickListener(this);
        mButtonChooseGallery.setOnClickListener(this);
        mButtonCancel.setOnClickListener(this);
    }

    public void show(IListenerChooseCallback callback) {
        mCallback = callback;
        show();
    }

    @Override
    public void onTakePhotoButtonClicked() {
        mCallback.onTakePhoto();
    }

    @Override
    public void onPickFromGalerryClicked() {
        mCallback.onChooseGallery();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTakePhoto:
                getPresenter().onTakePhotoButtonClicked();
                break;
            case R.id.tvChooseGallery:
                getPresenter().onPickFromGalerryButtonClicked();
                break;
            case R.id.tvCancelDialog:
                getPresenter().onCancelButtonClicked();
                break;
        }
    }
}
