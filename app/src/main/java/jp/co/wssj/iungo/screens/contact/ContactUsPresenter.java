package jp.co.wssj.iungo.screens.contact;

import jp.co.wssj.iungo.model.menu.MenuModel;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class ContactUsPresenter extends FragmentPresenter<IContactUsView> {

    protected ContactUsPresenter(IContactUsView view) {
        super(view);
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new MenuModel(view.getViewContext()));
    }

    public void getInfoUser() {
        String userName = getModel(SharedPreferencesModel.class).getUserName();
        String email = getModel(SharedPreferencesModel.class).getEmail();
        getView().getInfoUser(userName, email);
    }

    public void feedback(String feedback) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getView().showProgress();
        getModel(MenuModel.class).feedback(token, feedback, new MenuModel.IOnFeedBackCallback() {

            @Override
            public void onFeedBackSuccess(String message) {
                getView().hideProgress();
                getView().onFeedBackSuccess(message);
            }

            @Override
            public void onFeedBackFailure(String message) {
                getView().hideProgress();
                getView().onFeedBackFailure(message);
            }
        });
    }
}
