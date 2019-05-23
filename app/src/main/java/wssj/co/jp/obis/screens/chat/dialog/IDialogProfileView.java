package wssj.co.jp.obis.screens.chat.dialog;

import wssj.co.jp.obis.model.timeline.ProfileResponse;
import wssj.co.jp.obis.screens.base.IDialogView;

/**
 * Created by Nguyen Huu Ta on 7/11/2017.
 */

public interface IDialogProfileView extends IDialogView {
    void getProfileSuccess(ProfileResponse.Profile profile);

    void getProfileFailure(String message);
}
