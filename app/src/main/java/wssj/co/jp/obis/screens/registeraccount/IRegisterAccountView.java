package wssj.co.jp.obis.screens.registeraccount;

import wssj.co.jp.obis.model.ErrorMessage;
import wssj.co.jp.obis.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

interface IRegisterAccountView extends IFragmentView {

    void onValidateFailure(ErrorMessage errorMessage, int code);

    void onRegisterSuccess();


}
