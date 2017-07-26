package jp.co.wssj.iungo.screens.registeraccount;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.auth.RegisterData;
import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

interface IRegisterAccountView extends IFragmentView {

    void onValidateFailure(ErrorMessage errorMessage, int code);

    void onRegisterSuccess(RegisterData data, String message);

    void onRegisterFailure(ErrorMessage errorMessage);

}
