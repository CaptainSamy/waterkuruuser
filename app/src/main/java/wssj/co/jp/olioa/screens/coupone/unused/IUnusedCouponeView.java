package wssj.co.jp.olioa.screens.coupone.unused;

import java.util.List;

import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.screens.base.IFragmentView;
import wssj.co.jp.olioa.model.coupone.Coupone;

/**
 * Created by thang on 1/2/2018.
 */

public interface IUnusedCouponeView extends IFragmentView {
    void showListCoupone(List<Coupone> list);

    void displayErrorMessage(ErrorMessage errorMessage);
}
