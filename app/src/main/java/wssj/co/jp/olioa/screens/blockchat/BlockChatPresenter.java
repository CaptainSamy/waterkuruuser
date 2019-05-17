package wssj.co.jp.olioa.screens.blockchat;

import java.util.List;

import wssj.co.jp.olioa.model.baseapi.APICallback;
import wssj.co.jp.olioa.model.chat.ChatModel;
import wssj.co.jp.olioa.model.entities.BlockChatStore;
import wssj.co.jp.olioa.screens.base.FragmentPresenter;

class BlockChatPresenter extends FragmentPresenter<IBlockChatView> {


    BlockChatPresenter(IBlockChatView view) {
        super(view);
        registerModel(new ChatModel(view.getViewContext()));
    }


    void getGroupChat() {
        getView().showProgress();
        getModel(ChatModel.class).getListStoreBlock(new APICallback<List<BlockChatStore>>() {
            @Override
            public void onSuccess(List<BlockChatStore> blockChatStores) {
                getView().hideProgress();
                getView().showBlockList(blockChatStores);
            }

            @Override
            public void onFailure(String errorMessage) {
                getView().hideProgress();
                getView().showDialog(errorMessage);
            }
        });
    }


    void changeStatusGroup(final BlockChatStore params) {
        getView().showProgress();
        getModel(ChatModel.class).blockStore(params, new APICallback() {
            @Override
            public void onSuccess(Object o) {
              String message = params.getStatus() == 2 ?  "ブロックできました。" : "解除できました。";
              getView().hideProgress();
              getView().showDialog(message);

            }

            @Override
            public void onFailure(String errorMessage) {
                getView().hideProgress();
                getView().showDialog(errorMessage);
            }
        });
    }
}
