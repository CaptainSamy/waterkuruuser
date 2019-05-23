package wssj.co.jp.obis.model.baseapi;

import android.support.annotation.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class APICallAdapterFactory extends CallAdapter.Factory {
    @Override
    public CallAdapter<?, ?> get(@NonNull Type returnType, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {
        return new APICallAdapter<>(returnType);
    }
}
