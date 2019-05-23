package wssj.co.jp.obis.screens.comment;

import wssj.co.jp.obis.model.timeline.CommentResponse;
import wssj.co.jp.obis.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 21/9/2017.
 */

public interface ICommentView extends IFragmentView {

    void onGetListCommentSuccess(CommentResponse.CommentData commentData);

    void onGetListCommentFailure(String message);

    void onCommentSuccess(String message);

    void onCommentFailure(String message);
}
