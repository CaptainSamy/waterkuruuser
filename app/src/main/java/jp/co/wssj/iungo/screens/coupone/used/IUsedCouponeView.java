package jp.co.wssj.iungo.screens.coupone.used;

import java.util.List;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.coupone.Coupone;
import jp.co.wssj.iungo.screens.base.IFragmentView;
import jp.co.wssj.iungo.screens.coupone.ICouponeView;

/**
 * Created by thang on 1/2/2018.
 */

public interface IUsedCouponeView extends IFragmentView {
    void showListCoupone(List<Coupone> list);

    void displayErrorMessage(ErrorMessage errorMessage);
}
