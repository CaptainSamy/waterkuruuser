package wssj.co.jp.olioa.screens.registeraccount;

import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

interface IRegisterAccountView extends IFragmentView {

    void onValidateFailure(ErrorMessage errorMessage, int code);

    void onRegisterSuccess();


}
