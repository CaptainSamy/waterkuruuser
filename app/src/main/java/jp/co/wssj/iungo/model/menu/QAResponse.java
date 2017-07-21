package jp.co.wssj.iungo.model.menu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import jp.co.wssj.iungo.model.GsonSerializable;
import jp.co.wssj.iungo.model.ResponseData;

/**
 * Created by Nguyen Huu Ta on 6/7/2017.
 */

public class QAResponse extends ResponseData<QAResponse.ListQAData> {

    public class ListQAData implements GsonSerializable {

        @SerializedName("page")
        private int mPage;

        @SerializedName("total_page")
        private int mTotalPage;

        @SerializedName("limit")
        private int mLimit;

        @SerializedName("total_number_of_ques")
        private int mNumberOfQuestion;

        @SerializedName("number_of_ques_in_page")
        private int mNumberOfQuestionInPage;

        @SerializedName("list_titles")
        private List<QAData> mListQA;

        public class QAData {

            @SerializedName("title")
            private String mQuestion;

            @SerializedName("answer")
            private String mAnswer;

            public String getAnswer() {
                return mAnswer;
            }

            public String getQuestion() {
                return mQuestion;
            }
        }

        public int getTotalPage() {
            return mTotalPage;
        }

        public int getPage() {
            return mPage;
        }

        public int getNumberOfQuestion() {
            return mNumberOfQuestion;
        }

        public int getLimit() {
            return mLimit;
        }

        public int getNumberOfQuestionInPage() {
            return mNumberOfQuestionInPage;
        }

        public List<QAData> getListQA() {
            return mListQA;
        }
    }

}
