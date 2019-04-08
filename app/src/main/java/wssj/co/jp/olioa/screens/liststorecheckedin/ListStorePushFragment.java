package wssj.co.jp.olioa.screens.liststorecheckedin;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.entities.StoreInfo;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;
import wssj.co.jp.olioa.screens.liststorecheckedin.adapter.ListStoreCheckedInAdapter;

/**
 * Created by Nguyen Huu Ta on 12/6/2017.
 */

public class ListStorePushFragment extends BaseFragment<IListStoreCheckedInView, ListStoreCheckedInPresenter> implements IListStoreCheckedInView, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "ListStorePushFragment";

    public static final String ARG_STORE_INFO = "storeInfo";

    private static final int TYPE = 1;

    private SwipeRefreshLayout mRefreshLayout;

    private ListView mListStoreCheckedIn;

    private ListStoreCheckedInAdapter mAdapter;

    public static ListStorePushFragment newInstance(Bundle args) {
        ListStorePushFragment fragment = new ListStorePushFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_bottom_stamp);
    }

    @Override
    public boolean isDisplayBackButton() {
        return false;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_LIST_STORE_PUSH;
    }

    @Override
    public int getNavigationBottomId() {
        return R.id.navigation_push;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_list_store_checked_in;
    }

    @Override
    protected ListStoreCheckedInPresenter onCreatePresenter(IListStoreCheckedInView view) {
        return new ListStoreCheckedInPresenter(view);
    }

    @Override
    protected IListStoreCheckedInView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        mRefreshLayout = view.findViewById(R.id.refreshLayout);
        mListStoreCheckedIn = view.findViewById(R.id.lvListStoreCheckedIn);
    }

    @Override
    protected void initAction() {
        mRefreshLayout.setOnRefreshListener(this);
        mListStoreCheckedIn.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StoreInfo storeInfo = (StoreInfo) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable(ARG_STORE_INFO, storeInfo);
                getActivityCallback().displayScreen(IMainView.FRAGMENT_PUSH_NOTIFICATION, true, true, bundle);

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mAdapter = new ListStoreCheckedInAdapter(getActivityContext(), new ArrayList<StoreInfo>());
        mListStoreCheckedIn.setAdapter(mAdapter);
        getPresenter().getListStoreCheckedIn(TYPE);
    }

    @Override
    protected void onRefreshWhenBackFragment() {
        super.onRefreshWhenBackFragment();
        getPresenter().getListStoreCheckedIn(TYPE);
    }

    @Override
    public void onGetListStoreCheckedInSuccess(List<StoreInfo> listStores) {
        mAdapter.setListStore(listStores);
    }

    @Override
    public void onGetListStoreCheckedInFailure(String message) {
        showToast(message);
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(false);
        getPresenter().getListStoreCheckedIn(TYPE);
    }
}
