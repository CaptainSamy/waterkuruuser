package wssj.co.jp.olioa.model.baseapi;

import android.support.annotation.NonNull;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;

public class APICallAdapter<Data> implements CallAdapter<APIResponse<Data>, APICall<Data>> {

    private Type type;

    public APICallAdapter(Type type) {
        this.type = type;
    }

    @Override
    public Type responseType() {
        return type;
    }

    @Override
    public APICall<Data> adapt(@NonNull Call<APIResponse<Data>> call) {
        return new APICall<>(call);
    }
}
