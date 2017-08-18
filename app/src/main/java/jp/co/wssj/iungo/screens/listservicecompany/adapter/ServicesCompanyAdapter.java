package jp.co.wssj.iungo.screens.listservicecompany.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.stamp.ListCompanyResponse;
import jp.co.wssj.iungo.widget.CircleImageView;

/**
 * Created by HieuPT on 5/19/2017.
 */

public class ServicesCompanyAdapter extends ArrayAdapter<ListCompanyResponse.ListCompanyData.CompanyData> {

    private final List<ListCompanyResponse.ListCompanyData.CompanyData> mCardList;

    private final LayoutInflater mInflater;

    public ServicesCompanyAdapter(@NonNull Context context, @NonNull List<ListCompanyResponse.ListCompanyData.CompanyData> objects) {
        super(context, 0, objects);
        mCardList = objects;
        mInflater = LayoutInflater.from(context);
    }

    public void refreshData(List<ListCompanyResponse.ListCompanyData.CompanyData> cardList) {
        if (cardList != null) {
            mCardList.clear();
            mCardList.addAll(cardList);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder vh;
        if (view == null) {
            view = mInflater.inflate(R.layout.company_card_item_layout, parent, false);
            vh = new ViewHolder();
            vh.mCompanyLogoImageView = (CircleImageView) view.findViewById(R.id.company_logo_image_view);
            vh.mCompanyNameTextView = (TextView) view.findViewById(R.id.company_name_text_view);
            vh.mCardAmountTextView = (TextView) view.findViewById(R.id.card_amount_text_view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        vh.fillData(getContext(), getItem(position));
        return view;
    }

    private static class ViewHolder {

        private CircleImageView mCompanyLogoImageView;

        private TextView mCompanyNameTextView;

        private TextView mCardAmountTextView;

        private void fillData(Context context, ListCompanyResponse.ListCompanyData.CompanyData data) {
            if (data != null) {
                if (!TextUtils.isEmpty(data.getLogo())) {
                    Glide.with(context)
                            .load(data.getLogo())
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>() {

                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    mCompanyLogoImageView.setImageBitmap(resource);
                                }

                                @Override
                                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                    super.onLoadFailed(e, errorDrawable);
                                }
                            });
                } else {
                    mCompanyLogoImageView.setImageBitmap(null);
                }
                mCompanyNameTextView.setText(data.getCardName());
                if (data.getCardType() == 1) {
                    mCardAmountTextView.setText(context.getString(R.string.card_amount, String.valueOf(data.getCardNumber())));
                } else if (data.getCardType() == 2) {
                    mCardAmountTextView.setText(context.getString(R.string.point_amount, String.valueOf(0)));
                } else {
                    mCardAmountTextView.setText(context.getString(R.string.number_push_unread, String.valueOf(data.getUnreadPush())));
                }
            }
        }
    }
}
