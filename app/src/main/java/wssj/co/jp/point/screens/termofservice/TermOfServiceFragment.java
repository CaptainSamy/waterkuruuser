package wssj.co.jp.point.screens.termofservice;

import android.content.res.AssetManager;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import wssj.co.jp.point.R;
import wssj.co.jp.point.screens.IMainView;
import wssj.co.jp.point.screens.base.BaseFragment;

/**
 * Created by Nguyen Huu Ta on 5/6/2017.
 */

public class TermOfServiceFragment extends BaseFragment<ITermOfServicesView, TermOfServicePresenter> implements ITermOfServicesView {

    private static final String TAG = "TermOfServiceFragment";

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_TERM_OF_SERVICE;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_term_of_service);
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_term_of_service;
    }

    @Override
    protected TermOfServicePresenter onCreatePresenter(ITermOfServicesView view) {
        return new TermOfServicePresenter(view);
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return false;
    }

    @Override
    public boolean isDisplayExtraNavigationButton() {
        return false;
    }

    @Override
    public boolean isDisplayIconNotification() {
        return false;
    }

    @Override
    protected ITermOfServicesView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        super.initViews(rootView);
        mContentTermOfService = (TextView) rootView.findViewById(R.id.tvTermOfService);
    }

    private TextView mContentTermOfService;

    @Override
    protected void initAction() {
        super.initAction();
    }

    @Override
    protected void initData() {
        super.initData();

        String htmlFilename = "term_of_service.html";
        AssetManager mgr = getContext().getAssets();
        try {
            InputStream in = mgr.open(htmlFilename, AssetManager.ACCESS_BUFFER);
            String htmlContentInStringFormat = StreamToString(in);
            in.close();
            mContentTermOfService.setText(Html.fromHtml(htmlContentInStringFormat));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String StreamToString(InputStream in) throws IOException {
        if (in == null) {
            return "";
        }
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
        }
        return writer.toString();
    }
}
