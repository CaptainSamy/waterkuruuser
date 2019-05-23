package wssj.co.jp.obis.screens.login;

import wssj.co.jp.obis.model.ErrorMessage;
import wssj.co.jp.obis.screens.base.IFragmentView;

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
