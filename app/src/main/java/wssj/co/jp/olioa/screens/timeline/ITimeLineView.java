package wssj.co.jp.olioa.screens.timeline;

import wssj.co.jp.olioa.model.timeline.TimeLineResponse;
import wssj.co.jp.olioa.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 13/9/2017.
 */

public interface ITimeLineView extends IFragmentView {

    void onGetTimelineSuccess(TimeLineResponse.TimeLineData timeLineData);

    void onGetTimelineFailure(String message);

}
