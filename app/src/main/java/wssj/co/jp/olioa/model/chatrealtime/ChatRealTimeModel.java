package wssj.co.jp.olioa.model.chatrealtime;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.BaseModel;
import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.utils.VolleySequence;

/**
 * Created by thang on 1/26/2018.
 */

public class ChatRealTimeModel extends BaseModel {

    public ChatRealTimeModel(Context context) {
        super(context);
    }

    public interface IGetUserProfileResponeCallBack {

        void onGetUserProfileResponseSuccess(long userProfileResponse);

        void onGetUserProfileResponseFailure(ErrorMessage errorMessage);
    }

    public void GetUserProfile(String token, final ChatRealTimeModel.IGetUserProfileResponeCallBack callBack) {
        final Request request = APICreator.getUserProfile(token,
                new Response.Listener<UserProfileResponse>() {

                    @Override
                    public void onResponse(UserProfileResponse response) {
                        callBack.onGetUserProfileResponseSuccess(response.getData().getId());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callBack.onGetUserProfileResponseFailure(new ErrorMessage(getStringResource(R.string.network_error)));
                    }
                });
        request.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySequence.getInstance().addRequest(request);
    }
}
