package jp.co.wssj.iungo.screens.coupone.unused;

import java.util.List;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.coupone.Coupone;
import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by thang on 1/2/2018.
 */

public interface IUnusedCouponeView extends IFragmentView {
    void showListCoupone(List<Coupone> list);

    void displayErrorMessage(ErrorMessage errorMessage);
}
