package wssj.co.jp.point.screens.qa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wssj.co.jp.point.R;
import wssj.co.jp.point.model.menu.QAResponse;
import wssj.co.jp.point.utils.Constants;

/**
 * Created by Nguyen Huu Ta on 5/7/2017.
 */

public class QAAdapter extends BaseExpandableListAdapter {

    private static final String TAG = "QAAdapter";

    private List<QAResponse.ListQAData.QAData> mListQA;

    private Context mContext;

    private LayoutInflater mInflate;

    private IEndOfListView mCallback;

    public QAAdapter(Context context, List<QAResponse.ListQAData.QAData> listData, IEndOfListView callback) {
        mCallback = callback;
        mContext = context;
        mInflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (listData != null) {
            mListQA = listData;
        } else {
            mListQA = new ArrayList<>();
        }
    }

    @Override
    public int getGroupCount() {
        return mListQA.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public String getGroup(int groupPosition) {
        if (mListQA != null && mListQA.size() > 0) {
            return mListQA.get(groupPosition).getQuestion();
        }
        return Constants.EMPTY_STRING;
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        if (mListQA != null && mListQA.size() > 0) {
            return mListQA.get(groupPosition).getAnswer();
        }
        return Constants.EMPTY_STRING;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private TextView mTextQuestion, mTextAnswer, mImageDrop;

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflate.inflate(R.layout.item_question, null);
        }
        mTextQuestion = (TextView) convertView.findViewById(R.id.itemQuestion);
        mImageDrop = (TextView) convertView.findViewById(R.id.iconDrop);
        if (isExpanded) {
            mImageDrop.setBackground(mContext.getResources().getDrawable(R.drawable.drop_up));
        } else {
            mImageDrop.setBackground(mContext.getResources().getDrawable(R.drawable.drop_down));
        }
        mTextQuestion.setText(getGroup(groupPosition));
        if (mCallback != null) {
            if (groupPosition == (mListQA.size() - 1)) {
                mCallback.onEndOfListView();
            }
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflate.inflate(R.layout.item_answer, null);
        }
        mTextAnswer = (TextView) convertView.findViewById(R.id.itemAnswer);
        mTextAnswer.setText(getChild(groupPosition, childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public interface IEndOfListView {

        void onEndOfListView();
    }
}