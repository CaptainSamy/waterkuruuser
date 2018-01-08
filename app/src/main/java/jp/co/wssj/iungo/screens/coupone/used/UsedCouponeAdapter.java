package jp.co.wssj.iungo.screens.coupone.used;

import android.content.Context;
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
 * Created by thang on 1/3/2018.
 */

public class UsedCouponeAdapter extends BaseAdapter {
    Context myContext;
    int myLayout;
    List<Coupone> arrayCoupone;


    public UsedCouponeAdapter(Context context, int layout, List<Coupone> couponeList) {
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


        txtNameCoupone.setMaxLines(1);
        txtNameCoupone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ml = txtNameCoupone.getMaxLines();
                txtNameCoupone.setMaxLines(ml == 1 ? 15 : 1);
            }
        });

        Date date = new Date(arrayCoupone.get(position).getTimeEndCoupon());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateFormatted = formatter.format(date);

        txtNameCoupone.setText(arrayCoupone.get(position).getNameCoupon());
        txtNickName.setText(arrayCoupone.get(position).getStoreName());
        txtTypeCoupone.setText(arrayCoupone.get(position).getTypeCouponName());
        txtExpiryDate.setText(dateFormatted);

        Date dateCurrent = new Date();
        long currentDate = dateCurrent.getTime();

        if (arrayCoupone.get(position).getIsUse() == 1) {
            txtDay.setText("期限切れ");
        }
        if (arrayCoupone.get(position).getIsUse() == 1 && arrayCoupone.get(position).getTimeEndCoupon() < currentDate) {
            txtDay.setText("利用済み");
        } else {
            txtDay.setText("期限切れ");
        }

        Picasso.with(myContext).load(arrayCoupone.get(position).getStoreImg()).into(imgAvater);


        return convertView;
    }
}
