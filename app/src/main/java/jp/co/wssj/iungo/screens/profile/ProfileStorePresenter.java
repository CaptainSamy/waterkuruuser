package jp.co.wssj.iungo.screens.profile;

import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.model.timeline.ProfileResponse;
import jp.co.wssj.iungo.model.timeline.TimelineModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;

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
