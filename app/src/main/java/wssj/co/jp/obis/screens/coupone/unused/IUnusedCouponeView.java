package wssj.co.jp.obis.screens.coupone.unused;

import java.util.List;

import wssj.co.jp.obis.model.ErrorMessage;
import wssj.co.jp.obis.screens.base.IFragmentView;
import wssj.co.jp.obis.model.coupone.Coupone;

/**
 * Created by thang on 1/2/2018.
 */

public interface IUnusedCouponeView extends IFragmentView {
    void showListCoupone(List<Coupone> list);

    void displayErrorMessage(ErrorMessage errorMessage);
}
