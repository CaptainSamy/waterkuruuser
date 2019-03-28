package wssj.co.jp.olioa.screens.liststorecheckedin;

import java.util.List;

import wssj.co.jp.olioa.model.baseapi.APICallback;
import wssj.co.jp.olioa.model.entities.StoreInfo;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.model.stamp.StampModel;
import wssj.co.jp.olioa.model.util.UtilsModel;
import wssj.co.jp.olioa.screens.base.FragmentPresenter;

/**
 * Created by Nguyen Huu Ta on 12/6/2017.
 */

public class ListStoreCheckedInPresenter extends FragmentPresenter<IListStoreCheckedInView> {

    protected ListStoreCheckedInPresenter(IListStoreCheckedInView view) {
        super(view);
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new StampModel(view.getViewContext()));
        registerModel(new UtilsModel(view.getViewContext()));
    }

    public void getListStoreCheckedIn(int type) {
        getView().showProgress();
        getModel(StampModel.class).getListStoreCheckedIn(type, 0, new APICallback<List<StoreInfo>>() {

            @Override
            public void onSuccess(List<StoreInfo> storeInfo) {
                getView().hideProgress();
                getView().onGetListStoreCheckedInSuccess(storeInfo);
            }

            @Override
            public void onFailure(String errorMessage) {
                getView().hideProgress();
                getView().showDialog(errorMessage);
            }
        });
    }
}
