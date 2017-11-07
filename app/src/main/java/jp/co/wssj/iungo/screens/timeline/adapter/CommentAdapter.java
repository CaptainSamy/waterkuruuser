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
import android.widget.Toast;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.timeline.CommentResponse;
import jp.co.wssj.iungo.screens.comment.CommentPresenter;
import jp.co.wssj.iungo.screens.timeline.TimeLinePresenter;
import jp.co.wssj.iungo.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 13/9/2017.
 */

public class CommentAdapter extends ArrayAdapter<CommentResponse.CommentData.ListComment> {

    private LayoutInflater mInflate;

    private CommentPresenter mTimelinePresenter;

    private IOnClickAvatar mOnClickAvatar;

    public CommentAdapter(@NonNull Context context, @NonNull List<CommentResponse.CommentData.ListComment> objects, CommentPresenter presenter) {
        super(context, 0, objects);
        mInflate = LayoutInflater.from(context);
        mTimelinePresenter = presenter;
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

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public class CommentHolder {

        private TextView mContentComment, mTime, mLike, mLiked, mNumberLike;

        private RelativeLayout mLayoutLike;

        private LinearLayout mLayoutNumberLike;

        private CircleImageView mImageStore;

        private CommentResponse.CommentData.ListComment.Comment mComment;

        public CommentHolder(View view) {
            mContentComment = (TextView) view.findViewById(R.id.tvContentComment);
            mTime = (TextView) view.findViewById(R.id.tvTime);
            mLike = (TextView) view.findViewById(R.id.tvLike);
            mNumberLike = (TextView) view.findViewById(R.id.tvNumberLine);
            mLiked = (TextView) view.findViewById(R.id.tvUnlike);
            mLayoutLike = (RelativeLayout) view.findViewById(R.id.layoutLike);
            mLayoutNumberLike = (LinearLayout) view.findViewById(R.id.layoutNumberLike);
            mImageStore = (CircleImageView) view.findViewById(R.id.imageStore);
        }

        public void bind(final CommentResponse.CommentData.ListComment comments) {
            if (comments != null) {
                mComment = comments.getComment();
                if (mComment != null) {
                    String content = "<html><body><strong><font color='#3f51b5' font-weight: bold>" + mComment.getUserName() + "</font></strong></body></html>" + " " + StringEscapeUtils.unescapeJava(mComment.getContent());
                    mContentComment.setText(Html.fromHtml(content.replace("\n", "<br />")));
                    mTime.setText(Utils.distanceTimes(mComment.getCreated()));
                    mNumberLike.setText(String.valueOf(mComment.getNumberLike()));
                    Utils.fillImage(getContext(), mComment.getImageAvatar(), mImageStore, R.drawable.icon_user);
                    fillLikeComment(comments);
                    mLayoutLike.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            onLike(comments);
                        }
                    });
                    mImageStore.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (mOnClickAvatar != null) {
                                mOnClickAvatar.onClick(mComment.getManagementUserId());
                            }
                        }
                    });
                }
            }

        }

        private void fillLikeComment(CommentResponse.CommentData.ListComment comments) {
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

        private void onLike(CommentResponse.CommentData.ListComment comments) {
            if (comments != null) {
                final int isLike = comments.getStatusLike() == 0 ? 1 : 0;
                int statusLike = comments.getStatusLike();
                comments.setStatusLike(isLike);
                int numberLike;
                if (isLike == 1) {
                    numberLike = comments.getComment().getNumberLike() + 1;
                } else {
                    numberLike = comments.getComment().getNumberLike() - 1;
                }
                if (mComment != null) {
                    mComment.setNumberLike(numberLike);
                }
                fillLikeComment(comments);
                if (mTimelinePresenter != null) {
                    mTimelinePresenter.likeComment(comments.getComment().getId(), 1, statusLike, isLike, new TimeLinePresenter.IOnLikeCallback() {

                        @Override
                        public void onLikeSuccess(String message) {

                        }

                        @Override
                        public void onLikeFailure(String message) {
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }

    public interface IOnClickAvatar {

        void onClick(int managerId);
    }

    public void setOnClickAvatar(IOnClickAvatar mOnClickAvatar) {
        this.mOnClickAvatar = mOnClickAvatar;
    }
}
