package wssj.co.jp.olioa.screens.chat.dialog;

import wssj.co.jp.olioa.model.timeline.ProfileResponse;
import wssj.co.jp.olioa.screens.base.IDialogView;

/**
 * Created by Nguyen Huu Ta on 7/11/2017.
 */

public interface IDialogProfileView extends IDialogView {
    void getProfileSuccess(ProfileResponse.Profile profile);

    void getProfileFailure(String message);
}
