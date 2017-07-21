package jp.co.wssj.iungo.screens.basescreen;

import jp.co.wssj.iungo.screens.base.FragmentPresenter;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class BasePresenterScreen extends FragmentPresenter<IBaseView> {

    protected BasePresenterScreen(IBaseView view) {
        super(view);
    }
}
