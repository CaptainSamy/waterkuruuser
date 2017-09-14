package jp.co.wssj.iungo.screens.timeline.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.timeline.TimeLineResponse;
import jp.co.wssj.iungo.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 13/9/2017.
 */

public class CommentAdapter extends ArrayAdapter<TimeLineResponse.Comment> {

    private LayoutInflater mInflate;

    public CommentAdapter(@NonNull Context context, @NonNull List<TimeLineResponse.Comment> objects) {
        super(context, 0, objects);
        mInflate = LayoutInflater.from(context);
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

        private TextView mContentComment, mTime;

        public CommentHolder(View view) {

            mContentComment = (TextView) view.findViewById(R.id.tvContentComment);
            mTime = (TextView) view.findViewById(R.id.tvTime);
        }

        public void bind(TimeLineResponse.Comment comment) {

            String content = "<html><body><strong><font color='#3f51b5' font-weight: bold>" + comment.getUserName() + "</font></strong></body></html>" + " " + comment.getComment();
            mContentComment.setText(Html.fromHtml(content));
            mTime.setText(Utils.distanceTimesViet(comment.getTimeComment()));
        }
    }
}
