package wssj.co.jp.obis.screens.coupone.unused;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import wssj.co.jp.obis.R;
import wssj.co.jp.obis.model.ErrorMessage;
import wssj.co.jp.obis.screens.IMainView;
import wssj.co.jp.obis.screens.base.BaseFragment;
import wssj.co.jp.obis.utils.Logger;
import wssj.co.jp.obis.model.coupone.Coupone;

/**
 * Created by thang on 1/2/2018.
 */

public class UnusedCouponeFragment extends BaseFragment<IUnusedCouponeView, UnusedCouponePresenter> implements IUnusedCouponeView {
    private static String TAG = "UnusedCouponeFragment";
    private TextView txtNone;
    private ListView listViewUnusedCoupone;
    private UnusedCouponeAdapter unusedCouponeAdapter;
    private List<Coupone> arrayCoupones;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_COUPONE;
    }

    public static UnusedCouponeFragment newInstance(Bundle args) {
        UnusedCouponeFragment fragment = new UnusedCouponeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_unused_coupone;
    }

    @Override
    protected UnusedCouponePresenter onCreatePresenter(IUnusedCouponeView view) {
        return new UnusedCouponePresenter(view);
    }

    @Override
    public String getAppBarTitle() {
        return "クーポン";
    }

    @Override
    protected IUnusedCouponeView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        super.initViews(rootView);
        txtNone = (TextView) rootView.findViewById(R.id.txtCheckNone);
        listViewUnusedCoupone = (ListView) rootView.findViewById(R.id.listViewUnusedCoupone);
    }

    @Override
    protected void initAction() {
        super.initAction();

        listViewUnusedCoupone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("NameCouponeUn",arrayCoupones.get(position).getNameCoupon());
                bundle.putString("NickNameUn",arrayCoupones.get(position).getStoreName());
                bundle.putString("TypeUn",arrayCoupones.get(position).getTypeCouponName());
                bundle.putString("ImgAvatarUn",arrayCoupones.get(position).getStoreImg());
                bundle.putString("ImgCouponeUn",arrayCoupones.get(position).getImgCoupon());
                bundle.putString("UsageRulesUn",arrayCoupones.get(position).getPolicy());
                bundle.putString("CodeUn",arrayCoupones.get(position).getCodeCoupon());

                bundle.putInt("CouponeIDUn",arrayCoupones.get(position).getCouponId());
                bundle.putInt("isUseAgainUn",arrayCoupones.get(position).getIsUseAgain());

                Date date = new Date(arrayCoupones.get(position).getTimeEndCoupon());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String dateFormatted = formatter.format(date);

                Date dateCurrent = new Date();
                long currentDate=dateCurrent.getTime();

//                Date dateXX = new Date(arrayCoupones.get(position).getTimeEndCoupon()-currentDate);
//                SimpleDateFormat formatterXX = new SimpleDateFormat("dd");
//                String dateFormattedXX = formatterXX.format(dateXX);

                long dayxx=  arrayCoupones.get(position).getTimeEndCoupon()-currentDate;
                bundle.putString("ExpiryDateUn",dateFormatted);
                bundle.putString("DayUn","後"+ (TimeUnit.DAYS.convert(dayxx, TimeUnit.MILLISECONDS)+1)+"日");

                getActivityCallback().displayScreen(IMainView.FRAGMENT_UNUSED_DETAIL_COUPONE, false, true,bundle);

            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        getPresenter().getListCoupon(0, 20, 0);
    }

    @Override
    public void showListCoupone(List<Coupone> list) {
     Logger.d("couponechuasudung", list.size() + "");
        if (list.size() > 0) {
            arrayCoupones = new ArrayList<>();
            arrayCoupones=list;
            unusedCouponeAdapter = new UnusedCouponeAdapter(getActivityContext(), R.layout.fragment_unused_coupone, list);
            listViewUnusedCoupone.setAdapter(unusedCouponeAdapter);
        } else {
            txtNone.setVisibility(View.VISIBLE);
        }
//        for (int i=0;i>list.size();i++){
//            list.get(i).getTimeEndCoupon();
//        }
    }

    @Override
    public void displayErrorMessage(ErrorMessage errorMessage) {

    }
}
