package wssj.co.jp.obis.screens.switcher;

import wssj.co.jp.obis.screens.base.BaseFragment;
import wssj.co.jp.obis.screens.base.IFragmentView;

/**
 * Created by HieuPT on 10/17/2017.
 */

public interface ISwitcherView extends IFragmentView {

    void displayFragment(BaseFragment fragment);
}
