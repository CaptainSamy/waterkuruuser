package jp.co.wssj.iungo.screens.switcher;

import jp.co.wssj.iungo.screens.base.FragmentPresenter;
import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by HieuPT on 10/17/2017.
 */

public abstract class SwitcherPresenter<V extends IFragmentView> extends FragmentPresenter<V> {

    protected SwitcherPresenter(V view) {
        super(view);
    }
}
