package jp.co.wssj.iungo.screens.timeline;

import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
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

    public String getUserName() {
        return getModel(SharedPreferencesModel.class).getUserName();
    }

}
