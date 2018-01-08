package jp.co.wssj.iungo.screens.screentest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.apiTest.PostResponse;
import jp.co.wssj.iungo.model.apiTest.TestResponse;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.about.IAboutView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.utils.Logger;

/**
 * Created by admin on 12/19/2017.
 */

public class ScreenTestFragment extends BaseFragment<IScreenTestView, ScreenTestPresenter> implements IScreenTestView, View.OnClickListener {

    private TextView txtScreenTest;
    private Button btnScreenTest, btnSendScreenTest;
    private static String TAG = "ScreenTestFragment";
    private List<PostResponse> postResponses;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_SCREEN_TEST;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_screen_test;
    }

    @Override
    protected ScreenTestPresenter onCreatePresenter(IScreenTestView view) {
        return new ScreenTestPresenter(view);
    }

    @Override
    protected IScreenTestView onCreateView() {
        return this;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.menu_version);
    }

    @Override
    public int getNavigationMenuId() {
        return R.id.menu_version;
    }

    @Override
    protected void initViews(View rootView) {
        txtScreenTest = (TextView) rootView.findViewById(R.id.textScreenTest);
        btnScreenTest = (Button) rootView.findViewById(R.id.buttonScreenTest);
        btnSendScreenTest = (Button) rootView.findViewById(R.id.buttonSendScreenTest);
    }

    @Override
    protected void initAction() {
        btnScreenTest.setOnClickListener(this);
        btnSendScreenTest.setOnClickListener(this);
        super.initAction();
    }

    @Override
    protected void initData() {
        super.initData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textScreenTest:
                break;
            case R.id.buttonSendScreenTest:
//                getActivityCallback().displayScreen(IMainView.FRAGMENT_ABOUT, true, true);
                Bundle bundle = new Bundle();
                bundle.putString("name", "Thắng");
                getActivityCallback().displayScreen(IMainView.FRAGMENT_ABOUT, true, true, bundle);
                break;
        }
    }

    @Override
    public void onGetSucces(String message) {

    }
}
