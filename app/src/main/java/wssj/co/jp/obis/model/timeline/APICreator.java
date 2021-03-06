package wssj.co.jp.obis.model.timeline;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import wssj.co.jp.obis.model.ResponseData;
import wssj.co.jp.obis.model.volleyrequest.GsonJsonRequest;
import wssj.co.jp.obis.model.volleyrequest.GsonRequest;
import wssj.co.jp.obis.utils.Constants;
import wssj.co.jp.obis.utils.Logger;

/**
 * Created by Nguyen Huu Ta on 13/9/2017.
 */

final class APICreator {

    private static final String TAG = "ListTimeline.APICreator";

    private static final String API_GET_TIME_LINE = Constants.BASE_URL + "/api/client/users/get-list-timeline-by-user";

    private static final String API_GET_TIME_LINE_DETAIL = Constants.BASE_URL + "/api/client/users/user-get-list-timeline-by-store-id";

    private static final String API_GET_LIST_COMMENT = Constants.BASE_URL + "/api/client/users/user-get-list-comment-by-timeline-id";

    private static final String API_ADD_COMMENT = Constants.BASE_URL + "/api/client/users/user-add-comment";

    private static final String API_LIKE_TIMELINE = Constants.BASE_URL + "/api/client/users/user-like-timeline";

    private static final String API_LIKE_COMMENT = Constants.BASE_URL + "/api/client/users/user-like-comment";

    private static final String API_PROFILE_STORE = Constants.BASE_URL + "/api/client/users/get-store-profile";

    private APICreator() {
    }

    static GsonRequest<TimeLineResponse> getTimeline(final String token, final int lastTimelineId, final Response.Listener<TimeLineResponse> listener, final Response.ErrorListener errorListener) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        return new GsonJsonRequest<TimeLineResponse>(Request.Method.POST,
                API_GET_TIME_LINE,
                TimeLineResponse.class,
                headers,
                new Response.Listener<TimeLineResponse>() {

                    @Override
                    public void onResponse(TimeLineResponse response) {
                        Logger.i(TAG, "#getTimeline => onResponse");
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.i(TAG, "#getTimeline => onErrorResponse");
                        errorListener.onErrorResponse(error);
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> map = new HashMap<>();
                map.put("last_timeline_id", lastTimelineId);
                map.put("limit_timeline", Constants.LIMIT_TIMELINE);
                return map;
            }
        };
    }

    static GsonRequest<TimeLineResponse> getTimelineByStoreId(final String token, final int manageUserId, final int lastTimelineId, final Response.Listener<TimeLineResponse> listener, final Response.ErrorListener errorListener) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        return new GsonJsonRequest<TimeLineResponse>(Request.Method.POST,
                API_GET_TIME_LINE_DETAIL,
                TimeLineResponse.class,
                headers,
                new Response.Listener<TimeLineResponse>() {

                    @Override
                    public void onResponse(TimeLineResponse response) {
                        Logger.i(TAG, "#getTimeline => onResponse");
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.i(TAG, "#getTimeline => onErrorResponse");
                        errorListener.onErrorResponse(error);
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> map = new HashMap<>();
                map.put("manager_id", manageUserId);
                map.put("last_timeline_id", lastTimelineId);
                map.put("limit_timeline", Constants.LIMIT_TIMELINE);
                return map;
            }
        };
    }

    static GsonRequest<CommentResponse> getListComment(final String token, final int timelineId, final Response.Listener<CommentResponse> listener, final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        return new GsonJsonRequest<CommentResponse>(Request.Method.POST,
                API_GET_LIST_COMMENT,
                CommentResponse.class,
                headers,
                new Response.Listener<CommentResponse>() {

                    @Override
                    public void onResponse(CommentResponse response) {
                        Logger.i(TAG, "#getListComment => onResponse");
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.i(TAG, "#getListComment => onErrorResponse");
                        errorListener.onErrorResponse(error);
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> map = new HashMap<>();
                map.put("timeline_id", timelineId);
                return map;
            }
        };
    }

    static GsonRequest<ResponseData> likeTimeline(final String token, final int timelineId, final int newLikeId, final int oldLikeId, final int typeLike, final Response.Listener<ResponseData> listener, final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        return new GsonJsonRequest<ResponseData>(Request.Method.POST,
                API_LIKE_TIMELINE,
                ResponseData.class,
                headers,
                new Response.Listener<ResponseData>() {

                    @Override
                    public void onResponse(ResponseData response) {
                        Logger.i(TAG, "#likeTimeline => onResponse");
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.i(TAG, "#likeTimeline => onErrorResponse");
                        errorListener.onErrorResponse(error);
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> map = new HashMap<>();
                map.put("timeline_id", timelineId);
                map.put("like_id", newLikeId);
                map.put("old_like_id", oldLikeId);
                map.put("type_like", typeLike);
                return map;
            }
        };
    }

    static GsonRequest<ResponseData> addComment(final String token, final int timelineId, final String comment, final Response.Listener<ResponseData> listener, final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        return new GsonJsonRequest<ResponseData>(Request.Method.POST,
                API_ADD_COMMENT,
                ResponseData.class,
                headers,
                new Response.Listener<ResponseData>() {

                    @Override
                    public void onResponse(ResponseData response) {
                        Logger.i(TAG, "#addComment => onResponse");
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.i(TAG, "#addComment => onErrorResponse");
                        errorListener.onErrorResponse(error);
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> map = new HashMap<>();
                map.put("timeline_id", timelineId);
                map.put("content", comment);
                return map;
            }
        };
    }

    static GsonRequest<ResponseData> likeComment(final String token, final int commentId, final int newLikeId, final int oldLikeId, final int typeLike, final Response.Listener<ResponseData> listener, final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        return new GsonJsonRequest<ResponseData>(Request.Method.POST,
                API_LIKE_COMMENT,
                ResponseData.class,
                headers,
                new Response.Listener<ResponseData>() {

                    @Override
                    public void onResponse(ResponseData response) {
                        Logger.i(TAG, "#likeTimeline => onResponse");
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.i(TAG, "#likeTimeline => onErrorResponse");
                        errorListener.onErrorResponse(error);
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> map = new HashMap<>();
                map.put("like_id", newLikeId);
                map.put("old_like_id", oldLikeId);
                map.put("comment_id", commentId);
                map.put("type_like", typeLike);
                return map;
            }
        };
    }

    static GsonRequest<ProfileResponse> getProfileStore(final String token, final int manageUserId, final Response.Listener<ProfileResponse> listener, final Response.ErrorListener errorListener) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        return new GsonJsonRequest<ProfileResponse>(Request.Method.POST,
                API_PROFILE_STORE,
                ProfileResponse.class,
                headers,
                new Response.Listener<ProfileResponse>() {

                    @Override
                    public void onResponse(ProfileResponse response) {
                        Logger.i(TAG, "#getTimeline => onResponse");
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.i(TAG, "#getTimeline => onErrorResponse");
                        errorListener.onErrorResponse(error);
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> map = new HashMap<>();
                map.put("manager_id", manageUserId);
                return map;
            }
        };
    }
}


