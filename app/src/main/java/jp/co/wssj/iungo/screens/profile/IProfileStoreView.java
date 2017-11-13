package jp.co.wssj.iungo.screens.profile;

import jp.co.wssj.iungo.model.timeline.ProfileResponse;
import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 6/11/2017.
 */

public interface IProfileStoreView extends IFragmentView {
    void getProfileSuccess(ProfileResponse.Profile profile);

    void getProfileFailure(String message);
}
