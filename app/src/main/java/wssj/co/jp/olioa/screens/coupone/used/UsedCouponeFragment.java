package wssj.co.jp.olioa.screens.coupone.used;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;
import wssj.co.jp.olioa.model.coupone.Coupone;

/**
 * Created by thang on 1/2/2018.
 */

public class UsedCouponeFragment extends BaseFragment<IUsedCouponeView, UsedCouponePresenter> implements IUsedCouponeView {
    private static String TAG = "UsedCouponeFragment";
    private TextView txtNone;
    private ListView listViewUsedCoupone;
    private UsedCouponeAdapter usedCouponeAdapter;
    private List<Coupone> arrayCoupones;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_COUPONE;
    }

    public static UsedCouponeFragment newInstance(Bundle args) {
        UsedCouponeFragment fragment = new UsedCouponeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String getAppBarTitle() {
        return "クーポン";
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_used_coupone;
    }

    @Override
    protected UsedCouponePresenter onCreatePresenter(IUsedCouponeView view) {
        return new UsedCouponePresenter(view);
    }

    @Override
    protected IUsedCouponeView onCreateView() {
        return this;
    }

    @Override
    protected void initAction() {
        super.initAction();
        listViewUsedCoupone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle=new Bundle();
                bundle.putString("NameCouponeUd",arrayCoupones.get(position).getNameCoupon());
                bundle.putString("NickNameUd",arrayCoupones.get(position).getStoreName());
                bundle.putString("TypeUd",arrayCoupones.get(position).getTypeCouponName());
                bundle.putString("ImgAvatarUd",arrayCoupones.get(position).getStoreImg());
                bundle.putString("ImgCouponeUd",arrayCoupones.get(position).getImgCoupon());
                bundle.putString("UsageRulesUd",arrayCoupones.get(position).getPolicy());
                bundle.putString("CodeUd",arrayCoupones.get(position).getCodeCoupon());

                Date dateCurrent = new Date();
                long currentDate = dateCurrent.getTime();

                if (arrayCoupones.get(position).getIsUse() == 1) {
                    bundle.putString("DayUd","期限切れ");
                }
                if (arrayCoupones.get(position).getIsUse() == 1 && arrayCoupones.get(position).getTimeEndCoupon() < currentDate) {
                    bundle.putString("DayUd","利用済み");
                } else {
                    bundle.putString("DayUd","期限切れ");
                }
                getActivityCallback().displayScreen(IMainView.FRAGMENT_USED_DETAIL_COUPONE, false, true,bundle);
            }
        });
    }

    @Override
    protected void initViews(View rootView) {
        super.initViews(rootView);
        txtNone = (TextView) rootView.findViewById(R.id.txtCheckNone);
        listViewUsedCoupone = (ListView) rootView.findViewById(R.id.listViewUsedCoupone);
    }

    @Override
    protected void initData() {
        super.initData();
        getPresenter().getListCoupon(0, 20, 1);

    }

    @Override
    public void showListCoupone(List<Coupone> list) {
//        Logger.d("couponedasudung",list.size()+"");
        if (list.size() > 0) {
            arrayCoupones = new ArrayList<>();
            arrayCoupones=list;
            usedCouponeAdapter = new UsedCouponeAdapter(getActivityContext(), R.layout.fragment_used_coupone, list);
            listViewUsedCoupone.setAdapter(usedCouponeAdapter);
        } else {
            txtNone.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void displayErrorMessage(ErrorMessage errorMessage) {

    }
}
