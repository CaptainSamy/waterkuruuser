package wssj.co.jp.olioa.model.baseapi;

import wssj.co.jp.olioa.model.entities.AccessToken;
import wssj.co.jp.olioa.model.entities.User;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import wssj.co.jp.olioa.model.pushnotification.PushNotificationResponse;

/**
 */
public interface IAppAPI {

    @POST("auth/register-user")
    APICall<AccessToken> registerUser(@Body User params);

    @POST("auth/login-user")
    APICall<AccessToken> loginUser(@Body User params);

    @GET("api/get-push")
    APICall<PushNotificationResponse> getPush(@Query("page") int page);
}
