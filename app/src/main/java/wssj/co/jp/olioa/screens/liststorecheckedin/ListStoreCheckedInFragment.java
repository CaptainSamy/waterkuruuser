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
import wssj.co.jp.olioa.screens.pushnotification.pushlist.PushNotificationFragment;

/**
 * Created by Nguyen Huu Ta on 12/6/2017.
 */

public class ListStoreCheckedInFragment extends BaseFragment<IListStoreCheckedInView, ListStoreCheckedInPresenter> implements IListStoreCheckedInView, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "ListStoreCheckedInFragment";

    SwipeRefreshLayout mRefreshLayout;

    private ListView mListStoreCheckedIn;

    private ListStoreCheckedInAdapter mAdapter;

    public static ListStoreCheckedInFragment newInstance(Bundle args) {
        ListStoreCheckedInFragment fragment = new ListStoreCheckedInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_screen_list_store_checked_in);
    }

    @Override
    public boolean isDisplayBackButton() {
        return false;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_LIST_STORE_CHECKED_IN;
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
        mListStoreCheckedIn = (ListView) view.findViewById(R.id.lvListStoreCheckedIn);
    }

    @Override
    protected void initAction() {
        mRefreshLayout.setOnRefreshListener(this);
        mListStoreCheckedIn.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StoreInfo storeInfo = (StoreInfo) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putInt(PushNotificationFragment.ARG_STORE_ID, storeInfo.getId());
                getActivityCallback().displayScreen(IMainView.FRAGMENT_PUSH_NOTIFICATION, true, true, bundle);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mAdapter = new ListStoreCheckedInAdapter(getActivityContext(), new ArrayList<StoreInfo>());
        mListStoreCheckedIn.setAdapter(mAdapter);
        getPresenter().getListStoreCheckedIn();
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
        getPresenter().getListStoreCheckedIn();
    }
}
