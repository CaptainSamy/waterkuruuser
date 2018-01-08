package jp.co.wssj.iungo.model.coupone;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import jp.co.wssj.iungo.model.GsonSerializable;
import jp.co.wssj.iungo.model.ResponseData;
import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.model.pushnotification.ListNotificationResponse;

/**
 * Created by thang on 1/2/2018.
 */

public class ListCouponeResponse extends ResponseData<ListCouponeResponse.CouponeListData> {
    public class CouponeListData implements GsonSerializable {
        @SerializedName("list_coupons")
        private List<Coupone> mList;

        public List<Coupone> getmList() {
            return mList;
        }

        public void setmList(List<Coupone> mList) {
            this.mList = mList;
        }
    }
}
