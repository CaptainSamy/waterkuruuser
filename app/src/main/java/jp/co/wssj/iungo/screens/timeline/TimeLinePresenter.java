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

    public void getTimeline() {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel().getTimeLine(token, new TimelineModel.OnGetTimelineCallback() {

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

    public interface IOnLikeTimelineCallback {

        void onCommentSuccess(String message);

        void onCommentFailure(String message);
    }

    public void likeTimeline(int timelineId, int likeId, int typeLike, final IOnLikeTimelineCallback callback) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel().likeTimeline(token, timelineId, likeId, typeLike, new TimelineModel.OnLikeTimelineCallback() {

            @Override
            public void onLikeTimelineSuccess() {
                callback.onCommentSuccess(Constants.EMPTY_STRING);
            }

            @Override
            public void onLikeTimelineFailure(String message) {
                callback.onCommentFailure(message);
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
}
