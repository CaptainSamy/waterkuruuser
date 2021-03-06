package wssj.co.jp.obis.model.timeline;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import wssj.co.jp.obis.R;
import wssj.co.jp.obis.model.BaseModel;
import wssj.co.jp.obis.model.ErrorResponse;
import wssj.co.jp.obis.model.ResponseData;
import wssj.co.jp.obis.utils.Utils;
import wssj.co.jp.obis.utils.VolleySequence;

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

    public interface OnLikeCallback {

        void onLikeSuccess();

        void onLikeFailure(String message);
    }

    public interface OnAddCommentCallback {

        void onAddCommentSuccess();

        void onAddCommentFailure(String message);
    }

    public interface IGetProfileCallback {

        void getProfileSuccess(ProfileResponse.Profile profile);

        void getProfileFailure(String message);
    }

    public TimelineModel(@NonNull Context context) {
        super(context);
    }

    public void getTimeLine(String token, int lastTimelineId, final OnGetTimelineCallback callback) {
        final Request request = APICreator.getTimeline(token, lastTimelineId, new Response.Listener<TimeLineResponse>() {

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

    public void getTimeLineDetail(String token, int managerUserId, int lastTimelineId, final OnGetTimelineCallback callback) {
        final Request request = APICreator.getTimelineByStoreId(token, managerUserId, lastTimelineId, new Response.Listener<TimeLineResponse>() {

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

    public void likeTimeline(String token, int timelineId, int newLikeId, int oldLikeId, int typeLike, final OnLikeCallback callback) {
        final Request request = APICreator.likeTimeline(token, timelineId, newLikeId, oldLikeId, typeLike, new Response.Listener<ResponseData>() {

            @Override
            public void onResponse(ResponseData response) {
                if (response.isSuccess()) {
                    callback.onLikeSuccess();
                } else {
                    callback.onLikeFailure(response.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                if (errorResponse != null) {
                    callback.onLikeFailure(errorResponse.getMessage());
                } else {
                    callback.onLikeFailure(getStringResource(R.string.network_error));
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

    public void likeComment(String token, int commentId, int newLikeId, int oldLikeId, int typeLike, final OnLikeCallback callback) {
        final Request request = APICreator.likeComment(token, commentId, newLikeId, oldLikeId, typeLike, new Response.Listener<ResponseData>() {

            @Override
            public void onResponse(ResponseData response) {
                if (response.isSuccess()) {
                    callback.onLikeSuccess();
                } else {
                    callback.onLikeFailure(response.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                if (errorResponse != null) {
                    callback.onLikeFailure(errorResponse.getMessage());
                } else {
                    callback.onLikeFailure(getStringResource(R.string.network_error));
                }
            }
        });
        VolleySequence.getInstance().addRequest(request);
    }

    public void getProfileStore(final String token, final int manageUserId, final IGetProfileCallback callback) {
        final Request request = APICreator.getProfileStore(token, manageUserId, new Response.Listener<ProfileResponse>() {

            @Override
            public void onResponse(ProfileResponse response) {
                if (response.isSuccess()) {
                    callback.getProfileSuccess(response.getData());
                } else {
                    callback.getProfileFailure(response.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                if (errorResponse != null) {
                    callback.getProfileFailure(errorResponse.getMessage());
                } else {
                    callback.getProfileFailure(getStringResource(R.string.network_error));
                }
            }
        });
        VolleySequence.getInstance().addRequest(request);
    }

}
