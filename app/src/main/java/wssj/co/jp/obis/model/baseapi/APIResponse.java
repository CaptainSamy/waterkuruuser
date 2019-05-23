package wssj.co.jp.obis.model.baseapi;

import com.google.gson.annotations.SerializedName;

import wssj.co.jp.obis.model.ErrorMessage;

public class APIResponse<Data> {

    @SerializedName("result")
    private String result;

    @SerializedName("data")
    private Data data;

    @SerializedName("error")
    private ErrorMessage error;

    public String getResult() {
        return result;
    }

    public Data getData() {
        return data;
    }

    public boolean isSuccess() {
        return "ok".equalsIgnoreCase(result) || "success".equalsIgnoreCase(result);
    }

    public ErrorMessage getError() {
        if (error == null) {
            error = new ErrorMessage("サーバーが接続出来ません。");
        }
        return error;
    }

    public APIResponse() {
    }
}
