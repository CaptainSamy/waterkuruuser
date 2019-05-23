package wssj.co.jp.obis.screens.timeline;

import wssj.co.jp.obis.model.timeline.TimeLineResponse;
import wssj.co.jp.obis.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 13/9/2017.
 */

public interface ITimeLineView extends IFragmentView {

    void onGetTimelineSuccess(TimeLineResponse.TimeLineData timeLineData);

    void onGetTimelineFailure(String message);

}
