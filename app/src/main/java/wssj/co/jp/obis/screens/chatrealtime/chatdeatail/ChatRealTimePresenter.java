package wssj.co.jp.obis.screens.chatrealtime.chatdeatail;

import wssj.co.jp.obis.model.ErrorMessage;
import wssj.co.jp.obis.model.preference.SharedPreferencesModel;
import wssj.co.jp.obis.screens.base.FragmentPresenter;
import wssj.co.jp.obis.model.chatrealtime.ChatRealTimeModel;

/**
 * Created by thang on 1/22/2018.
 */

public class ChatRealTimePresenter extends FragmentPresenter<IChatRealTimeView> {
    protected ChatRealTimePresenter(IChatRealTimeView view) {
        super(view);
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new ChatRealTimeModel(view.getViewContext()));
    }
    public void getUserProfile() {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(ChatRealTimeModel.class).GetUserProfile(token, new ChatRealTimeModel.IGetUserProfileResponeCallBack() {
            @Override
            public void onGetUserProfileResponseSuccess(long userProfileResponse) {
                getView().hideProgress();
                getView().getUserIdSucess(userProfileResponse);
            }
            @Override
            public void onGetUserProfileResponseFailure(ErrorMessage errorMessage) {
                getView().hideProgress();
                getView().getUserIdErro(errorMessage);
            }
        });

    }

}
