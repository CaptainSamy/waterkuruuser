package jp.co.wssj.iungo.model.menu;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.BaseModel;
import jp.co.wssj.iungo.model.ErrorResponse;
import jp.co.wssj.iungo.model.ResponseData;
import jp.co.wssj.iungo.utils.Utils;
import jp.co.wssj.iungo.utils.VolleySequence;

/**
 * Created by Nguyen Huu Ta on 5/7/2017.
 */

public class MenuModel extends BaseModel {

    public MenuModel(Context context) {
        super(context);
    }

    public interface IOnGetListQACallback {

        void onGetListQASuccess(int currentPage, int totalPage, List<QAResponse.ListQAData.QAData> data);

        void onGetListQAFailure(String message);
    }

    public interface IOnFeedBackCallback {

        void onFeedBackSuccess(String message);

        void onFeedBackFailure(String message);
    }

    public interface IOnHowUseAppCallback {

        void onHowUseAppSuccess(String html);

        void onHowUseAppFailure(String message);
    }

    public interface IOnPolicyCallback {

        void onPolicySuccess(String html);

        void onPolicyFailure(String message);
    }

    public interface IOnGetTermOfService {

        void onGetTermOfServiceSuccess(String html);

        void onGetTermOfServiceFailure(String message);
    }

    public void getListQA(String token, int page, int limit, final IOnGetListQACallback callback) {
        final Request requestQA = APICreator.getListQA(token, page, limit, new Response.Listener<QAResponse>() {

            @Override
            public void onResponse(QAResponse response) {
                if (response.isSuccess()) {
                    QAResponse.ListQAData data = response.getData();
                    callback.onGetListQASuccess(data.getPage(), data.getTotalPage(), data.getListQA());
                } else {
                    callback.onGetListQAFailure(response.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                if (errorResponse != null) {
                    callback.onGetListQAFailure(errorResponse.getMessage());
                } else {
                    callback.onGetListQAFailure(getStringResource(R.string.failure));
                }
            }
        });
        VolleySequence.getInstance().addRequest(requestQA);
    }

    public void feedback(String token, String feedback, final IOnFeedBackCallback callback) {
        final Request requestQA = APICreator.feedback(token, feedback, new Response.Listener<ResponseData>() {

            @Override
            public void onResponse(ResponseData response) {
                if (response.isSuccess()) {
                    if (TextUtils.isEmpty(response.getMessage())) {
                        callback.onFeedBackSuccess(getStringResource(R.string.feedback_success));
                    } else {
                        callback.onFeedBackSuccess(response.getMessage());
                    }
                } else {
                    callback.onFeedBackFailure(response.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                if (errorResponse != null) {
                    callback.onFeedBackFailure(errorResponse.getMessage());
                } else {
                    callback.onFeedBackFailure(getStringResource(R.string.failure));
                }
            }
        });
        VolleySequence.getInstance().addRequest(requestQA);
    }

    public void howUseApp(String token, final IOnHowUseAppCallback callback) {
        final Request requestQA = APICreator.howUseApp(token, new Response.Listener<HtmlResponse>() {

            @Override
            public void onResponse(HtmlResponse response) {
                if (response.isSuccess() && response.getData() != null) {
                    callback.onHowUseAppSuccess(response.getData().getHtml());
                } else {
                    callback.onHowUseAppFailure(response.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                if (errorResponse != null) {
                    callback.onHowUseAppFailure(errorResponse.getMessage());
                } else {
                    callback.onHowUseAppFailure(getStringResource(R.string.failure));
                }
            }
        });
        VolleySequence.getInstance().addRequest(requestQA);
    }

    public void policy(final IOnPolicyCallback callback) {
        final Request requestQA = APICreator.policy(new Response.Listener<HtmlResponse>() {

            @Override
            public void onResponse(HtmlResponse response) {
                if (response.isSuccess() && response.getData() != null) {
                    callback.onPolicySuccess(response.getData().getHtml());
                } else {
                    callback.onPolicyFailure(response.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                if (errorResponse != null) {
                    callback.onPolicyFailure(errorResponse.getMessage());
                } else {
                    callback.onPolicyFailure(getStringResource(R.string.failure));
                }
            }
        });
        VolleySequence.getInstance().addRequest(requestQA);
    }

    public void termOfService(final IOnGetTermOfService callback) {
        final Request requestQA = APICreator.termOfService(new Response.Listener<HtmlResponse>() {

            @Override
            public void onResponse(HtmlResponse response) {
                if (response.isSuccess() && response.getData() != null) {
                    callback.onGetTermOfServiceSuccess(response.getData().getHtml());
                } else {
                    callback.onGetTermOfServiceFailure(response.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                if (errorResponse != null) {
                    callback.onGetTermOfServiceFailure(errorResponse.getMessage());
                } else {
                    callback.onGetTermOfServiceFailure(getStringResource(R.string.failure));
                }
            }
        });
        VolleySequence.getInstance().addRequest(requestQA);
    }
}
