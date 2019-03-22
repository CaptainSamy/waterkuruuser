package wssj.co.jp.olioa.model.baseapi;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;

import wssj.co.jp.olioa.App;
import wssj.co.jp.olioa.utils.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APICall<Data> extends APIResponse<Data> {

    public static final String CANNOT_CONNECT = "サーバーが接続出来ません。";

    private Call<APIResponse<Data>> call;

    public APICall(Call<APIResponse<Data>> call) {
        this.call = call;
        ActivityManager am = (ActivityManager) App.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
    }

    public void getAsyncResponse(final APICallback<Data> callback) {
        call.enqueue(new Callback<APIResponse<Data>>() {

            @Override
            public void onResponse(Call<APIResponse<Data>> call, final Response<APIResponse<Data>> response) {
                Logger.d("APICall", "onResponse");
                App.getInstance().getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            APIResponse<Data> apiResponse = response.body();
                            if (apiResponse != null && apiResponse.isSuccess()) {
                                callback.onSuccess(apiResponse.getData());
                            } else {
                                callback.onFailure(apiResponse.getError().getMessage());
                            }
                        } else {
                            int code = response.raw().code();
                            String message = null;
                            try {
                                String json = response.errorBody().string();
                                Gson gson = new Gson();
                                APIResponse apiResponse = gson.fromJson(json, APIResponse.class);
                                message = apiResponse.getError().getMessage();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (code == 404) {
                                message = CANNOT_CONNECT;
                            }
                            Logger.d("APICall", message);
                            callback.onFailure(message);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<APIResponse<Data>> call, Throwable t) {
                Logger.d("APICall", "onFailure");
                App.getInstance().getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        callback.onFailure(CANNOT_CONNECT);
                    }
                });
            }
        });
    }

}
