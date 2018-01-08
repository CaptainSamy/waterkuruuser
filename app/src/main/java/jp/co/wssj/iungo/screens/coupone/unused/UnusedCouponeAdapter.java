package jp.co.wssj.iungo.screens.coupone.unused;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.coupone.Coupone;

/**
 * Created by thang on 1/4/2018.
 */

public class UnusedCouponeAdapter extends BaseAdapter {
    Context myContext;
    int myLayout;
    List<Coupone> arrayCoupone;

    public UnusedCouponeAdapter(Context context, int layout, List<Coupone> couponeList) {
        myContext = context;
        myLayout = layout;
        arrayCoupone = couponeList;
    }

    @Override
    public int getCount() {
        //Logger.d("ddd", String.valueOf(arrayCoupone.size()));
        if (arrayCoupone.size() > 0) {
            return arrayCoupone.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_coupone, null);

        final TextView txtNameCoupone = (TextView) convertView.findViewById(R.id.txtNameCoupone);
        TextView txtNickName = (TextView) convertView.findViewById(R.id.txtNickName);
        final TextView txtTypeCoupone = (TextView) convertView.findViewById(R.id.txtTypeCoupone);
        TextView txtExpiryDate = (TextView) convertView.findViewById(R.id.txtExpiryDate);
        TextView txtDay = (TextView) convertView.findViewById(R.id.txtDay);
        ImageView imgAvater = (ImageView) convertView.findViewById(R.id.imgAvatar);


        Date date = new Date(arrayCoupone.get(position).getTimeEndCoupon());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateFormatted = formatter.format(date);


        Date dateCurrent = new Date();
        long currentDate=dateCurrent.getTime();

        Date dateXX = new Date(arrayCoupone.get(position).getTimeEndCoupon()-currentDate);
        SimpleDateFormat formatterXX = new SimpleDateFormat("dd");
        String dateFormattedXX = formatterXX.format(dateXX);


        txtNameCoupone.setText(arrayCoupone.get(position).getNameCoupon());
        txtNickName.setText(arrayCoupone.get(position).getStoreName());
        txtTypeCoupone.setText(arrayCoupone.get(position).getTypeCouponName());
        txtExpiryDate.setText(dateFormatted);
        txtDay.setText("後"+dateFormattedXX+"日");
        Picasso.with(myContext).load(arrayCoupone.get(position).getStoreImg()).into(imgAvater);

        return convertView;
    }
}
