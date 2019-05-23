package wssj.co.jp.obis.model.menu;

import com.google.gson.annotations.SerializedName;

import wssj.co.jp.obis.model.GsonSerializable;
import wssj.co.jp.obis.model.ResponseData;

/**
 * Created by Nguyen Huu Ta on 5/7/2017.
 */

public class HtmlResponse extends ResponseData<HtmlResponse.HtmlData> {

    public class HtmlData implements GsonSerializable {

        @SerializedName("content")
        private String mHtml;

        public String getHtml() {
            return mHtml;
        }
    }
}
