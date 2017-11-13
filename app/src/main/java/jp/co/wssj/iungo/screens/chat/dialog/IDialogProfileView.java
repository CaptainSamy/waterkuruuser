package jp.co.wssj.iungo.screens.chat.dialog;

import jp.co.wssj.iungo.model.timeline.ProfileResponse;
import jp.co.wssj.iungo.screens.base.IDialogView;

/**
 * Created by Nguyen Huu Ta on 7/11/2017.
 */

public interface IDialogProfileView extends IDialogView {
    void getProfileSuccess(ProfileResponse.Profile profile);

    void getProfileFailure(String message);
}
