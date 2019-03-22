package wssj.co.jp.olioa.screens.coupone.detailunused;

import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.screens.base.IFragmentView;

/**
 * Created by thang on 1/4/2018.
 */

public interface IUnusedDetailCouponeView extends IFragmentView{
    void useCoupone(Boolean kq);

    void displayErrorMessage(ErrorMessage errorMessage);
}
