package wssj.co.jp.olioa.screens.dialogphoto;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.screens.base.BaseDialog;
import wssj.co.jp.olioa.widget.ImageRoundCorners;

/**
 * Created by Nguyen Huu Ta on 19/5/2017.
 */

public class PhotoDialog extends BaseDialog<IPhotoDialogView, PhotoDialogPresenter> implements IPhotoDialogView, View.OnClickListener {

    public interface IOnDeleteButtonClickListener {

        void onDeleteClicked(int imageCode);
    }

    private final IOnDeleteButtonClickListener mCallback;

    private ImageView mPhoto;

    private RelativeLayout mButtonDone, mButtonDelete;

    private int mImageCode;

    public PhotoDialog(@NonNull Context context, IOnDeleteButtonClickListener callback) {
        super(context, R.style.DialogTheme);
        mCallback = callback;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.photo_dialog);
        initView();
        initAction();
    }

    private void initView() {
        mButtonDone = (RelativeLayout) findViewById(R.id.layoutDone);
        mButtonDelete = (RelativeLayout) findViewById(R.id.layoutDelete);
        mPhoto = (ImageRoundCorners) findViewById(R.id.ivPhoto);
    }

    private void initAction() {
        mButtonDone.setOnClickListener(this);
        mButtonDelete.setOnClickListener(this);
    }

    @Override
    protected IPhotoDialogView onCreateView() {
        return this;
    }

    @Override
    protected PhotoDialogPresenter onCreatePresenter(IPhotoDialogView view) {
        return new PhotoDialogPresenter(view);
    }

    public void showImage(Drawable bitmap, int imageCode) {
        mPhoto.setImageDrawable(bitmap);
        mImageCode = imageCode;
        show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutDone:
                getPresenter().dismissDialog();
                break;
            case R.id.layoutDelete:
                if (mCallback != null) {
                    mCallback.onDeleteClicked(mImageCode);
                }
                getPresenter().dismissDialog();
                break;
        }
    }
}

