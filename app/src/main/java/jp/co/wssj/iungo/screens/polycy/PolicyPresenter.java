package jp.co.wssj.iungo.screens.polycy;

import jp.co.wssj.iungo.model.menu.MenuModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class PolicyPresenter extends FragmentPresenter<IPolicyView> {

    protected PolicyPresenter(IPolicyView view) {
        super(view);
        registerModel(new MenuModel(view.getViewContext()));
    }

    void getPolicy() {
        getView().showProgress();
        getModel(MenuModel.class).policy(new MenuModel.IOnPolicyCallback() {

            @Override
            public void onPolicySuccess(String html) {
                getView().hideProgress();
                getView().onPolicySuccess(html);
            }

            @Override
            public void onPolicyFailure(String message) {
                getView().hideProgress();
                getView().onPolicyFailure(message);
            }
        });
    }
}
