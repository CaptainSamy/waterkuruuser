package jp.co.wssj.iungo.screens.chatrealtime.chatdeatail;

import java.util.List;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by thang on 1/22/2018.
 */

public interface IChatRealTimeView extends IFragmentView{
    void getUserIdSucess(long userId);
    void getUserIdErro(ErrorMessage errorMessage);
}
