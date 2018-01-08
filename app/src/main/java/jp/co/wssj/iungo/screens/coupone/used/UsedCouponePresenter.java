package jp.co.wssj.iungo.screens.coupone.used;

import java.util.List;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.coupone.Coupone;
import jp.co.wssj.iungo.model.coupone.CouponeModel;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;

/**
 * Created by thang on 1/2/2018.
 */

public class UsedCouponePresenter extends FragmentPresenter<IUsedCouponeView> {
    protected UsedCouponePresenter(IUsedCouponeView view) {
        super(view);
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new CouponeModel(view.getViewContext()));
    }

    public void getListCoupon(int lastCouponId, int limit, int isUsed) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(CouponeModel.class).getListCoupone(token, lastCouponId, limit, isUsed,
                new CouponeModel.IGetListCouponeResponseCallback() {
                    @Override
                    public void onGetListCouponeResponseSuccess(List<Coupone> list) {
                        getView().showListCoupone(list);
                    }
                    @Override
                    public void onGetListCouponeResponseFailure(ErrorMessage errorMessage) {
                        getView().displayErrorMessage(errorMessage);
                    }
                });
    }
}
