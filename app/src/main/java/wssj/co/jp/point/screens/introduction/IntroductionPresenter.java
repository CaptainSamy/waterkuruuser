package wssj.co.jp.point.screens.introduction;

import wssj.co.jp.point.screens.base.FragmentPresenter;

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
