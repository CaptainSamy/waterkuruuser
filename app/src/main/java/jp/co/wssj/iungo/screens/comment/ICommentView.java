package jp.co.wssj.iungo.screens.comment;

import jp.co.wssj.iungo.model.timeline.CommentResponse;
import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 21/9/2017.
 */

public interface ICommentView extends IFragmentView {

    void onGetListCommentSuccess(CommentResponse.CommentData commentData);

    void onGetListCommentFailure(String message);

    void onCommentSuccess(String message);

    void onCommentFailure(String message);
}
