package wssj.co.jp.olioa.screens.chat.dialog;

import wssj.co.jp.olioa.model.timeline.TimelineModel;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.model.timeline.ProfileResponse;
import wssj.co.jp.olioa.screens.base.BasePresenter;

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
