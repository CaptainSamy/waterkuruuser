package wssj.co.jp.point.screens.polycy;

import wssj.co.jp.point.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public interface IPolicyView extends IFragmentView {

    void onPolicySuccess(String html);

    void onPolicyFailure(String message);
}
