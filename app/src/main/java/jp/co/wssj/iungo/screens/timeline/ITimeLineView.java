package jp.co.wssj.iungo.screens.timeline;

import jp.co.wssj.iungo.model.timeline.TimeLineResponse;
import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 13/9/2017.
 */

public interface ITimeLineView extends IFragmentView {

    void onGetTimelineSuccess(TimeLineResponse.TimeLineData timeLineData);

    void onGetTimelineFailure(String message);

}
