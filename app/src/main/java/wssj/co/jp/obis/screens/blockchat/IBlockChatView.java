package wssj.co.jp.obis.screens.blockchat;

import java.util.List;

import wssj.co.jp.obis.model.entities.BlockChatStore;
import wssj.co.jp.obis.screens.base.IFragmentView;

public interface IBlockChatView extends IFragmentView {

    void showBlockList(List<BlockChatStore> list);

    void onBlockSuccess(String message);
}
