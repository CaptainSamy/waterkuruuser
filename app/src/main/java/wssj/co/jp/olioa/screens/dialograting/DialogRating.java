package wssj.co.jp.olioa.screens.dialograting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.List;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.utils.Utils;
import wssj.co.jp.olioa.screens.MainActivity;
import wssj.co.jp.olioa.screens.base.BaseDialog;

/**
 * Created by Nguyen Huu Ta on 23/6/2017.
 */

public class DialogRating extends BaseDialog<IDialogRatingView, DialogRatingPresenter> implements IDialogRatingView {

    private SimpleRatingBar mRating;

    private TextView mButtonSaveRating;

    private EditText mInputRating;

    private ImageView mButtonBack;

    private int mCurrentPosition, mStampId;

    private LinearLayout mLayoutDialog;

    private IOnClickListenerRating mCallback;

    public DialogRating(@NonNull Context context, IOnClickListenerRating callback) {
        super(context, R.style.DialogTheme);
        mCallback = callback;
        setOwnerActivity((MainActivity) context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_rating);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);
        initView();
        initAction();
    }

    @Override
    protected DialogRatingPresenter onCreatePresenter(IDialogRatingView view) {
        return new DialogRatingPresenter(view);
    }

    @Override
    protected IDialogRatingView onCreateView() {
        return this;
    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, @Nullable Menu menu, int deviceId) {

    }

    private void initView() {
        mLayoutDialog = (LinearLayout) findViewById(R.id.layoutDialog);
        Utils.setupUI(mLayoutDialog, this);
        mButtonBack = (ImageView) findViewById(R.id.buttonBack);
        mRating = (SimpleRatingBar) findViewById(R.id.ratingBar);
        mButtonSaveRating = (TextView) findViewById(R.id.buttonSaveRating);
        mInputRating = (EditText) findViewById(R.id.inputRating);

    }

    private void initAction() {
        mButtonSaveRating.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String review = mInputRating.getText().toString().trim();
                if (mRating.getRating() > 0 || !TextUtils.isEmpty(review)) {
                    showProgress();
                    getPresenter().reviewServiceByStamp(mStampId, mRating.getRating(), review);
                }

            }
        });
        mButtonBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismissDialogView();
            }
        });
    }

    public void show(int position, int stampId) {
        mRating.setRating(0.0f);
        mInputRating.setText("");
        mCurrentPosition = position;
        mStampId = stampId;
        show();
    }

    public void show(int position, int stampId, float rating, String note) {
        mCurrentPosition = position;
        mStampId = stampId;
        mRating.setRating(rating);
        mInputRating.setText(note);
        show();
    }

    @Override
    public void reviewServiceSuccess(String message) {
        hideProgress();
        dismissDialogView();
        mCallback.onButtonRating(mRating.getRating(), mInputRating.getText().toString().trim(), mCurrentPosition);
        mRating.setRating(0.0f);
        mInputRating.setText("");
    }

    @Override
    public void reviewServiceFailure(String message) {
        hideProgress();
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(getContext(), getContext().getString(R.string.failure), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }

    }

    public interface IOnClickListenerRating {

        void onButtonRating(float rating, String note, int currentPosition);
    }

}
