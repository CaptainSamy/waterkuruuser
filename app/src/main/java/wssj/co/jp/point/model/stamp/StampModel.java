package wssj.co.jp.point.model.stamp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import wssj.co.jp.point.R;
import wssj.co.jp.point.model.BaseModel;
import wssj.co.jp.point.model.ErrorMessage;
import wssj.co.jp.point.model.ErrorResponse;
import wssj.co.jp.point.model.ResponseData;
import wssj.co.jp.point.utils.Logger;
import wssj.co.jp.point.utils.Utils;
import wssj.co.jp.point.utils.VolleySequence;

/**
 * Created by HieuPT on 5/18/2017.
 */

public class StampModel extends BaseModel {

    private static String TAG = "StampModel";

    public interface IGetListCompanyResponse {

        void onSuccess(List<ListCompanyResponse.ListCompanyData.CompanyData> companyDataList);

        void onFailure(ErrorMessage errorMessage);
    }

    public interface IGetListCardResponse {

        void onSuccess(List<ListCardResponse.ListCardData.CardData> cards,int page,int totalPage);

        void onFailure(ErrorMessage errorMessage);
    }

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

    public void getListCompany(String token, final IGetListCompanyResponse callback) {
        Request request = APICreator.createGetListCompanyRequest(token,
                new Response.Listener<ListCompanyResponse>() {

                    @Override
                    public void onResponse(ListCompanyResponse response) {
                        if (response.isSuccess()) {
                            ListCompanyResponse.ListCompanyData data = response.getData();
                            List<ListCompanyResponse.ListCompanyData.CompanyData> cardList;
                            if (data != null) {
                                cardList = data.getListCompany();
                            } else {
                                cardList = new ArrayList<>();
                            }
                            callback.onSuccess(cardList);
                        } else {
                            callback.onFailure(new ErrorMessage(response.getMessage()));
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                        if (errorResponse != null) {
                            callback.onFailure(new ErrorMessage(errorResponse.getMessage()));
                        } else {
                            callback.onFailure(new ErrorMessage(getStringResource(R.string.network_error)));
                        }
                    }
                });
        VolleySequence.getInstance().addRequest(request);
    }

    public void getListCardByServiceCompany(String token, int serviceCompanyId, int page, int limit, final IGetListCardResponse callback) {
        Request request = APICreator.createGetListCardByServiceCompanyRequest(token, serviceCompanyId, page, limit,
                new Response.Listener<ListCardResponse>() {

                    @Override
                    public void onResponse(ListCardResponse response) {
                        Logger.d(TAG, "#getListCardByServiceCompany => onResponse");
                        if (response.isSuccess()) {
                            List<ListCardResponse.ListCardData.CardData> cardDataList = response.getData().getCards();
                            if (cardDataList != null) {
                                callback.onSuccess(cardDataList,response.getData().getPage(),response.getData().getTotalPage());
                                return;
                            }
                        }
                        callback.onFailure(new ErrorMessage(response.getMessage()));
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#getListCardByServiceCompany => onErrorResponse");
                        ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                        if (errorResponse != null) {
                            callback.onFailure(new ErrorMessage(errorResponse.getMessage()));
                        } else {
                            callback.onFailure(new ErrorMessage(getStringResource(R.string.network_error)));
                        }
                    }
                });
        VolleySequence.getInstance().addRequest(request);
    }

    public void getListStoreCheckedIn(String token, int serviceCompanyId, final double latitude, final double longitude, final IGetListStoreCheckedResponse callback) {

        final Request request = APICreator.getListStoreCheckedIn(token, serviceCompanyId,
                new Response.Listener<ListStoreCheckedResponse>() {

                    @Override
                    public void onResponse(ListStoreCheckedResponse response) {
                        Logger.d(TAG, "#getListStoreCheckedIn => onResponse ");
                        if (response.isSuccess() && response.getData() != null) {
                            ListStoreCheckedResponse.ListStoreData data = response.getData();
//                            List<ListStoreCheckedResponse.StoreCheckedIn> listStore = data.getListStore();
//                            if (listStore != null && listStore.size() > 0) {
//                                sortLocations(listStore, latitude, longitude);
                            callback.onSuccess(data);
                        } else {
                            callback.onFailure(new ErrorMessage(response.getMessage()));
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#getListStoreCheckedIn => onErrorResponse");
                        ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                        if (errorResponse != null) {
                            callback.onFailure(new ErrorMessage(errorResponse.getMessage()));
                        } else {
                            callback.onFailure(new ErrorMessage(getStringResource(R.string.network_error)));
                        }
                    }
                });
        VolleySequence.getInstance().addRequest(request);

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

    public void reviewServiceByStamp(String token, int stampId, float rating, String note, final IOnReviewServiceResponse callback) {
        Request request = APICreator.reviewServiceByStamp(token, stampId, rating, note,
                new Response.Listener<ResponseData>() {

                    @Override
                    public void onResponse(ResponseData response) {
                        if (response.isSuccess()) {
                            callback.onSuccess(getStringResource(R.string.success));
                        } else {
                            callback.onFailure(new ErrorMessage(response.getMessage()));
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                        if (errorResponse != null) {
                            callback.onFailure(new ErrorMessage(errorResponse.getMessage()));
                        } else {
                            callback.onFailure(new ErrorMessage(getStringResource(R.string.network_error)));
                        }
                    }
                });
        VolleySequence.getInstance().addRequest(request);
    }

}
