package wssj.co.jp.point.screens.listcard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import wssj.co.jp.point.R;
import wssj.co.jp.point.model.stamp.ListCardResponse.ListCardData.CardData;
import wssj.co.jp.point.widget.ExpandableHeightGridView;

/**
 * Created by HieuPT on 6/1/2017.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private final List<CardData> mCards;

    private IEndOfRecycleView mOnEndOfRecycleView;

    public CardAdapter(List<CardData> cards) {
        mCards = cards;
    }

    public void refreshData(List<CardData> cards) {
        if (cards != null) {
            mCards.clear();
            mCards.addAll(cards);
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.stamp_grid_view_layout, parent, false);
        return new ViewHolder(context, view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(mCards != null ? mCards.get(position) : null);
        if (position == (getItemCount() - 1)) {
            if (mOnEndOfRecycleView != null) {
                mOnEndOfRecycleView.onEndOfRecycleView();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mCards == null ? 1 : mCards.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ExpandableHeightGridView mGridView;

        private final Context mContext;

        ViewHolder(Context context, View itemView) {
            super(itemView);
            mGridView = (ExpandableHeightGridView) itemView;
            mGridView.setExpanded(true);
            mContext = context;
        }

        void bindData(CardData data) {
            if (data != null) {
                mGridView.setAdapter(new StampAdapter(mContext, data.getStamps(), data.getMaxNumberOfStamp()));
            } else {
                mGridView.setAdapter(new StampAdapter(mContext, 15));
            }
        }
    }

    public interface IEndOfRecycleView {

        void onEndOfRecycleView();
    }

    public void setOnEndOfRecycleView(IEndOfRecycleView mOnEndOfRecycleView) {
        this.mOnEndOfRecycleView = mOnEndOfRecycleView;
    }
}
