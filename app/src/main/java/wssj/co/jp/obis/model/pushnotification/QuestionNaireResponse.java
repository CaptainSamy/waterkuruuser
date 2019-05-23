package wssj.co.jp.obis.model.pushnotification;

import com.google.gson.annotations.SerializedName;

import wssj.co.jp.obis.model.GsonSerializable;
import wssj.co.jp.obis.model.ResponseData;

/**
 * Created by Nguyen Huu Ta on 6/9/2017.
 */

public class QuestionNaireResponse extends ResponseData<QuestionNaireResponse.QuestionNaire> {

    public class QuestionNaire implements GsonSerializable {

        @SerializedName("code")
        private String mCode;

        public String getCode() {
            return mCode;
        }
    }
}
