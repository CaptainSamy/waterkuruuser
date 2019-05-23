package wssj.co.jp.obis.screens.qa;

import java.util.List;

import wssj.co.jp.obis.model.menu.MenuModel;
import wssj.co.jp.obis.model.menu.QAResponse;
import wssj.co.jp.obis.model.preference.SharedPreferencesModel;
import wssj.co.jp.obis.screens.base.FragmentPresenter;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class QAPresenter extends FragmentPresenter<IQAView> {

    protected QAPresenter(IQAView view) {
        super(view);
        registerModel(new MenuModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
    }

    public MenuModel getMenuModel() {
        return getModel(MenuModel.class);
    }

    public void getListQA(int page, int limit) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getView().showProgress();
        getMenuModel().getListQA(token, page, limit, new MenuModel.IOnGetListQACallback() {

            @Override
            public void onGetListQASuccess(int currentPage, int totalPage, List<QAResponse.ListQAData.QAData> data) {
                getView().hideProgress();
                getView().onGetListQASuccess(currentPage, totalPage, data);
            }

            @Override
            public void onGetListQAFailure(String message) {
                getView().hideProgress();
                getView().onGetListQAFailure(message);
            }
        });
    }
}
