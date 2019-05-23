package wssj.co.jp.obis.screens.qa;

import java.util.List;

import wssj.co.jp.obis.model.menu.QAResponse;
import wssj.co.jp.obis.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public interface IQAView extends IFragmentView {

    void onGetListQASuccess(int currentPage, int totalPage, List<QAResponse.ListQAData.QAData> data);

    void onGetListQAFailure(String message);
}
