package jp.co.wssj.iungo.screens.coupone.detailunused;

import java.util.List;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.coupone.Coupone;
import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by thang on 1/4/2018.
 */

public interface IUnusedDetailCouponeView extends IFragmentView{
    void useCoupone(Boolean kq);

    void displayErrorMessage(ErrorMessage errorMessage);
}
