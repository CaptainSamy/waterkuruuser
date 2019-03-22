package wssj.co.jp.olioa.screens.profile;

import wssj.co.jp.olioa.model.timeline.ProfileResponse;
import wssj.co.jp.olioa.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 6/11/2017.
 */

public interface IProfileStoreView extends IFragmentView {
    void getProfileSuccess(ProfileResponse.Profile profile);

    void getProfileFailure(String message);
}
