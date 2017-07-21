package jp.co.wssj.iungo.screens.contact;

import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public interface IContactUsView extends IFragmentView {

    void getInfoUser(String userName, String email);

    void onFeedBackSuccess(String message);

    void onFeedBackFailure(String message);
}
