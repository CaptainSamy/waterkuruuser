package jp.co.wssj.iungo.screens.resetpassword;

import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

interface IResetPasswordView extends IFragmentView {

    void onResetSuccess(String message);

    void onResetFailure(String message);

    void onValidateFailure(String message);
}
