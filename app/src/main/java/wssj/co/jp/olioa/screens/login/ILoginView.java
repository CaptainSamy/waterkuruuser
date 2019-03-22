package wssj.co.jp.olioa.screens.login;

import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.screens.base.IFragmentView;

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
