package jp.co.wssj.iungo.model.timeline;

import java.util.List;

/**
 * Created by Nguyen Huu Ta on 13/9/2017.
 */

public class TimeLineResponse {

    public TimeLineResponse(String content) {
        mContent = content;
    }

    private int mId;

    private List<String> mListImage;

    private String mContent;

    private int mNumberLine;

    private long mTime;

    List<Comment> mListComment;

    public static class Comment {

        public Comment(String userName, String comment, long time) {
            mUserName = userName;
            mComment = comment;
            mTimeComment = time;
        }

        private String mUserName;

        private String mComment;

        private long mTimeComment;

        public String getUserName() {
            return mUserName;
        }

        public String getComment() {
            return mComment;
        }

        public long getTimeComment() {
            return mTimeComment;
        }
    }

    public int getId() {
        return mId;
    }

    public List<String> getListImage() {
        return mListImage;
    }

    public String getContent() {
        return mContent;
    }

    public int getNumberLine() {
        return mNumberLine;
    }

    public long getTime() {
        return mTime;
    }

    public List<Comment> getListComment() {
        return mListComment;
    }

    public void setListComment(List<Comment> listComment) {
        this.mListComment = listComment;
    }
}
