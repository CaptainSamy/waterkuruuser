package jp.co.wssj.iungo.screens.timeline;

import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.model.timeline.CommentResponse;
import jp.co.wssj.iungo.model.timeline.TimeLineResponse;
import jp.co.wssj.iungo.model.timeline.TimelineModel;
import jp.co.wssj.iungo.model.util.UtilsModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;
import jp.co.wssj.iungo.utils.Constants;

/**
 * Created by Nguyen Huu Ta on 13/9/2017.
 */

public class TimeLinePresenter extends FragmentPresenter<ITimeLineView> {

    protected TimeLinePresenter(ITimeLineView view) {
        super(view);
        registerModel(new TimelineModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new UtilsModel(view.getViewContext()));

    }

    private TimelineModel getModel() {
        return getModel(TimelineModel.class);
    }

    public void getTimeline(int page) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel().getTimeLine(token, page, new TimelineModel.OnGetTimelineCallback() {

            @Override
            public void onGetTimelineSuccess(TimeLineResponse.TimeLineData timeLineData) {
                getView().onGetTimelineSuccess(timeLineData);
            }

            @Override
            public void onGetTimelineFailure(String message) {
                getView().onGetTimelineFailure(message);
            }
        });
    }

    public interface IOnGetListCommentCallback {

        void onGetListCommentSuccess(CommentResponse.CommentData commentData);

        void onGetListCommentFailure(String message);
    }

    public void getListComment(int timelineId, final IOnGetListCommentCallback callback) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel().getListComment(token, timelineId, new TimelineModel.OnGetListCommentCallback() {

            @Override
            public void onGetListCommentSuccess(CommentResponse.CommentData commentData) {
                callback.onGetListCommentSuccess(commentData);
            }

            @Override
            public void onGetListCommentFailure(String message) {
                callback.onGetListCommentFailure(message);
            }
        });
    }

    public interface IOnCommentCallback {

        void onCommentSuccess(String message);

        void onCommentFailure(String message);
    }

    public void addComment(int timelineId, String comment, final IOnCommentCallback callback) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel().addComment(token, timelineId, comment, new TimelineModel.OnAddCommentCallback() {

            @Override
            public void onAddCommentSuccess() {
                callback.onCommentSuccess(Constants.EMPTY_STRING);
            }

            @Override
            public void onAddCommentFailure(String message) {
                callback.onCommentFailure(message);
            }
        });
    }

    public interface IOnLikeCallback {

        void onLikeSuccess(String message);

        void onLikeFailure(String message);
    }

    public void likeTimeline(int timelineId, int newLikeId, int oldLikeId, int typeLike, final IOnLikeCallback callback) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel().likeTimeline(token, timelineId, newLikeId, oldLikeId, typeLike, new TimelineModel.OnLikeCallback() {

            @Override
            public void onLikeSuccess() {
                callback.onLikeSuccess(Constants.EMPTY_STRING);
            }

            @Override
            public void onLikeFailure(String message) {
                callback.onLikeFailure(message);
            }
        });
    }

    public void likeComment(int commentId, int newLikeId, int oldLikeId, int typeLike, final IOnLikeCallback callback) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel().likeComment(token, commentId, newLikeId, oldLikeId, typeLike, new TimelineModel.OnLikeCallback() {

            @Override
            public void onLikeSuccess() {
                callback.onLikeSuccess(Constants.EMPTY_STRING);
            }

            @Override
            public void onLikeFailure(String message) {
                callback.onLikeFailure(message);
            }
        });
    }
}
