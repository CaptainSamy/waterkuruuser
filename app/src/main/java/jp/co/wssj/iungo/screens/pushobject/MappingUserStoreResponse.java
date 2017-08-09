package jp.co.wssj.iungo.screens.pushobject;

import com.google.gson.annotations.SerializedName;

import jp.co.wssj.iungo.model.GsonSerializable;
import jp.co.wssj.iungo.model.ResponseData;

/**
 * Created by Nguyen Huu Ta on 8/8/2017.
 */

public class MappingUserStoreResponse extends ResponseData<MappingUserStoreResponse.PushData> {

    public class PushData implements GsonSerializable {

        @SerializedName("push_id")
        private long mPushId;

        public long getPushId() {
            return mPushId;
        }
    }

}
