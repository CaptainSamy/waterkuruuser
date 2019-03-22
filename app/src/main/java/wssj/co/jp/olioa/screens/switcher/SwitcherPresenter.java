package wssj.co.jp.olioa.screens.switcher;

import wssj.co.jp.olioa.screens.base.FragmentPresenter;
import wssj.co.jp.olioa.screens.base.IFragmentView;

/**
 * Created by HieuPT on 10/17/2017.
 */

public abstract class SwitcherPresenter<V extends IFragmentView> extends FragmentPresenter<V> {

    protected SwitcherPresenter(V view) {
        super(view);
    }
}
