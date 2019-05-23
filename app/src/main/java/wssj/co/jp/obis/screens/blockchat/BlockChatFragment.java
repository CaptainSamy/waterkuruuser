package wssj.co.jp.obis.screens.blockchat;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wssj.co.jp.obis.R;
import wssj.co.jp.obis.model.baseapi.IOlioaCallback;
import wssj.co.jp.obis.model.entities.BlockChatStore;
import wssj.co.jp.obis.screens.IMainView;
import wssj.co.jp.obis.screens.base.BaseFragment;
import wssj.co.jp.obis.screens.blockchat.adapter.BlockChatAdapter;
import wssj.co.jp.obis.screens.dialogerror.DialogMessage;

public class BlockChatFragment extends BaseFragment<IBlockChatView, BlockChatPresenter> implements IBlockChatView, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = "BlockChatFragment";

    private SwipeRefreshLayout mRefreshLayout;
    private ListView mListViewBlock;
    private TextView mTextNoItem;

    private BlockChatAdapter mAdapter;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_BLOCK_CHAT;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_list_store_checked_in;
    }

    @Override
    protected BlockChatPresenter onCreatePresenter(IBlockChatView view) {
        return new BlockChatPresenter(view);
    }

    @Override
    protected IBlockChatView onCreateView() {
        return this;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.block_chat);
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        mRefreshLayout = view.findViewById(R.id.refreshLayout);
        mListViewBlock = view.findViewById(R.id.lvListStoreCheckedIn);
        mTextNoItem = view.findViewById(R.id.emptyView);
    }

    @Override
    protected void initAction() {
        super.initAction();
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mAdapter = new BlockChatAdapter(getActivityContext(), new ArrayList<BlockChatStore>(), new IOlioaCallback<BlockChatStore>() {
            @Override
            public void onAction(int type, BlockChatStore... data) {
                if (data.length == 0) return;
                final BlockChatStore store = data[0];
                int newStatus = store.getStatus() == 1 ? 2 : 1;
                print("newStatus " + newStatus);
                store.setStatus(newStatus);
                if (store.getStatus() == 2) {
                     DialogMessage mDialog = new DialogMessage(getActivityContext(), new DialogMessage.IOnClickListener() {
                        @Override
                        public void buttonYesClick() {
                            getPresenter().changeStatusGroup(store);
                        }

                        @Override
                        public void buttonCancelClick() {
                            store.setStatus(1);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                    mDialog.initData(store.getMessage(), getString(R.string.OK), getString(R.string.CANCEL));
                    mDialog.show();
                } else {
                    getPresenter().changeStatusGroup(store);
                }


            }
        });
        mListViewBlock.setAdapter(mAdapter);
        getPresenter().getGroupChat();
    }


    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(false);
        getPresenter().getGroupChat();
    }

    @Override
    public void showBlockList(List<BlockChatStore> list) {
        mAdapter.updateList(list);
        mTextNoItem.setVisibility(mAdapter.getCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onBlockSuccess(String message) {

    }
}
