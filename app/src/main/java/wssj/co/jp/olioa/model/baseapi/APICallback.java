package wssj.co.jp.olioa.model.baseapi;

public interface APICallback<Data> {

    void onSuccess(Data data);

    void onFailure(String errorMessage);
}
