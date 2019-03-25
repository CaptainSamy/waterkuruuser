package wssj.co.jp.olioa.model.baseapi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import wssj.co.jp.olioa.BuildConfig;

public class APIService {

    public static APIService apiService;

    public static APIService getInstance() {
        if (apiService == null) {
            apiService = new APIService();
        }
        return apiService;
    }

    private Map<String, Retrofit> clients = new HashMap<>();

    private static final int REQUEST_TIMEOUT = 20;

    private IAppAPI appAPI;

    private Retrofit getClient(String baseUrl) {
        Retrofit retrofit = clients.get(baseUrl);
        if (retrofit == null) {
            OkHttpClient.Builder httpClient = createOkHttp();
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(new APICallAdapterFactory())
                    .build();
            clients.put(baseUrl, retrofit);
        }
        appAPI = retrofit.create(IAppAPI.class);
        return retrofit;
    }

    private OkHttpClient.Builder createOkHttp() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {
                okhttp3.Request request = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .build();
                return chain.proceed(request);
            }
        });
        return httpClient;
    }

    private OkHttpClient.Builder createOkHttp(final String token) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {
                okhttp3.Request request = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .addHeader("Authorization", token)
                        .build();
                return chain.proceed(request);
            }
        });
        return httpClient;
    }

    public void addAuthorizationHeader(String token) {
        String baseUrl = BuildConfig.HOST;
        OkHttpClient.Builder httpClient = createOkHttp(token);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new APICallAdapterFactory())
                .build();
        clients.put(baseUrl, retrofit);
    }

    public IAppAPI getAppAPI(String baseUrl) {
        return getClient(baseUrl).create(IAppAPI.class);
    }

}