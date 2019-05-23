package wssj.co.jp.obis.screens.timeline;

import wssj.co.jp.obis.model.timeline.TimeLineResponse;
import wssj.co.jp.obis.model.timeline.TimelineModel;
import wssj.co.jp.obis.model.preference.SharedPreferencesModel;
import wssj.co.jp.obis.model.util.UtilsModel;
import wssj.co.jp.obis.screens.base.FragmentPresenter;
import wssj.co.jp.obis.utils.Constants;

/**
 * Created by Nguyen Huu Ta on 13/9/2017.
 */

public class TimeLinePresenter extends FragmentPresenter<ITimeLineView> {

    public TimeLinePresenter(ITimeLineView view) {
        super(view);
        registerModel(new TimelineModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new UtilsModel(view.getViewContext()));

    }

    private TimelineModel getModel() {
        return getModel(TimelineModel.class);
    }

    public void getTimeline(int lastTimelineId) {

        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel().getTimeLine(token, lastTimelineId, new TimelineModel.OnGetTimelineCallback() {

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

    public void getTimelineDetail(int managerUserId, int lastTimelineId) {

        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel().getTimeLineDetail(token, managerUserId, lastTimelineId, new TimelineModel.OnGetTimelineCallback() {

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
