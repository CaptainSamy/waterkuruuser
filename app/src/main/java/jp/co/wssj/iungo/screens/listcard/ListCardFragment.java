package jp.co.wssj.iungo.screens.listcard;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.stamp.ListCardResponse;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.listcard.adapter.CardAdapter;
import jp.co.wssj.iungo.screens.note.UserMemoFragment;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.widget.MarginItemDecoration;

/**
 * Created by HieuPT on 6/1/2017.
 */

public class ListCardFragment extends BaseFragment<IListCardView, ListCardPresenter> implements IListCardView {

    public static final String KEY_CARD_NAME = "KEY_CARD_NAME";

    public static final String KEY_SERVICE_ID = "KEY_SERVICE_ID";

    private static final String TAG = "ListCardFragment";

    private RecyclerView mUnusedCardRecycler, mCanUseCardRecycler, mUsedCardRecycler;

    private CardAdapter mUnusedCardAdapter, mCanUseCardAdapter, mUsedCardAdapter;

    private TextView mEditMemoTextView, mTextCardName;

    private TextView mButtonListStore;

    private TextView mTitleExplainStamp, mExplainStamp;

    private View mUnusedContainer, mCanUseContainer, mUsedContainer;

    private String mServiceName;

    private LinearLayout mLayoutButton;

    private List<ListCardResponse.ListCardData.CardData> mListCardCanUse, mListCardUsed;

    public static ListCardFragment newInstance(Bundle bundle) {
        ListCardFragment fragment = new ListCardFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_LIST_CARD;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_screen_stamp);
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return true;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_list_card;
    }

    @Override
    protected void initViews(View rootView) {
        mLayoutButton = (LinearLayout) rootView.findViewById(R.id.layoutButton);
        mEditMemoTextView = (TextView) rootView.findViewById(R.id.edit_memo_text_view);
        mButtonListStore = (TextView) rootView.findViewById(R.id.tvListStoresCheckedIn);
        mTextCardName = (TextView) rootView.findViewById(R.id.tvCardName);

        mTitleExplainStamp = (TextView) rootView.findViewById(R.id.titleExplainStamp);
        mExplainStamp = (TextView) rootView.findViewById(R.id.textExplainStamp);
        mUnusedContainer = rootView.findViewById(R.id.unused_card_container);
        mCanUseContainer = rootView.findViewById(R.id.can_use_card_container);
        mUsedContainer = rootView.findViewById(R.id.used_card_container);

        mUnusedCardRecycler = (RecyclerView) rootView.findViewById(R.id.unused_card_recycler_view);
        mCanUseCardRecycler = (RecyclerView) rootView.findViewById(R.id.can_use_card_recycler_view);
        mUsedCardRecycler = (RecyclerView) rootView.findViewById(R.id.used_card_recycler_view);

        mUnusedCardRecycler.setLayoutManager(new LinearLayoutManager(getActivityContext(), LinearLayoutManager.VERTICAL, true));
        mCanUseCardRecycler.setLayoutManager(new LinearLayoutManager(getActivityContext(), LinearLayoutManager.VERTICAL, true));
        mUsedCardRecycler.setLayoutManager(new LinearLayoutManager(getActivityContext(), LinearLayoutManager.VERTICAL, true));

        mUnusedCardRecycler.setNestedScrollingEnabled(false);
        mCanUseCardRecycler.setNestedScrollingEnabled(false);
        mUsedCardRecycler.setNestedScrollingEnabled(false);

        mUnusedCardRecycler.setHasFixedSize(true);
        mCanUseCardRecycler.setHasFixedSize(true);
        mUsedCardRecycler.setHasFixedSize(true);

        mUnusedCardRecycler.addItemDecoration(new MarginItemDecoration(getResources().getDimensionPixelSize(R.dimen.card_divider_dimen)));
        mCanUseCardRecycler.addItemDecoration(new MarginItemDecoration(getResources().getDimensionPixelSize(R.dimen.card_divider_dimen)));
        mUsedCardRecycler.addItemDecoration(new MarginItemDecoration(getResources().getDimensionPixelSize(R.dimen.card_divider_dimen)));
    }

    private int mServiceCompanyId;

    @Override
    protected void initAction() {
        super.initAction();
    }

    @Override
    protected void initData() {
        mListCardCanUse = new ArrayList<>();
        mCanUseCardAdapter = new CardAdapter(mListCardCanUse);
        mCanUseCardRecycler.setAdapter(mCanUseCardAdapter);

        mListCardUsed = new ArrayList<>();
        mUsedCardAdapter = new CardAdapter(mListCardUsed);
        mUsedCardRecycler.setAdapter(mUsedCardAdapter);


        final Bundle bundle = getArguments();
        if (bundle != null) {
            String cardName = bundle.getString(KEY_CARD_NAME);
            if (!TextUtils.isEmpty(cardName)) {
                mTextCardName.setText(cardName);
            } else {
                mTextCardName.setVisibility(View.GONE);
            }
            final int serviceId = bundle.getInt(KEY_SERVICE_ID);
            mServiceName = bundle.getString(UserMemoFragment.KEY_SERVICE_NAME);
            mEditMemoTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    getPresenter().onEditServiceMemoClicked(serviceId);
                }
            });
            mServiceCompanyId = bundle.getInt(Constants.KEY_SERVICE_COMPANY_ID);
            getPresenter().getListCardByServiceCompany(mServiceCompanyId, Constants.INIT_PAGE, Constants.LIMIT);

            mButtonListStore.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Bundle dataListStore = new Bundle();
                    dataListStore.putInt(Constants.KEY_SERVICE_COMPANY_ID, mServiceCompanyId);
                    getActivityCallback().displayScreen(IMainView.FRAGMENT_LIST_STORE_CHECKED_IN, true, true, dataListStore);
                }
            });
        }
    }

    @Override
    protected ListCardPresenter onCreatePresenter(IListCardView view) {
        return new ListCardPresenter(view);
    }

    @Override
    protected IListCardView onCreateView() {
        return this;
    }

    @Override
    public void showListUnusedCard(List<ListCardResponse.ListCardData.CardData> cardDataList, String explain) {
        mUnusedContainer.setVisibility(View.VISIBLE);
        mLayoutButton.setVisibility(View.VISIBLE);
        if (cardDataList != null && cardDataList.size() > 0 && cardDataList.get(0).getMaxNumberOfStamp() != 0) {
            if (mUnusedCardAdapter == null) {
                mUnusedCardAdapter = new CardAdapter(cardDataList);
                mUnusedCardRecycler.setAdapter(mUnusedCardAdapter);
            } else {
                if (mUnusedCardRecycler.getAdapter() == null) {
                    mUnusedCardRecycler.setAdapter(mUnusedCardAdapter);
                }
                mUnusedCardAdapter.refreshData(cardDataList);
            }
        } else {
            mUnusedCardRecycler.setVisibility(View.GONE);
        }
        showExplainStamp(explain);
    }

    @Override
    public void hideListUnusedCard(int numberStampInCard, String explain) {
        mUnusedContainer.setVisibility(View.VISIBLE);
        mLayoutButton.setVisibility(View.VISIBLE);
        if (mUnusedCardAdapter == null) {
            mUnusedCardAdapter = new CardAdapter(null);
            mUnusedCardRecycler.setAdapter(mUnusedCardAdapter);
        } else {
            if (mUnusedCardRecycler.getAdapter() == null) {
                mUnusedCardRecycler.setAdapter(mUnusedCardAdapter);
            }
        }
        mUnusedCardAdapter.setNumberStampInCard(numberStampInCard);
        showExplainStamp(explain);
    }

    private void showExplainStamp(String explain) {
        if (!TextUtils.isEmpty(explain)) {
            mTitleExplainStamp.setVisibility(View.VISIBLE);
            mExplainStamp.setVisibility(View.VISIBLE);
            mExplainStamp.setText(Html.fromHtml(explain.trim()));
        } else {
            mTitleExplainStamp.setVisibility(View.GONE);
            mExplainStamp.setVisibility(View.GONE);
        }
    }

    @Override
    public void showListCanUseCard(List<ListCardResponse.ListCardData.CardData> cardDataList) {
        mCanUseContainer.setVisibility(View.VISIBLE);

        mListCardCanUse.addAll(cardDataList);
        mCanUseCardAdapter.notifyDataSetChanged();
//        if (mCanUseCardAdapter == null) {
//            mCanUseCardAdapter = new CardAdapter(cardDataList);
//            mCanUseCardRecycler.setAdapter(mCanUseCardAdapter);
//        } else {
//            if (mCanUseCardRecycler.getAdapter() == null) {
//                mCanUseCardRecycler.setAdapter(mCanUseCardAdapter);
//            }
//            mCanUseCardAdapter.refreshData(cardDataList);
//        }
    }

    @Override
    public void hideListCanUseCard() {
        mCanUseContainer.setVisibility(View.GONE);
    }

    @Override
    public void showListUsedCard(List<ListCardResponse.ListCardData.CardData> cardDataList, final int page, final int totalPage) {
        if (cardDataList != null && cardDataList.size() != 0) {
            mUsedContainer.setVisibility(View.VISIBLE);
            mListCardUsed.addAll(cardDataList);
            mUsedCardAdapter.notifyDataSetChanged();
            mUsedCardAdapter.setOnEndOfRecycleView(new CardAdapter.IEndOfRecycleView() {

                @Override
                public void onEndOfRecycleView() {
                    if (page < totalPage) {
                        getPresenter().getListCardByServiceCompany(mServiceCompanyId, page + 1, Constants.LIMIT);

                    }
                }
            });
        }
//        if (mUsedCardAdapter == null) {
//            mUsedCardAdapter = new CardAdapter(cardDataList);
//            mUsedCardRecycler.setAdapter(mUsedCardAdapter);
//        } else {
//            if (mUsedCardRecycler.getAdapter() == null) {
//                mUsedCardRecycler.setAdapter(mUsedCardAdapter);
//            }
//            mUsedCardAdapter.refreshData(cardDataList);
//        }
    }

    @Override
    public void hideListUsedCard() {
        mUsedContainer.setVisibility(View.GONE);
    }

    @Override
    public void displayEditServiceMemoScreen(int serviceId) {
        Bundle bundle = new Bundle();
        bundle.putInt(UserMemoFragment.KEY_SERVICE_ID, serviceId);
        bundle.putString(UserMemoFragment.KEY_SERVICE_NAME, mServiceName);
        getActivityCallback().displayScreen(IMainView.FRAGMENT_USER_MEMO, true, true, bundle);
    }

    @Override
    public int getMenuBottomID() {
        return MENU_MY_STAMP;
    }
}
