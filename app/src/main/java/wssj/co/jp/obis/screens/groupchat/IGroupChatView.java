package wssj.co.jp.obis.screens.groupchat;

import java.util.List;

import wssj.co.jp.obis.model.entities.GroupChat;
import wssj.co.jp.obis.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 12/6/2017.
 */

public interface IGroupChatView extends IFragmentView {

    void onGetListGroupSuccess(List<GroupChat> listGroup);

}
