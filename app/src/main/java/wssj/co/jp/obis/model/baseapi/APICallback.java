package wssj.co.jp.obis.model.baseapi;

public interface APICallback<Data> {

    void onSuccess(Data data);

    void onFailure(String errorMessage);
}
