package wssj.co.jp.olioa.model.stamp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import wssj.co.jp.olioa.model.GsonSerializable;
import wssj.co.jp.olioa.model.ResponseData;

/**
 * Created by HieuPT on 6/1/2017.
 */

public class ListCardResponse extends ResponseData<ListCardResponse.ListCardData> {

    public class ListCardData implements GsonSerializable {

        @SerializedName("page")
        private int mPage;

        @SerializedName("total_page")
        private int mTotalPage;

        @SerializedName("limit")
        private int mLimit;

        @SerializedName("total_number_of_cards")
        private int mNumberOfCard;

        @SerializedName("number_of_stamps_in_card")
        private int mNumberStampInCard;

        @SerializedName("number_of_cards_in_page")
        private int mNumberOfCardInPage;

        @SerializedName("explanation_stamp")
        private String mExplaintionStamp;

        @SerializedName("cards")
        private List<CardData> mCards;

        public int getNumberOfCard() {
            return mNumberOfCard;
        }

        public List<CardData> getCards() {
            return mCards;
        }

        public int getPage() {
            return mPage;
        }

        public int getTotalPage() {
            return mTotalPage;
        }

        public int getNumberStampInCard() {
            return mNumberStampInCard;
        }

        public int getLimit() {
            return mLimit;
        }

        public int getNumberOfCardInPage() {
            return mNumberOfCardInPage;
        }

        public String getExplaintionStamp() {
            return mExplaintionStamp;
        }

        public class CardData implements GsonSerializable {

            @SerializedName("card_id")
            private int mCardId;

            @SerializedName("max_number_of_stamps")
            private int mMaxNumberOfStamp;

            @SerializedName("stamp")
            private List<StampData> mStamps;

            @SerializedName("card_status")
            private String mCardStatus;

            @SerializedName("current_number_of_stamps")
            private int mCurrentNumStamp;

            public int getCardId() {
                return mCardId;
            }

            public int getMaxNumberOfStamp() {
                return mMaxNumberOfStamp;
            }

            public List<StampData> getStamps() {
                return mStamps;
            }

            public String getCardStatus() {
                return mCardStatus;
            }

            public int getCurrentNumberOfStamp() {
                return mCurrentNumStamp;
            }

            public class StampData implements GsonSerializable {

                @SerializedName("stamp_id")
                private int mStampId;

                @SerializedName("management_user_id")
                private int mManagementUserId;

                @SerializedName("number_in_card")
                private int mNumberInCard;

                @SerializedName("store_name")
                private String mStoreName;

                @SerializedName("created")
                private long mCreated;

                @SerializedName("text_review")
                private String mContentRating;

                @SerializedName("star_review")
                private float mRating;

                public int getStampId() {
                    return mStampId;
                }

                public int getManagementUserId() {
                    return mManagementUserId;
                }

                public int getNumberInCard() {
                    return mNumberInCard;
                }

                public String getStoreName() {
                    return mStoreName;
                }

                public long getCreated() {
                    return mCreated;
                }

                public float getRating() {
                    return mRating;
                }

                public void setRating(float mRating) {
                    this.mRating = mRating;
                }

                public String getContentRating() {
                    return mContentRating;
                }

                public void setContentRating(String mContentRating) {
                    this.mContentRating = mContentRating;
                }


            }
        }
    }

}
