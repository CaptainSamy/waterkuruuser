package jp.co.wssj.iungo.screens.qa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.menu.QAResponse;
import jp.co.wssj.iungo.utils.Constants;

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

    private TextView mTextQuestion, mImageDrop;

    private WebView mTextAnswer;

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
        mTextAnswer = (WebView) convertView.findViewById(R.id.itemAnswer);
        WebSettings webSettings = mTextAnswer.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        mTextAnswer.loadDataWithBaseURL("", getChild(groupPosition, childPosition), "text/html", "UTF-8", "");
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