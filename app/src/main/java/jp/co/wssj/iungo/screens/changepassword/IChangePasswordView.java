package jp.co.wssj.iungo.screens.changepassword;

import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by HieuPT on 5/15/2017.
 */

interface IChangePasswordView extends IFragmentView {

    void onValidateFailure(String message);

    void onChangePasswordSuccess(String message);

    void onChangePasswordFailure(String message);
}
