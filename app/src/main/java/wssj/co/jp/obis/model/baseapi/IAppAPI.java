package wssj.co.jp.obis.model.baseapi;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import wssj.co.jp.obis.model.chat.ChatMessage;
import wssj.co.jp.obis.model.chat.MessageBody;
import wssj.co.jp.obis.model.checkin.CheckInBody;
import wssj.co.jp.obis.model.entities.AccessToken;
import wssj.co.jp.obis.model.entities.BlockChatStore;
import wssj.co.jp.obis.model.entities.GroupChat;
import wssj.co.jp.obis.model.entities.GroupChatMessage;
import wssj.co.jp.obis.model.entities.StoreInfo;
import wssj.co.jp.obis.model.entities.User;
import wssj.co.jp.obis.model.entities.UserResponse;
import wssj.co.jp.obis.model.pushnotification.PushNotificationResponse;
import wssj.co.jp.obis.utils.Constants;

/**
 */
public interface IAppAPI {
    @Headers("app:" + Constants.APP_CERTIFICATE)
    @POST("auth/register-user")
    APICall<AccessToken> registerUser(@Body User params);

    @Headers("app:" + Constants.APP_CERTIFICATE)
    @POST("auth/login-user")
    APICall<AccessToken> loginUser(@Body User params);

    @GET("api/get-store-checked-in")
    APICall<List<StoreInfo>> getStoreCheckedIn(@Query("type") int type, @Query("page") int page);

    @GET("chat/get-group-chat")
    APICall<List<GroupChat>> getGroupChat();

    @GET("api/get-push")
    APICall<PushNotificationResponse> getPush(@Query("page") int page);

    @POST("api/user-check-in")
    APICall<StoreInfo> checkIn(@Body CheckInBody checkInBody);

    @POST("api/user-confirm-check-in")
    APICall<Integer> userConfirmCheckIn(@Body CheckInBody checkInBody);

    @GET("chat/get-message")
    APICall<List<ChatMessage>> getHistoryChat(@Query("with_id") int storeId, @Query("max_id") long lastChatId);

    @POST("chat/insert-message")
    APICall<ChatMessage> sendChat(@Body MessageBody chatUser);

    @GET("api/get-user-info")
    APICall<UserResponse> getUserInfo();

    @POST("api/update-user-info")
    APICall updateUserInfo(@Body UserResponse response);

    @POST("api/change-account")
    APICall<AccessToken> changeAccount(@Body User body);

    @Multipart
    @POST("api/upload-image")
    APICall<String> uploadImage(@Part MultipartBody.Part file);

    @GET("chat/get-message-group")
    APICall<List<GroupChatMessage>> getHistoryGroupChat(@Query("group_id") int groupId, @Query("last_message_id") long lastChatId);

    @POST("chat/insert-message-group")
    APICall<ChatMessage> sendGroupChat(@Body MessageBody chatUser);

    @GET("api/get-block-chat-store")
    APICall<List<BlockChatStore>> getBlockChat();

    @POST("api/save-block-chat-store")
    APICall saveListBlock(@Body  BlockChatStore params);


}
