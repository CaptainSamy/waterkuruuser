package wssj.co.jp.obis.screens.chatrealtime.chatdeatail;

import wssj.co.jp.obis.model.ErrorMessage;
import wssj.co.jp.obis.screens.base.IFragmentView;

/**
 * Created by thang on 1/22/2018.
 */

public interface IChatRealTimeView extends IFragmentView{
    void getUserIdSucess(long userId);
    void getUserIdErro(ErrorMessage errorMessage);
}
