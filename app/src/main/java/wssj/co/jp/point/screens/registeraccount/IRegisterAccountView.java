package wssj.co.jp.point.screens.registeraccount;

import wssj.co.jp.point.model.ErrorMessage;
import wssj.co.jp.point.model.auth.RegisterData;
import wssj.co.jp.point.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

interface IRegisterAccountView extends IFragmentView {

    void onValidateFailure(ErrorMessage errorMessage, int code);

    void onRegisterSuccess(RegisterData data);

    void onRegisterFailure(ErrorMessage errorMessage);

}
