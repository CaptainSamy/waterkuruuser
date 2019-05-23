package wssj.co.jp.obis.screens.scanner.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import wssj.co.jp.obis.R;
import wssj.co.jp.obis.model.entities.StoreInfo;
import wssj.co.jp.obis.screens.base.BaseDialog;
import wssj.co.jp.obis.utils.Utils;

/**
 * Created by HieuPT on 5/17/2017.
 */

public class ConfirmCheckInDialog extends BaseDialog<IConfirmCheckInView, ConfirmCheckInPresenter> implements IConfirmCheckInView, View.OnClickListener {

    public static final String TAG = "ConfirmCheckInDialog";

    private TextView mStoreNameTextView;

    private TextView mOkButton, mCancelButton;

    private CircleImageView mImageLogoCompany;

    private ProgressBar mProgressBar;

    private IListenerDismissDialog mCallbackDismissDialog;

    private String code;

    public ConfirmCheckInDialog(@NonNull Context context, IListenerDismissDialog dismissDialog) {
        super(context, R.style.DialogTheme);
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
        mImageLogoCompany = (CircleImageView) findViewById(R.id.ivLogoCompany);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void initAction() {
        mOkButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
    }

    public void initData(StoreInfo storeInfo) {
        this.code = storeInfo.getCheckInCode();
        mStoreNameTextView.setText(storeInfo.getName());
        Utils.fillImage(getViewContext(), storeInfo.getLogo(), mImageLogoCompany, R.drawable.image_choose);
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
                dismissDialogView();
                mCallbackDismissDialog.onConfirm(code);
                break;
            case R.id.tvCancelCheckIn:
                dismissDialogView();
                mCallbackDismissDialog.onDismissDialog();
                break;
        }
    }

    public interface IListenerDismissDialog {

        void onConfirm(String code);

        void onDismissDialog();
    }
}
