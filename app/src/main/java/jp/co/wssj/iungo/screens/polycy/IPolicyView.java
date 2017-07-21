package jp.co.wssj.iungo.screens.polycy;

import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public interface IPolicyView extends IFragmentView {

    void onPolicySuccess(String html);

    void onPolicyFailure(String message);
}
