package wssj.co.jp.point.screens.dialograting;

import wssj.co.jp.point.screens.base.IDialogView;

/**
 * Created by Nguyen Huu Ta on 23/6/2017.
 */

public interface IDialogRatingView extends IDialogView {

    void reviewServiceSuccess(String message);

    void reviewServiceFailure(String message);
}
