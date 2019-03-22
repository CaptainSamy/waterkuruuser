package wssj.co.jp.olioa.screens.chatrealtime.chatdeatail;

import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.screens.base.IFragmentView;

/**
 * Created by thang on 1/22/2018.
 */

public interface IChatRealTimeView extends IFragmentView{
    void getUserIdSucess(long userId);
    void getUserIdErro(ErrorMessage errorMessage);
}
