package wssj.co.jp.olioa.screens.blockchat;

import java.util.List;

import wssj.co.jp.olioa.model.entities.BlockChatStore;
import wssj.co.jp.olioa.screens.base.IFragmentView;

public interface IBlockChatView extends IFragmentView {

    void showBlockList(List<BlockChatStore> list);

    void onBlockSuccess(String message);
}
