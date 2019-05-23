package wssj.co.jp.obis.screens.termofservice;

import wssj.co.jp.obis.model.menu.MenuModel;
import wssj.co.jp.obis.screens.base.FragmentPresenter;

/**
 * Created by Nguyen Huu Ta on 5/6/2017.
 */

public class TermOfServicePresenter extends FragmentPresenter<ITermOfServicesView> {

    public TermOfServicePresenter(ITermOfServicesView view) {
        super(view);
        registerModel(new MenuModel(view.getViewContext()));
    }

    public void getContentTermOfService() {
        getView().showProgress();
        getModel(MenuModel.class).termOfService(new MenuModel.IOnGetTermOfService() {

            @Override
            public void onGetTermOfServiceSuccess(String html) {
                getView().hideProgress();
                getView().onGetTermOfServiceSuccess(html);
            }

            @Override
            public void onGetTermOfServiceFailure(String message) {
                getView().hideProgress();
                getView().onGetTermOfServiceFailure(message);
            }
        });
    }
}
