package wssj.co.jp.olioa.screens.contact;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;
import wssj.co.jp.olioa.utils.Constants;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class ContactUsFragment extends BaseFragment<IContactUsView, ContactUsPresenter> implements IContactUsView {

    private static String TAG = "ContactUsFragment";

    private EditText mInputUserName, mInputUserEmail, mInputFeedback;

    private TextView mButtonFeedback;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_CONTACT_US;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_contact_us;
    }

    @Override
    protected ContactUsPresenter onCreatePresenter(IContactUsView view) {
        return new ContactUsPresenter(view);
    }

    @Override
    protected IContactUsView onCreateView() {
        return this;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_screen_contact);
    }

    @Override
    public int getNavigationMenuId() {
        return R.id.menu_contact_us;
    }

    @Override
    protected void initViews(View rootView) {
        super.initViews(rootView);
        mInputUserName = (EditText) rootView.findViewById(R.id.user_name);
        mInputUserEmail = (EditText) rootView.findViewById(R.id.email_user);
        mInputFeedback = (EditText) rootView.findViewById(R.id.inputFeedback);
        mButtonFeedback = (TextView) rootView.findViewById(R.id.buttonFeedback);
    }

    @Override
    protected void initData() {
        super.initData();
        getPresenter().getInfoUser();
    }

    @Override
    protected void initAction() {
        super.initAction();
        mButtonFeedback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String feedback = mInputFeedback.getText().toString();
                if (!TextUtils.isEmpty(feedback)) {
                    getPresenter().feedback(feedback);
                } else {
                    showToast(getString(R.string.content_feedback_not_empty));
                }
            }
        });
    }

    @Override
    public void getInfoUser(String userName, String email) {
        if (!TextUtils.isEmpty(userName)) {
            mInputUserName.setText(userName);
        }
        if (!TextUtils.isEmpty(email)) {
            mInputUserEmail.setText(email);
        }
    }

    @Override
    public void onFeedBackSuccess(String message) {
        showToast(message);
        mInputFeedback.setText(Constants.EMPTY_STRING);
    }

    @Override
    public void onFeedBackFailure(String message) {
        showToast(message);
    }
}
