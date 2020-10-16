package wssj.co.jp.obis.model.stamp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import wssj.co.jp.obis.R;
import wssj.co.jp.obis.model.BaseModel;
import wssj.co.jp.obis.model.ErrorMessage;
import wssj.co.jp.obis.model.ErrorResponse;
import wssj.co.jp.obis.model.ResponseData;
import wssj.co.jp.obis.model.baseapi.APICallback;
import wssj.co.jp.obis.model.entities.GroupChat;
import wssj.co.jp.obis.model.entities.StoreInfo;
import wssj.co.jp.obis.utils.Logger;
import wssj.co.jp.obis.utils.Utils;
import wssj.co.jp.obis.utils.VolleySequence;

/**
 * Created by HieuPT on 5/18/2017.
 */

public class StampModel extends BaseModel {

    private static String TAG = "StampModel";

    public interface IGetListStoreCheckedResponse {

        void onSuccess(ListStoreCheckedResponse.ListStoreData data);

        void onFailure(ErrorMessage errorMessage);
    }

    public interface IOnReviewServiceResponse {

        void onSuccess(String message);

        void onFailure(ErrorMessage errorMessage);
    }

    public StampModel(Context context) {
        super(context);
    }

    public void getListStoreCheckedIn(int type, int page, APICallback<List<StoreInfo>> callback) {
        getApi().getStoreCheckedIn(type, page).getAsyncResponse(callback);

    }

    public void getListGroupChat(APICallback<List<GroupChat>> callback) {
        getApi().getGroupChat().getAsyncResponse(callback);

    }

    public List<ListStoreCheckedResponse.StoreCheckedIn> sortLocations(List<ListStoreCheckedResponse.StoreCheckedIn> locations, final double myLatitude, final double myLongitude) {
        Comparator comp = new Comparator<ListStoreCheckedResponse.StoreCheckedIn>() {

            @Override
            public int compare(ListStoreCheckedResponse.StoreCheckedIn o, ListStoreCheckedResponse.StoreCheckedIn o2) {
                float[] result1 = new float[3];
                android.location.Location.distanceBetween(myLatitude, myLongitude, o.getLatitude(), o.getLongitude(), result1);
                Float distance1 = result1[0];

                float[] result2 = new float[3];
                android.location.Location.distanceBetween(myLatitude, myLongitude, o2.getLatitude(), o2.getLongitude(), result2);
                Float distance2 = result2[0];

                return distance1.compareTo(distance2);
            }
        };
        Collections.sort(locations, comp);
        return locations;
    }

}