package jp.co.wssj.iungo.screens.timeline.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.timeline.TimeLineResponse;
import jp.co.wssj.iungo.widget.ExpandableListView;
import jp.co.wssj.iungo.widget.ExpandableTextView;

/**
 * Created by Nguyen Huu Ta on 13/9/2017.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineHolder> {

    private List<TimeLineResponse> mListTimeLine;

    public TimeLineAdapter(List<TimeLineResponse> listTimeLine) {
        mListTimeLine = listTimeLine;
    }

    public TimeLineResponse getItemTimeLine(int position) {
        if (mListTimeLine != null && mListTimeLine.size() > 0) {
            return mListTimeLine.get(position);
        }
        return null;
    }

    @Override
    public TimeLineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_time_line, parent, false);
        return new TimeLineHolder(context, view);
    }

    @Override
    public void onBindViewHolder(TimeLineHolder holder, int position) {
        TimeLineResponse response = getItemTimeLine(position);
        if (response != null) {
            holder.bind(getItemTimeLine(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return mListTimeLine.size();
    }

    public class TimeLineHolder extends RecyclerView.ViewHolder {

        private Context mContext;

        private ExpandableTextView mContent;

        private ImageView mImageSmile, mImageComment;

        private ExpandableListView mListViewComment;

        private RelativeLayout mLayoutComment;

        private TextView mNumberComment, mButtonSend;

        private EditText mInputComment;

        private LinearLayout mLayoutInput;

        private ProgressBar mProgressBar;

        private CommentAdapter adapter;

        public TimeLineHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            mNumberComment = (TextView) itemView.findViewById(R.id.tvNumberComment);
            mContent = (ExpandableTextView) itemView.findViewById(R.id.tvContent);
            mImageSmile = (ImageView) itemView.findViewById(R.id.ivSmile);
            mImageComment = (ImageView) itemView.findViewById(R.id.ivComment);
            mListViewComment = (ExpandableListView) itemView.findViewById(R.id.lvComment);
            mLayoutComment = (RelativeLayout) itemView.findViewById(R.id.layoutComment);
            mButtonSend = (TextView) itemView.findViewById(R.id.tvSendComment);
            mInputComment = (EditText) itemView.findViewById(R.id.etComment);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            mLayoutInput = (LinearLayout) itemView.findViewById(R.id.layoutInput);
        }

        public void bind(final TimeLineResponse response, final int position) {
            mContent.setText(response.getContent());
            mImageComment.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    RelativeLayout layout = (RelativeLayout) ((LinearLayout) v.getParent().getParent()).findViewById(R.id.layoutComment);
                    if (mLayoutComment.getVisibility() == View.GONE) {
                        mLayoutComment.setVisibility(View.VISIBLE);
                        if (response.getListComment() == null) {
                            mProgressBar.setVisibility(View.VISIBLE);
                            mLayoutInput.setVisibility(View.GONE);
                            response.setListComment(createListComment());
                            final String numberComment = mContent.getResources().getString(R.string.number_comment, String.valueOf(response.getListComment().size()));
                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    mLayoutInput.setVisibility(View.VISIBLE);
                                    mProgressBar.setVisibility(View.GONE);
                                    mNumberComment.setText(numberComment);
                                    adapter = new CommentAdapter(mContext, response.getListComment());
                                    mListViewComment.setAdapter(adapter);
                                }
                            }, 2000);

                        }
                    } else {
                        mLayoutComment.setVisibility(View.GONE);
                    }
                }
            });
            mButtonSend.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String commentString = mInputComment.getText().toString();
                    if (!TextUtils.isEmpty(commentString)) {
                        TimeLineResponse.Comment comment = new TimeLineResponse.Comment("Me", commentString, System.currentTimeMillis());
                        mListTimeLine.get(position).getListComment().add(comment);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    public List<TimeLineResponse.Comment> createListComment() {
        List<TimeLineResponse.Comment> listComment = new ArrayList<>();
        TimeLineResponse.Comment comment = new TimeLineResponse.Comment("Đại Kỵ Sỹ", "If we form the gradients using the above mentioned method and apply them to the element, we can get the required effect", System.currentTimeMillis());
        listComment.add(comment);
        comment = new TimeLineResponse.Comment("Màn Đêm", "I fine", System.currentTimeMillis());
        listComment.add(comment);
        comment = new TimeLineResponse.Comment("Only Love", "I Love You", System.currentTimeMillis());
        listComment.add(comment);
        return listComment;

    }
}
