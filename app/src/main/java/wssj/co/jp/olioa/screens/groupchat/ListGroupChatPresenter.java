package wssj.co.jp.olioa.screens.groupchat;

import java.util.List;

import wssj.co.jp.olioa.model.baseapi.APICallback;
import wssj.co.jp.olioa.model.entities.GroupChat;
import wssj.co.jp.olioa.model.entities.StoreInfo;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.model.stamp.StampModel;
import wssj.co.jp.olioa.model.util.UtilsModel;
import wssj.co.jp.olioa.screens.base.FragmentPresenter;

/**
 * Created by Nguyen Huu Ta on 12/6/2017.
 */

public class ListGroupChatPresenter extends FragmentPresenter<IGroupChatView> {

    protected ListGroupChatPresenter(IGroupChatView view) {
        super(view);
        registerModel(new StampModel(view.getViewContext()));
        registerModel(new UtilsModel(view.getViewContext()));
    }

    private SharedPreferencesModel getShareModel() {
        return getModel(SharedPreferencesModel.class);
    }

    public void getGroupChat(boolean isShowProgress) {
        if (isShowProgress) {
            getView().showProgress();
        }
        getModel(StampModel.class).getListGroupChat(new APICallback<List<GroupChat>>() {
            @Override
            public void onSuccess(List<GroupChat> groupChats) {
                getView().hideProgress();
                getView().onGetListGroupSuccess(groupChats);
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

}
