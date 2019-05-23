package wssj.co.jp.obis.screens.profile;

import wssj.co.jp.obis.model.preference.SharedPreferencesModel;
import wssj.co.jp.obis.model.timeline.ProfileResponse;
import wssj.co.jp.obis.model.timeline.TimelineModel;
import wssj.co.jp.obis.screens.base.FragmentPresenter;

/**
 * Created by Nguyen Huu Ta on 6/11/2017.
 */

public class ProfileStorePresenter extends FragmentPresenter<IProfileStoreView> {

    protected ProfileStorePresenter(IProfileStoreView view) {
        super(view);
        registerModel(new TimelineModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
    }

    public void getProfileStore(int managerUserId) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getView().showProgress();
        getModel(TimelineModel.class).getProfileStore(token, managerUserId, new TimelineModel.IGetProfileCallback() {

            @Override
            public void getProfileSuccess(ProfileResponse.Profile profile) {
                getView().hideProgress();
                getView().getProfileSuccess(profile);
            }

            @Override
            public void getProfileFailure(String message) {
                getView().hideProgress();
                getView().getProfileFailure(message);
            }
        });
    }
}
