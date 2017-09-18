package jp.co.wssj.iungo.model.timeline;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.BaseModel;
import jp.co.wssj.iungo.model.ErrorResponse;
import jp.co.wssj.iungo.model.ResponseData;
import jp.co.wssj.iungo.utils.Utils;
import jp.co.wssj.iungo.utils.VolleySequence;

/**
 * Created by Nguyen Huu Ta on 14/9/2017.
 */

public class TimelineModel extends BaseModel {

    public interface OnGetTimelineCallback {

        void onGetTimelineSuccess(TimeLineResponse.TimeLineData timeLineData);

        void onGetTimelineFailure(String message);
    }

    public interface OnGetListCommentCallback {

        void onGetListCommentSuccess(CommentResponse.CommentData commentData);

        void onGetListCommentFailure(String message);
    }

    public interface OnLikeTimelineCallback {

        void onLikeTimelineSuccess();

        void onLikeTimelineFailure(String message);
    }

    public interface OnAddCommentCallback {

        void onAddCommentSuccess();

        void onAddCommentFailure(String message);
    }

    public TimelineModel(@NonNull Context context) {
        super(context);
    }

    public void getTimeLine(String token, final OnGetTimelineCallback callback) {
        final Request request = APICreator.getTimeline(token, new Response.Listener<TimeLineResponse>() {

            @Override
            public void onResponse(TimeLineResponse response) {
                if (response.isSuccess() && response.getData() != null) {
                    TimeLineResponse.TimeLineData data = response.getData();
                    callback.onGetTimelineSuccess(data);
                } else {
                    callback.onGetTimelineFailure(response.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                if (errorResponse != null) {
                    callback.onGetTimelineFailure(errorResponse.getMessage());
                } else {
                    callback.onGetTimelineFailure(getStringResource(R.string.network_error));
                }
            }
        });
        VolleySequence.getInstance().addRequest(request);
    }

    public void getListComment(String token, int timelineId, final OnGetListCommentCallback callback) {
        final Request request = APICreator.getListComment(token, timelineId, new Response.Listener<CommentResponse>() {

            @Override
            public void onResponse(CommentResponse response) {
                if (response.isSuccess() && response.getData() != null) {
                    CommentResponse.CommentData data = response.getData();
                    callback.onGetListCommentSuccess(data);
                } else {
                    callback.onGetListCommentFailure(response.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                if (errorResponse != null) {
                    callback.onGetListCommentFailure(errorResponse.getMessage());
                } else {
                    callback.onGetListCommentFailure(getStringResource(R.string.network_error));
                }
            }
        });
        VolleySequence.getInstance().addRequest(request);
    }

    public void likeTimeline(String token, int timelineId, int likeId, int typeLike, final OnLikeTimelineCallback callback) {
        final Request request = APICreator.likeTimeline(token, timelineId, likeId, typeLike, new Response.Listener<ResponseData>() {

            @Override
            public void onResponse(ResponseData response) {
                if (response.isSuccess()) {
                    callback.onLikeTimelineSuccess();
                } else {
                    callback.onLikeTimelineFailure(response.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                if (errorResponse != null) {
                    callback.onLikeTimelineFailure(errorResponse.getMessage());
                } else {
                    callback.onLikeTimelineFailure(getStringResource(R.string.network_error));
                }
            }
        });
        VolleySequence.getInstance().addRequest(request);
    }

    public void addComment(String token, int timelineId, String comment, final OnAddCommentCallback callback) {
        final Request request = APICreator.addComment(token, timelineId, comment, new Response.Listener<ResponseData>() {

            @Override
            public void onResponse(ResponseData response) {
                if (response.isSuccess()) {
                    callback.onAddCommentSuccess();
                } else {
                    callback.onAddCommentFailure(response.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                if (errorResponse != null) {
                    callback.onAddCommentFailure(errorResponse.getMessage());
                } else {
                    callback.onAddCommentFailure(getStringResource(R.string.network_error));
                }
            }
        });
        VolleySequence.getInstance().addRequest(request);
    }
}
