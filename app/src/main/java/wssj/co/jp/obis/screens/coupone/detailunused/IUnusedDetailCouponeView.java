package wssj.co.jp.obis.screens.coupone.detailunused;

import wssj.co.jp.obis.model.ErrorMessage;
import wssj.co.jp.obis.screens.base.IFragmentView;

/**
 * Created by thang on 1/4/2018.
 */

public interface IUnusedDetailCouponeView extends IFragmentView{
    void useCoupone(Boolean kq);

    void displayErrorMessage(ErrorMessage errorMessage);
}
