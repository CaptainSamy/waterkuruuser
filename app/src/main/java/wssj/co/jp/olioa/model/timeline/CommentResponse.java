package wssj.co.jp.olioa.model.timeline;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import wssj.co.jp.olioa.model.GsonSerializable;
import wssj.co.jp.olioa.model.ResponseData;

/**
 * Created by Nguyen Huu Ta on 14/9/2017.
 */

public class CommentResponse extends ResponseData<CommentResponse.CommentData> {

    public class CommentData implements GsonSerializable {

        @SerializedName("list_comment")
        @Expose
        private List<ListComment> listComment;

        public class ListComment {

            @SerializedName("comment")
            @Expose
            public Comment comment;

            @SerializedName("self_like_id")
            @Expose
            public int mStatusLike;

            public class Comment {

                @SerializedName("content")
                @Expose
                private String content;

                @SerializedName("created")
                @Expose
                private long created;

                @SerializedName("id")
                @Expose
                private int id;

                @SerializedName("is_deleted")
                @Expose
                private int isDeleted;

                @SerializedName("key_gen")
                @Expose
                private String keyGen;

                @SerializedName("management_user_id")
                @Expose
                private int managementUserId;

                @SerializedName("modified")
                @Expose
                private long modified;

                @SerializedName("number_like")
                @Expose
                private int numberLike;

                @SerializedName("timeline_id")
                @Expose
                private int timelineId;

                @SerializedName("user_id")
                @Expose
                private int userId;

                @SerializedName("user_name")
                @Expose
                private String userName;

                @SerializedName("img_avatar")
                private String mImageAvatar;

                public String getContent() {
                    return content;
                }

                public long getCreated() {
                    return created;
                }

                public Integer getId() {
                    return id;
                }

                public Integer getIsDeleted() {
                    return isDeleted;
                }

                public String getKeyGen() {
                    return keyGen;
                }

                public int getManagementUserId() {
                    return managementUserId;
                }

                public long getModified() {
                    return modified;
                }

                public Integer getNumberLike() {
                    return numberLike;
                }

                public void setNumberLike(int numberLike) {
                    this.numberLike = numberLike;
                }

                public Integer getTimelineId() {
                    return timelineId;
                }

                public int getUserId() {
                    return userId;
                }

                public String getUserName() {
                    return userName;
                }

                public String getImageAvatar() {
                    return mImageAvatar;
                }
            }

            public Comment getComment() {
                return comment;
            }

            public int getStatusLike() {
                return mStatusLike;
            }

            public void setStatusLike(int statusLike) {
                this.mStatusLike = statusLike;
            }
        }

        public List<ListComment> getListComment() {
            return listComment;
        }
    }
}
