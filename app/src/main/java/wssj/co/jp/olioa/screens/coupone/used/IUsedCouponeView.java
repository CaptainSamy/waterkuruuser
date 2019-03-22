package wssj.co.jp.olioa.screens.coupone.used;

import java.util.List;

import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.screens.base.IFragmentView;
import wssj.co.jp.olioa.model.coupone.Coupone;

/**
 * Created by thang on 1/2/2018.
 */

public interface IUsedCouponeView extends IFragmentView {
    void showListCoupone(List<Coupone> list);

    void displayErrorMessage(ErrorMessage errorMessage);
}
