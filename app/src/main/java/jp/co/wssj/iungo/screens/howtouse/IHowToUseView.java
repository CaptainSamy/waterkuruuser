package jp.co.wssj.iungo.screens.howtouse;

import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public interface IHowToUseView extends IFragmentView {

    void onHowUseAppSuccess(String html);

    void onHowUseAppFailure(String message);
}
