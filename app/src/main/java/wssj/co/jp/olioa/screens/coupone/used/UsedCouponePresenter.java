package wssj.co.jp.olioa.screens.coupone.used;

import java.util.List;

import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.screens.base.FragmentPresenter;
import wssj.co.jp.olioa.model.coupone.Coupone;
import wssj.co.jp.olioa.model.coupone.CouponeModel;

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
