package wssj.co.jp.olioa.screens.contact;

import wssj.co.jp.olioa.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public interface IContactUsView extends IFragmentView {

    void getInfoUser(String userName, String email);

    void onFeedBackSuccess(String message);

    void onFeedBackFailure(String message);
}
