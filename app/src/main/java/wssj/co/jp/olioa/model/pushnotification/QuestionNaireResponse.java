package wssj.co.jp.olioa.model.pushnotification;

import com.google.gson.annotations.SerializedName;

import wssj.co.jp.olioa.model.GsonSerializable;
import wssj.co.jp.olioa.model.ResponseData;

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
