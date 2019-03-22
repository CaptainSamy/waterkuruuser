package wssj.co.jp.olioa.screens.qa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.menu.QAResponse;
import wssj.co.jp.olioa.utils.Constants;

/**
 * Created by Nguyen Huu Ta on 5/7/2017.
 */

public class QAAdapter extends BaseAdapter {

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
    public int getCount() {
        if (mListQA == null) return 0;
        return mListQA.size();
    }

    @Override
    public String getItem(int position) {
        if (mListQA == null) return Constants.EMPTY_STRING;
        return mListQA.get(position).getQuestion();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private TextView mTextQuestion;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflate.inflate(R.layout.item_question, null);
        }
        mTextQuestion = (TextView) convertView.findViewById(R.id.itemQuestion);
        mTextQuestion.setText(getItem(position));
        if (mCallback != null) {
            if (position == (mListQA.size() - 1)) {
                mCallback.onEndOfListView();
            }
        }
        return convertView;
    }

    public interface IEndOfListView {

        void onEndOfListView();
    }
}