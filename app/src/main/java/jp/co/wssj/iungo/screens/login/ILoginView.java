package jp.co.wssj.iungo.screens.login;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

interface ILoginView extends IFragmentView {

    void showListMessageValidate(ErrorMessage errorMessage);

    void displayHomeScreen();

    void displayResetPasswordScreen();

    void displayChangePasswordScreen();

    void showLoginFailureMessage(String message);
}
