package wssj.co.jp.obis.screens.switcher;

import wssj.co.jp.obis.screens.base.FragmentPresenter;
import wssj.co.jp.obis.screens.base.IFragmentView;

/**
 * Created by HieuPT on 10/17/2017.
 */

public abstract class SwitcherPresenter<V extends IFragmentView> extends FragmentPresenter<V> {

    protected SwitcherPresenter(V view) {
        super(view);
    }
}
