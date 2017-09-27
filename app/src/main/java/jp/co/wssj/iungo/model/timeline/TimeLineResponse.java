package jp.co.wssj.iungo.model.timeline;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import jp.co.wssj.iungo.model.GsonSerializable;
import jp.co.wssj.iungo.model.ResponseData;

/**
 * Created by Nguyen Huu Ta on 13/9/2017.
 */

public class TimeLineResponse extends ResponseData<TimeLineResponse.TimeLineData> {

    public static class TimeLineData implements GsonSerializable {

        @SerializedName("number_of_timeline_in_page")
        @Expose
        private int numberOfTimelineInPage;

        @SerializedName("page")
        @Expose
        private int page;

        @SerializedName("total_number_of_timeline")
        @Expose
        private int totalNumberOfTimeline;

        @SerializedName("total_page")
        @Expose
        private int totalPage;

        @SerializedName("limit")
        @Expose
        private String limit;

        @SerializedName("list_timeline")
        @Expose
        private List<ListTimeline> listTimeline = null;

        public static class ListTimeline {

            @SerializedName("likes")
            @Expose
            private List<Like> likes;

            @SerializedName("timeline")
            @Expose
            private Timeline timeline;

            public class Timeline {

                @SerializedName("comment_number")
                @Expose
                private int commentNumber;

                @SerializedName("created")
                @Expose
                private long created;

                @SerializedName("id")
                @Expose
                private int id;

                @SerializedName("images")
                @Expose
                private String images;

                @SerializedName("img_store")
                private String imageStore;

                @SerializedName("is_deleted")
                @Expose
                private String isDeleted;

                @SerializedName("key_gen")
                @Expose
                private String keyGen;

                @SerializedName("management_user_id")
                @Expose
                private String managementUserId;

                @SerializedName("messages")
                @Expose
                private String messages;

                @SerializedName("modified")
                @Expose
                private long modified;

                @SerializedName("number_like")
                @Expose
                private int numberLike;

                @SerializedName("self_like_id")
                @Expose
                private int statusLikeId;

                @SerializedName("manager_name")
                @Expose
                private String managerName;

                public int getCommentNumber() {
                    return commentNumber;
                }

                public void setCommentNumber(int number) {
                    commentNumber = number;
                }

                public long getCreated() {
                    return created;
                }

                public int getId() {
                    return id;
                }

                public String getImages() {
                    return images;
                }

                public String getImageStore() {
                    return imageStore;
                }

                public String getIsDeleted() {
                    return isDeleted;
                }

                public String getKeyGen() {
                    return keyGen;
                }

                public String getManagementUserId() {
                    return managementUserId;
                }

                public String getMessages() {
                    return messages;
                }

                public long getModified() {
                    return modified;
                }

                public int getNumberLike() {
                    return numberLike;
                }

                public void setNumberLike(int numberLike) {
                    this.numberLike = numberLike;
                }

                public int getStatusLikeId() {
                    return statusLikeId;
                }

                public void setStatusLikeId(int statusLikeId) {
                    this.statusLikeId = statusLikeId;
                }

                public String getManagerName() {
                    return managerName;
                }
            }

            public static class Like {

                public Like(int likeId) {
                    this.likeId = likeId;
                }

                @SerializedName("count_like")
                @Expose
                private String countLike;

                @SerializedName("created")
                @Expose
                private String created;

                @SerializedName("id")
                @Expose
                private String id;

                @SerializedName("key_gen")
                @Expose
                private String keyGen;

                @SerializedName("like_id")
                @Expose
                private int likeId;

                @SerializedName("list_user_like")
                @Expose
                private List<String> listUserLike = null;

                @SerializedName("modified")
                @Expose
                private String modified;

                @SerializedName("timeline_id")
                @Expose
                private String timelineId;

                public String getCountLike() {
                    return countLike;
                }

                public String getCreated() {
                    return created;
                }

                public String getId() {
                    return id;
                }

                public String getKeyGen() {
                    return keyGen;
                }

                public int getLikeId() {
                    return likeId;
                }

                public List<String> getListUserLike() {
                    return listUserLike;
                }

                public String getModified() {
                    return modified;
                }

                public String getTimelineId() {
                    return timelineId;
                }

            }

            public List<Like> getLikes() {
                return likes;
            }

            public Timeline getTimeline() {
                return timeline;
            }

        }

        public int getNumberOfTimelineInPage() {
            return numberOfTimelineInPage;
        }

        public int getPage() {
            return page;
        }

        public int getTotalNumberOfTimeline() {
            return totalNumberOfTimeline;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public String getLimit() {
            return limit;
        }

        public List<ListTimeline> getListTimeline() {
            return listTimeline;
        }

    }
}
