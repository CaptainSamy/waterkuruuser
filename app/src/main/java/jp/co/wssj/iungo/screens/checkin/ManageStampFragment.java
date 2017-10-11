package jp.co.wssj.iungo.screens.checkin;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.stamp.ListCardResponse;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.PagedFragment;
import jp.co.wssj.iungo.screens.listcard.adapter.CardAdapter;
import jp.co.wssj.iungo.screens.note.UserMemoFragment;
import jp.co.wssj.iungo.screens.primary.PrimaryFragment;
import jp.co.wssj.iungo.screens.waitstoreconfirm.WaitStoreConfirmFragment;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;
import jp.co.wssj.iungo.widget.MarginItemDecoration;

/**
 * Created by Nguyen Huu Ta on 23/5/2017.
 */

public class ManageStampFragment extends PagedFragment<IManageStampView, ManageStampPresenter> implements IManageStampView {

    private static final String TAG = "ManageStampFragment";

    private RecyclerView mUnusedCardRecycler, mCanUseCardRecycler;

    private CardAdapter mUnusedCardAdapter, mCanUseCardAdapter;

    private TextView mTextStoreName;

    private TextView mExplainStamp;

    private View mEditMemoView;

    private View mUnusedContainer, mCanUseContainer;

    private int mCountOnResume = 0;

    private String mStoreName, mServiceName;

    public static ManageStampFragment newInstance(Bundle args) {
        ManageStampFragment fragment = new ManageStampFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_manage_stamp;
    }

    @Override
    protected ManageStampPresenter onCreatePresenter(IManageStampView view) {
        return new ManageStampPresenter(view);
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_screen_manager_stamp);
    }

    @Override
    protected IManageStampView onCreateView() {
        return this;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected void initViews(View rootView) {
        mEditMemoView = rootView.findViewById(R.id.edit_memo_image_view);
        mUnusedContainer = rootView.findViewById(R.id.unused_card_container);
        mCanUseContainer = rootView.findViewById(R.id.can_use_card_container);
        mTextStoreName = (TextView) rootView.findViewById(R.id.tvCardName);

        mExplainStamp = (TextView) rootView.findViewById(R.id.textExplainStamp);

        mUnusedCardRecycler = (RecyclerView) rootView.findViewById(R.id.unused_card_recycler_view);
        mCanUseCardRecycler = (RecyclerView) rootView.findViewById(R.id.can_use_card_recycler_view);

        mUnusedCardRecycler.setLayoutManager(new LinearLayoutManager(getActivityContext(), LinearLayoutManager.VERTICAL, true));
        mCanUseCardRecycler.setLayoutManager(new LinearLayoutManager(getActivityContext(), LinearLayoutManager.VERTICAL, true));

        mUnusedCardRecycler.setNestedScrollingEnabled(false);
        mCanUseCardRecycler.setNestedScrollingEnabled(false);

        mUnusedCardRecycler.setHasFixedSize(true);
        mCanUseCardRecycler.setHasFixedSize(true);

        mUnusedCardRecycler.addItemDecoration(new MarginItemDecoration(getResources().getDimensionPixelSize(R.dimen.card_divider_dimen)));
        mCanUseCardRecycler.addItemDecoration(new MarginItemDecoration(getResources().getDimensionPixelSize(R.dimen.card_divider_dimen)));
    }

    @Override
    protected void initAction() {
        mEditMemoView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getPresenter().onEditServiceMemoClicked();
            }
        });
    }

    @Override
    protected void initData() {

        mListCardCanUse = new ArrayList<>();
        mCanUseCardAdapter = new CardAdapter(mListCardCanUse);
        mCanUseCardRecycler.setAdapter(mCanUseCardAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mStoreName = bundle.getString(WaitStoreConfirmFragment.KEY_STORE_NAME);
            mServiceName = bundle.getString(WaitStoreConfirmFragment.KEY_SERVICE_NAME);
        }
        getPresenter().getListCardByServiceCompany(Constants.INIT_PAGE, Constants.LIMIT);
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.d(TAG, "#onResume " + mCountOnResume);
        getPresenter().getCheckInStatus();
    }

    @Override
    public void showListUnusedCard(List<ListCardResponse.ListCardData.CardData> cardDataList, String explain) {
        mUnusedContainer.setVisibility(View.VISIBLE);
        if (cardDataList != null && cardDataList.size() > 0) {
            ListCardResponse.ListCardData.CardData data = cardDataList.get(0);
            if (data != null && data.getStamps() != null && data.getStamps().size() != 0) {
                ListCardResponse.ListCardData.CardData.StampData stampData = data.getStamps().get(0);
                if (stampData != null) {
                    mStoreName = stampData.getStoreName();
                    mTextStoreName.setText(mStoreName);
                } else {
                    mTextStoreName.setVisibility(View.INVISIBLE);
                }
            }
        }
        if (mUnusedCardAdapter == null) {
            mUnusedCardAdapter = new CardAdapter(cardDataList);
            mUnusedCardRecycler.setAdapter(mUnusedCardAdapter);
        } else {
            if (mUnusedCardRecycler.getAdapter() == null) {
                mUnusedCardRecycler.setAdapter(mUnusedCardAdapter);
            }
            mUnusedCardAdapter.refreshData(cardDataList);
        }
        showExplainStamp(explain);
    }

    @Override
    public void showListUnusedCardEmpty(int numberStampInCard, String explain) {
        if (numberStampInCard != 0) {
            mUnusedContainer.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mStoreName)) {
                mTextStoreName.setText(mStoreName);
            } else {
                mTextStoreName.setVisibility(View.GONE);
            }
            if (mUnusedCardAdapter == null) {
                mUnusedCardAdapter = new CardAdapter(null);
                mUnusedCardRecycler.setAdapter(mUnusedCardAdapter);
            } else {
                if (mUnusedCardRecycler.getAdapter() == null) {
                    mUnusedCardRecycler.setAdapter(mUnusedCardAdapter);
                }
            }
            mUnusedCardAdapter.setNumberStampInCard(numberStampInCard);
        } else {
            mUnusedContainer.setVisibility(View.GONE);
        }
        showExplainStamp(explain);
    }

    private void showExplainStamp(String explain) {
        if (!TextUtils.isEmpty(explain)) {
            mExplainStamp.setVisibility(View.VISIBLE);
            mExplainStamp.setText(Html.fromHtml(explain.trim()));
        } else {
            mExplainStamp.setVisibility(View.GONE);
        }
    }

    private List<ListCardResponse.ListCardData.CardData> mListCardCanUse;

    @Override
    public void showListCanUseCard(List<ListCardResponse.ListCardData.CardData> cardDataList, final int page, final int totalPage) {
        mCanUseContainer.setVisibility(View.VISIBLE);
        mListCardCanUse.addAll(cardDataList);
        mCanUseCardAdapter.notifyDataSetChanged();
        mCanUseCardAdapter.setOnEndOfRecycleView(new CardAdapter.IEndOfRecycleView() {

            @Override
            public void onEndOfRecycleView() {
                Logger.d(TAG, "#onEndOfRecycleView");
                if (page < totalPage) {
                    getPresenter().getListCardByServiceCompany(page + 1, Constants.LIMIT);
                }
            }
        });
//            if (mCanUseCardRecycler.getAdapter() == null) {
//                mCanUseCardRecycler.setAdapter(mCanUseCardAdapter);
//            }
//            mCanUseCardAdapter.refreshData(cardDataList);
    }

    @Override
    public void hideListCanUseCard() {
        mCanUseContainer.setVisibility(View.GONE);
    }

    @Override
    public void displayEditServiceMemoScreen(int serviceId) {
        Bundle bundle = new Bundle();
        bundle.putInt(UserMemoFragment.KEY_SERVICE_ID, serviceId);
        String serviceName = mTextStoreName.getText().toString();
        if (!TextUtils.isEmpty(serviceName)) {
            bundle.putString(UserMemoFragment.KEY_SERVICE_NAME, mServiceName);
        }
        getActivityCallback().displayScreen(IMainView.FRAGMENT_USER_MEMO, true, true, bundle);
    }

    @Override
    public void displayMyStampScreen() {
        getPagerFragmentCallback().setSelectedPage(PrimaryFragment.SCREEN_LIST_SERVICE_COMPANY);
    }
}
