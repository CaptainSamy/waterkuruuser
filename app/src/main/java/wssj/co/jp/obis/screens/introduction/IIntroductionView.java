package wssj.co.jp.obis.screens.introduction;

import wssj.co.jp.obis.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

interface IIntroductionView extends IFragmentView {

    void displayLoginScreen();

    void displayRegisterScreen();

    void displayHomeScreen();
}
