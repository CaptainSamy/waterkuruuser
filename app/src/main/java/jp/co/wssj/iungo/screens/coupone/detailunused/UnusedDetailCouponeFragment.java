package jp.co.wssj.iungo.screens.coupone.detailunused;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.coupone.detailused.UsedDetailCouponeFragment;
import jp.co.wssj.iungo.screens.coupone.unused.UnusedCouponeFragment;
import jp.co.wssj.iungo.utils.Logger;

/**
 * Created by thang on 1/4/2018.
 */

public class UnusedDetailCouponeFragment extends BaseFragment<IUnusedDetailCouponeView, UnusedDetailCouponePresenter> implements IUnusedDetailCouponeView {

    private static String TAG = "UnusedDetailCouponeFragment";
    private TextView txtNameCoupone;
    private TextView txtNickName;
    private TextView txtTypeCoupone;
    private TextView txtExpiryDate;
    private TextView txtDay;
    private ImageView imgAvater;
    private TextView txtUsageRules;

    private ImageView imgCoupone;
    private Button btnCode;
    private Button btnUse;
    private Dialog dialog;
    private int couponeID, isUseAgian;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_UNUSED_DETAIL_COUPONE;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_detail_unused_coupone;
    }

    @Override
    public String getAppBarTitle() {
        return "おトク情報詳細";
    }


    @Override
    protected UnusedDetailCouponePresenter onCreatePresenter(IUnusedDetailCouponeView view) {
        return new UnusedDetailCouponePresenter(view);
    }

    @Override
    protected IUnusedDetailCouponeView onCreateView() {
        return this;
    }

    @Override
    protected void initAction() {
        super.initAction();
        btnUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 1/5/2018 request api use coupone
                showAlertDialog();
            }
        });
    }

    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivityContext());
        builder.setMessage("このクーポンの利用回数は一回のみです。担当者を見せてから利用ボタンを押してください");
        builder.setCancelable(false);
        builder.setPositiveButton("利用", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getPresenter().useCoupon(couponeID, isUseAgian);
            }
        });
        builder.setNegativeButton("利用しない", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void showDialog() {
        dialog = new Dialog(getActivityContext());
        dialog.setContentView(R.layout.dialog_coupone);
        dialog.findViewById(R.id.buttonOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                btnUse.setVisibility(View.GONE);
            }
        });
        dialog.show();
    }

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getArguments();
        txtNameCoupone.setText(bundle.getString("NameCouponeUn"));
        txtNickName.setText(bundle.getString("NickNameUn"));
        txtTypeCoupone.setText(bundle.getString("TypeUn"));
        Picasso.with(getActivityContext()).load(bundle.getString("ImgAvatarUn")).into(imgAvater);
        Picasso.with(getActivityContext()).load(bundle.getString("ImgCouponeUn")).into(imgCoupone);
        txtUsageRules.setText(bundle.getString("UsageRulesUn"));
        btnCode.setText(bundle.getString("CodeUn"));

        txtExpiryDate.setText(bundle.getString("ExpiryDateUn"));
        txtDay.setText(bundle.getString("DayUn"));
        couponeID = bundle.getInt("CouponeIDUn");
        isUseAgian = bundle.getInt("isUseAgainUn");

    }

    public static UnusedDetailCouponeFragment newInstance(Bundle bundle) {
        UnusedDetailCouponeFragment fragment = new UnusedDetailCouponeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initViews(View rootView) {
        super.initViews(rootView);
        txtNameCoupone = (TextView) rootView.findViewById(R.id.txtNameCoupone);
        txtNickName = (TextView) rootView.findViewById(R.id.txtNickName);
        txtTypeCoupone = (TextView) rootView.findViewById(R.id.txtTypeCoupone);
        txtExpiryDate = (TextView) rootView.findViewById(R.id.txtExpiryDate);
        txtDay = (TextView) rootView.findViewById(R.id.txtDay);
        imgAvater = (ImageView) rootView.findViewById(R.id.imgAvatar);
        txtUsageRules = (TextView) rootView.findViewById(R.id.txtUsageRules);

        btnCode = (Button) rootView.findViewById(R.id.buttonCode);
        btnUse = (Button) rootView.findViewById(R.id.buttonUse);
        imgCoupone = (ImageView) rootView.findViewById(R.id.imageCoupone);
    }

    @Override
    public void useCoupone(Boolean kq) {
        Logger.d("thangdd", kq + "");
        //    backToPreviousScreen();
        if (kq == true) {
            showDialog();
        }
    }

    @Override
    public void displayErrorMessage(ErrorMessage errorMessage) {

    }
}
