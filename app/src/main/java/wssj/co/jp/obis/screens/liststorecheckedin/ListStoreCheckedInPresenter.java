package wssj.co.jp.obis.screens.liststorecheckedin;

import java.util.List;

import wssj.co.jp.obis.model.baseapi.APICallback;
import wssj.co.jp.obis.model.entities.StoreInfo;
import wssj.co.jp.obis.model.preference.SharedPreferencesModel;
import wssj.co.jp.obis.model.stamp.StampModel;
import wssj.co.jp.obis.model.util.UtilsModel;
import wssj.co.jp.obis.screens.base.FragmentPresenter;

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

    private SharedPreferencesModel getShareModel() {
        return getModel(SharedPreferencesModel.class);
    }

    public void getListStoreCheckedIn(int type, boolean... isShowProgress) {
        if (isShowProgress.length == 0) {
            getView().showProgress();
        }
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

    void saveLastTimeReadChat(long storeId) {
        getShareModel().putLastTimeReadChat(storeId, System.currentTimeMillis());
    }

    long getLasTimeRead(long storeId) {
        return getShareModel().getLastTimeReadChat(storeId);
    }
}
