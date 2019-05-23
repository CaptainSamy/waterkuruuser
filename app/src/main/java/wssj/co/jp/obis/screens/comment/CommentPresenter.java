package wssj.co.jp.obis.screens.comment;

import wssj.co.jp.obis.model.timeline.CommentResponse;
import wssj.co.jp.obis.model.timeline.TimelineModel;
import wssj.co.jp.obis.model.preference.SharedPreferencesModel;
import wssj.co.jp.obis.screens.base.FragmentPresenter;
import wssj.co.jp.obis.screens.timeline.TimeLinePresenter;
import wssj.co.jp.obis.utils.Constants;

/**
 * Created by Nguyen Huu Ta on 21/9/2017.
 */

public class CommentPresenter extends FragmentPresenter<ICommentView> {

    protected CommentPresenter(ICommentView view) {
        super(view);
        registerModel(new TimelineModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
    }

    public void getListComment(int timelineId) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(TimelineModel.class).getListComment(token, timelineId, new TimelineModel.OnGetListCommentCallback() {

            @Override
            public void onGetListCommentSuccess(CommentResponse.CommentData commentData) {
                getView().onGetListCommentSuccess(commentData);
            }

            @Override
            public void onGetListCommentFailure(String message) {
                getView().onGetListCommentFailure(message);
            }
        });
    }

    public void addComment(int timelineId, String comment) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(TimelineModel.class).addComment(token, timelineId, comment, new TimelineModel.OnAddCommentCallback() {

            @Override
            public void onAddCommentSuccess() {
                getView().onCommentSuccess(Constants.EMPTY_STRING);
            }

            @Override
            public void onAddCommentFailure(String message) {
                getView().onCommentFailure(message);
            }
        });
    }

    public void likeComment(int commentId, int newLikeId, int oldLikeId, int typeLike, final TimeLinePresenter.IOnLikeCallback callback) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(TimelineModel.class).likeComment(token, commentId, newLikeId, oldLikeId, typeLike, new TimelineModel.OnLikeCallback() {

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
