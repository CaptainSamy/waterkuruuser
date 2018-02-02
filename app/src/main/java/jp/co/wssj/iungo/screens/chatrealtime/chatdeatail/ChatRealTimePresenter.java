package jp.co.wssj.iungo.screens.chatrealtime.chatdeatail;

import java.util.List;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.chatrealtime.ChatRealTimeModel;
import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.model.pushnotification.PushNotificationModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;
import jp.co.wssj.iungo.utils.Constants;

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
                getView().getUserIdSucess(userProfileResponse);
            }
            @Override
            public void onGetUserProfileResponseFailure(ErrorMessage errorMessage) {
                getView().getUserIdErro(errorMessage);
            }
        });

    }

}
