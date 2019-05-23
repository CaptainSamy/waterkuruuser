package wssj.co.jp.obis.model.coupone;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import wssj.co.jp.obis.model.GsonSerializable;
import wssj.co.jp.obis.model.ResponseData;

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
