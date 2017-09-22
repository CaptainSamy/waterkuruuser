package jp.co.wssj.iungo.screens.timeline.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.timeline.CommentResponse;
import jp.co.wssj.iungo.screens.comment.CommentPresenter;
import jp.co.wssj.iungo.screens.timeline.TimeLinePresenter;
import jp.co.wssj.iungo.utils.Logger;
import jp.co.wssj.iungo.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 13/9/2017.
 */

public class CommentAdapter extends ArrayAdapter<CommentResponse.CommentData.ListComment> {

    private LayoutInflater mInflate;

    private CommentPresenter mTimelinePresenter;

    private Context mContex;

    public CommentAdapter(@NonNull Context context, @NonNull List<CommentResponse.CommentData.ListComment> objects, CommentPresenter presenter) {
        super(context, 0, objects);
        mInflate = LayoutInflater.from(context);
        mTimelinePresenter = presenter;
        mContex = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CommentHolder commentHolder;
        if (convertView == null) {
            convertView = mInflate.inflate(R.layout.item_comment, parent, false);
            commentHolder = new CommentHolder(convertView);
            convertView.setTag(commentHolder);
        } else {
            commentHolder = (CommentHolder) convertView.getTag();
        }
        commentHolder.bind(getItem(position));
        return convertView;
    }

    public class CommentHolder {

        private TextView mContentComment, mTime, mLike, mLiked, mNumberLike;

        private RelativeLayout mLayoutLike;

        private LinearLayout mLayoutNumberLike;

        public CommentHolder(View view) {
            mContentComment = (TextView) view.findViewById(R.id.tvContentComment);
            mTime = (TextView) view.findViewById(R.id.tvTime);
            mLike = (TextView) view.findViewById(R.id.tvLike);
            mNumberLike = (TextView) view.findViewById(R.id.tvNumberLine);
            mLiked = (TextView) view.findViewById(R.id.tvUnlike);
            mLayoutLike = (RelativeLayout) view.findViewById(R.id.layoutLike);
            mLayoutNumberLike = (LinearLayout) view.findViewById(R.id.layoutNumberLike);
        }

        public void bind(final CommentResponse.CommentData.ListComment comments) {
            if (comments != null) {
                CommentResponse.CommentData.ListComment.Comment comment = comments.getComment();
                if (comment != null) {
                    String content = "<html><body><strong><font color='#3f51b5' font-weight: bold>" + comment.getUserName() + "</font></strong></body></html>" + " " + comment.getContent();
                    mContentComment.setText(Html.fromHtml(content));
                    mTime.setText(Utils.distanceTimes(comment.getCreated()));
                }
                mNumberLike.setText(String.valueOf(comment.getNumberLike()));
                int numberLike = comments.getComment().getNumberLike();
                if (numberLike > 0) {
                    mLayoutNumberLike.setVisibility(View.VISIBLE);
                    mNumberLike.setText(String.valueOf(numberLike));
                } else {
                    mLayoutNumberLike.setVisibility(View.GONE);
                }
                if (comments.getStatusLike() != 0) {
                    mLike.setVisibility(View.GONE);
                    mLiked.setVisibility(View.VISIBLE);
                } else {
                    mLike.setVisibility(View.VISIBLE);
                    mLiked.setVisibility(View.INVISIBLE);
                }
            }
            mLayoutLike.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final int isLike = comments.getStatusLike() == 0 ? 1 : 0;

                    if (mTimelinePresenter != null) {
                        mTimelinePresenter.likeComment(comments.getComment().getId(), 1, comments.getStatusLike(), isLike, new TimeLinePresenter.IOnLikeCallback() {

                            @Override
                            public void onLikeSuccess(String message) {
                                int numberLike;
                                if (comments.getStatusLike() == 0) {
                                    numberLike = comments.getComment().getNumberLike() + 1;
                                    mLike.setVisibility(View.GONE);
                                    mLiked.setVisibility(View.VISIBLE);
                                } else {
                                    numberLike = comments.getComment().getNumberLike() - 1;
                                    mLike.setVisibility(View.VISIBLE);
                                    mLiked.setVisibility(View.INVISIBLE);
                                }
                                if (numberLike > 0) {
                                    mLayoutNumberLike.setVisibility(View.VISIBLE);
                                    mNumberLike.setText(String.valueOf(numberLike));
                                } else {
                                    mLayoutNumberLike.setVisibility(View.GONE);
                                }
                                comments.setStatusLike(isLike);
                                Logger.d("onClick", "onLikeSuccess");
                            }

                            @Override
                            public void onLikeFailure(String message) {
                                Logger.d("onClick", "onLikeFailure");
                            }
                        });
                    }
                }
            });
        }
    }
}
