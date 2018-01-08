package jp.co.wssj.iungo.screens.coupone.detailunused;

import java.util.List;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.coupone.Coupone;
import jp.co.wssj.iungo.model.coupone.CouponeModel;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;
import jp.co.wssj.iungo.screens.coupone.unused.IUnusedCouponeView;

/**
 * Created by thang on 1/4/2018.
 */

public class UnusedDetailCouponePresenter extends FragmentPresenter<IUnusedDetailCouponeView> {


    protected UnusedDetailCouponePresenter(IUnusedDetailCouponeView view) {
        super(view);

        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new CouponeModel(view.getViewContext()));
    }

    public void useCoupon(int couponId, int isUseAgain) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(CouponeModel.class).useCoupone(token, couponId,isUseAgain,
                new CouponeModel.IUseCouponeResponseCallback() {
                    @Override
                    public void onUseResponseSuccess(Boolean kq) {
                        getView().useCoupone(kq);
                    }
                    @Override
                    public void onUseResponseFailure(ErrorMessage errorMessage) {
                        getView().displayErrorMessage(errorMessage);
                    }
                });
    }
}
