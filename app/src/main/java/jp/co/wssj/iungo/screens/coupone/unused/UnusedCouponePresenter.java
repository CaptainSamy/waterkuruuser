package jp.co.wssj.iungo.screens.coupone.unused;

import java.util.List;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.coupone.Coupone;
import jp.co.wssj.iungo.model.coupone.CouponeModel;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;
import jp.co.wssj.iungo.utils.Constants;

/**
 * Created by thang on 1/2/2018.
 */

public class UnusedCouponePresenter extends FragmentPresenter<IUnusedCouponeView> {
    protected UnusedCouponePresenter(IUnusedCouponeView view) {
        super(view);
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new CouponeModel(view.getViewContext()));
    }


    public void getListCoupon(int lastCouponId, int limit, int isUsed) {
        getView().showProgress();
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(CouponeModel.class).getListCoupone(token, lastCouponId, limit, isUsed,
                new CouponeModel.IGetListCouponeResponseCallback() {
                    @Override
                    public void onGetListCouponeResponseSuccess(List<Coupone> list) {
                        getView().showListCoupone(list);
                        getView().hideProgress();
                    }
                    @Override
                    public void onGetListCouponeResponseFailure(ErrorMessage errorMessage) {
                        getView().displayErrorMessage(errorMessage);
                        getView().hideProgress();
                    }
                });
    }
}
