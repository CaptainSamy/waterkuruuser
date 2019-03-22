package wssj.co.jp.olioa.screens.coupone.detailunused;

import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.screens.base.FragmentPresenter;
import wssj.co.jp.olioa.model.coupone.CouponeModel;

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
