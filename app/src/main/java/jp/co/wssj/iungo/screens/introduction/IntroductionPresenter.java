package jp.co.wssj.iungo.screens.introduction;

import jp.co.wssj.iungo.screens.base.FragmentPresenter;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

class IntroductionPresenter extends FragmentPresenter<IIntroductionView> {

    IntroductionPresenter(IIntroductionView view) {
        super(view);
    }

    void onLoginButtonClicked() {
        getView().displayLoginScreen();
    }

    void onRegisterButtonClicked() {
        getView().displayRegisterScreen();
    }
}
