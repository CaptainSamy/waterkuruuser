package wssj.co.jp.point.screens.termofservice;

import wssj.co.jp.point.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 5/6/2017.
 */

public interface ITermOfServicesView extends IFragmentView {

    void onGetTermOfServiceSuccess(String html);

    void onGetTermOfServiceFailure(String message);
}
