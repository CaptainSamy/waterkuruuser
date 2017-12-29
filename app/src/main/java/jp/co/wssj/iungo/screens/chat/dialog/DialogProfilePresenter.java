package jp.co.wssj.iungo.screens.chat.dialog;

import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.model.timeline.ProfileResponse;
import jp.co.wssj.iungo.model.timeline.TimelineModel;
import jp.co.wssj.iungo.screens.base.BasePresenter;

/**
 * Created by Nguyen Huu Ta on 7/11/2017.
 */

public class DialogProfilePresenter extends BasePresenter<IDialogProfileView> {

    protected DialogProfilePresenter(IDialogProfileView view) {
        super(view);
        registerModel(new TimelineModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
    }

    public void getProfileStore(int managerUserId) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(TimelineModel.class).getProfileStore(token, managerUserId, new TimelineModel.IGetProfileCallback() {

            @Override
            public void getProfileSuccess(ProfileResponse.Profile profile) {
                getView().getProfileSuccess(profile);
            }

            @Override
            public void getProfileFailure(String message) {
                getView().getProfileFailure(message);
            }
        });
    }

}
