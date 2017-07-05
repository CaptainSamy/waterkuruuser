package wssj.co.jp.point.screens.changepassword;

import wssj.co.jp.point.screens.base.IFragmentView;

/**
 * Created by HieuPT on 5/15/2017.
 */

interface IChangePasswordView extends IFragmentView {

    void onValidateFailure(String message);

    void onChangePasswordSuccess(String message);

    void onChangePasswordFailure(String message);
}
