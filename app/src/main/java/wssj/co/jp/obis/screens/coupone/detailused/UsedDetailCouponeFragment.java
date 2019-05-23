package wssj.co.jp.obis.screens.coupone.detailused;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import wssj.co.jp.obis.R;
import wssj.co.jp.obis.screens.IMainView;
import wssj.co.jp.obis.screens.base.BaseFragment;

/**
 * Created by thang on 1/4/2018.
 */

public class UsedDetailCouponeFragment extends BaseFragment<IUsedDetailCouponeView, UsedDeailCouponePresenter> implements IUsedDetailCouponeView {

    private static String TAG = "UsedDetailCouponeFragment";
    private TextView txtNameCoupone;
    private TextView txtNickName;
    private TextView txtTypeCoupone;
    private TextView txtExpiryDate;
    private TextView txtDay;
    private ImageView imgAvater;
    private TextView txtUsageRules;
    private Button btnCode;
    private ImageView imgCoupone;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_USED_DETAIL_COUPONE;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_detail_used_coupone;
    }

    public static UsedDetailCouponeFragment newInstance(Bundle bundle) {
        UsedDetailCouponeFragment fragment = new UsedDetailCouponeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public String getAppBarTitle() {
        return "おトク情報詳細";
    }

    @Override
    protected UsedDeailCouponePresenter onCreatePresenter(IUsedDetailCouponeView view) {
        return new UsedDeailCouponePresenter(view);
    }

    @Override
    protected IUsedDetailCouponeView onCreateView() {
        return this;
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
        imgCoupone = (ImageView) rootView.findViewById(R.id.imageCoupone);
    }

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle=getArguments();
        txtNameCoupone.setText(bundle.getString("NameCouponeUd"));
        txtNickName.setText(bundle.getString("NickNameUd"));
        txtTypeCoupone.setText(bundle.getString("TypeUd"));
        Picasso.with(getActivityContext()).load(bundle.getString("ImgAvatarUd")).into(imgAvater);
        Picasso.with(getActivityContext()).load(bundle.getString("ImgCouponeUd")).into(imgCoupone);
        txtUsageRules.setText(bundle.getString("UsageRulesUd"));
        btnCode.setText(bundle.getString("CodeUd"));
        txtDay.setText(bundle.getString("DayUd"));
    }

    @Override
    protected void initAction() {
        super.initAction();
    }
}
