package wssj.co.jp.point.screens.login;

import wssj.co.jp.point.model.ErrorMessage;
import wssj.co.jp.point.screens.base.IFragmentView;

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
