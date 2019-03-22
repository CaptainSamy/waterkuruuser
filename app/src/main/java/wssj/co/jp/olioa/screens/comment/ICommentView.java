package wssj.co.jp.olioa.screens.comment;

import wssj.co.jp.olioa.model.timeline.CommentResponse;
import wssj.co.jp.olioa.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 21/9/2017.
 */

public interface ICommentView extends IFragmentView {

    void onGetListCommentSuccess(CommentResponse.CommentData commentData);

    void onGetListCommentFailure(String message);

    void onCommentSuccess(String message);

    void onCommentFailure(String message);
}
