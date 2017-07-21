package jp.co.wssj.iungo.screens.howtouse;

import jp.co.wssj.iungo.model.menu.MenuModel;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class HowToUsePresenter extends FragmentPresenter<IHowToUseView> {

    protected HowToUsePresenter(IHowToUseView view) {
        super(view);
        registerModel(new MenuModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
    }

    public void getHowUseApp() {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getView().showProgress();
        getModel(MenuModel.class).howUseApp(token, new MenuModel.IOnHowUseAppCallback() {

            @Override
            public void onHowUseAppSuccess(String html) {
                getView().hideProgress();
                getView().onHowUseAppSuccess(html);
            }

            @Override
            public void onHowUseAppFailure(String message) {
                getView().hideProgress();
                getView().onHowUseAppFailure(message);
            }
        });
    }
}
