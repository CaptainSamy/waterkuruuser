package jp.co.wssj.iungo.screens.qa;

import java.util.List;

import jp.co.wssj.iungo.model.menu.MenuModel;
import jp.co.wssj.iungo.model.menu.QAResponse;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;

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
