package wssj.co.jp.olioa.screens.howtouse;

import wssj.co.jp.olioa.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public interface IHowToUseView extends IFragmentView {

    void onHowUseAppSuccess(String html);

    void onHowUseAppFailure(String message);
}
