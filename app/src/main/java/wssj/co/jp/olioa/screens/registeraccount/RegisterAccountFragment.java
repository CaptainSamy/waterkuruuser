package wssj.co.jp.olioa.screens.registeraccount;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;
import wssj.co.jp.olioa.utils.Constants;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

public class RegisterAccountFragment extends BaseFragment<IRegisterAccountView, RegisterAccountPresenter>
        implements IRegisterAccountView, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "RegisterAccountFragment";

    private String[] mArrayAge = Constants.mArrayAge;

    private EditText mInputUserName, mInputUserId, mInputPassword, mInputConfirmPassword, mInputEmail;

    private TextView mTextRegisterAccount, mTermOfService;

    private TextView mTextUserNameError, mTextUserIdError, mTextEmailError, mTextPasswordError, mTextConfirmPasswordError;

    private Spinner mSpinnerAge;

    private RadioGroup mRadioGroupSex;

    private RadioButton mRadioMale, mRadioFemale;

    private int mAgeChoose, mSex;

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_REGISTER_ACCOUNT;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.register_account_fragment;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_register);
    }

    @Override
    public boolean isDisplayActionBar() {
        return true;
    }

    @Override
    public boolean isDisplayExtraNavigationButton() {
        return false;
    }

    @Override
    public boolean isEnableDrawableLayout() {
        return false;
    }

    @Override
    public boolean isDisplayIconNotification() {
        return false;
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return false;
    }

    @Override
    protected RegisterAccountPresenter onCreatePresenter(IRegisterAccountView view) {
        return new RegisterAccountPresenter(view);
    }

    @Override
    protected IRegisterAccountView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        mInputUserName = (EditText) rootView.findViewById(R.id.etUserName);
        mInputUserId = (EditText) rootView.findViewById(R.id.etUserId);
        mInputPassword = (EditText) rootView.findViewById(R.id.etPassword);
        mInputConfirmPassword = (EditText) rootView.findViewById(R.id.etConfirmPassword);
        mInputEmail = (EditText) rootView.findViewById(R.id.etEmail);
        mTextRegisterAccount = (TextView) rootView.findViewById(R.id.tvRegisterAccount);
        mTermOfService = (TextView) rootView.findViewById(R.id.tvTermOfService);
        mTextUserNameError = (TextView) rootView.findViewById(R.id.tvUserNameError);
        mTextUserIdError = (TextView) rootView.findViewById(R.id.tvUserIdError);
        mTextEmailError = (TextView) rootView.findViewById(R.id.tvEmailError);
        mTextPasswordError = (TextView) rootView.findViewById(R.id.tvPasswordError);
        mTextConfirmPasswordError = (TextView) rootView.findViewById(R.id.tvConfirmPasswordError);
        mSpinnerAge = (Spinner) rootView.findViewById(R.id.spAge);
        mRadioGroupSex = (RadioGroup) rootView.findViewById(R.id.radioGroupSex);
        mRadioMale = (RadioButton) rootView.findViewById(R.id.radioMale);
        mRadioFemale = (RadioButton) rootView.findViewById(R.id.radioFeMale);
    }

    @Override
    protected void initAction() {
        mTextRegisterAccount.setOnClickListener(this);
        mSpinnerAge.setOnItemSelectedListener(this);
        mRadioGroupSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == mRadioMale.getId()) {
                    mSex = Constants.Register.MALE;
                } else {
                    mSex = Constants.Register.FE_MALE;
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        initTermOfService();
        initAge();
        mSex = 1;

    }

    public void initAge() {
        List<String> list = Arrays.asList(mArrayAge);
        ArrayAdapter<String> mAdapterSpNumWord = new ArrayAdapter<String>(getActivityContext(), android.R.layout.simple_list_item_1, list) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (v instanceof TextView) {
                    ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                }
                return v;
            }
        };
        mSpinnerAge.setAdapter(mAdapterSpNumWord);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mAgeChoose = position + 1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initTermOfService() {
        String completeString = getString(R.string.term_of_service);
        String partToClick = getString(R.string.link);
        createLink(mTermOfService, completeString, partToClick,
                new ClickableSpan() {

                    @Override
                    public void onClick(View widget) {
                        //getActivityCallback().displayScreen(IMainView.FRAGMENT_TERM_OF_SERVICE_N0_BOTTOM, true, true);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        int linkColor = ContextCompat.getColor(getActivityContext(), R.color.blumine);
                        ds.setColor(linkColor);
                        ds.setUnderlineText(true);
                    }
                });
    }

    public TextView createLink(TextView targetTextView, String completeString,
                               String partToClick, ClickableSpan clickableAction) {
        SpannableString spannableString = new SpannableString(completeString);
        int startPosition = completeString.indexOf(partToClick);
        int endPosition = completeString.lastIndexOf(partToClick) + partToClick.length();

        spannableString.setSpan(clickableAction, startPosition, endPosition,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        targetTextView.setText(spannableString);
        targetTextView.setMovementMethod(LinkMovementMethod.getInstance());

        return targetTextView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRegisterAccount:
                hideTextViewError();
                String userName = mInputUserName.getText().toString().trim();
                String userId = mInputUserId.getText().toString().trim();
                String password = mInputPassword.getText().toString().trim();
                String confirmPassword = mInputConfirmPassword.getText().toString().trim();
                String email = mInputEmail.getText().toString().trim();
                //mAgeChoose = mAgeChoose * Constants.Register.MIN_AGE;
                getPresenter().onValidateInfoRegister(userName, userId, password, confirmPassword, email, mAgeChoose, mSex);
                break;
        }
    }

    public void hideTextViewError() {
        mTextUserNameError.setVisibility(View.GONE);
        mTextUserIdError.setVisibility(View.GONE);
        mTextEmailError.setVisibility(View.GONE);
        mTextPasswordError.setVisibility(View.GONE);
        mTextConfirmPasswordError.setVisibility(View.GONE);
    }

    public void showTextViewError(TextView textView, String message) {
        textView.setVisibility(View.VISIBLE);
        textView.setText(message);
    }

    @Override
    public void onValidateFailure(ErrorMessage errorMessage, int code) {
        if (errorMessage == null) return;
        switch (code) {
            case 1:
                showTextViewError(mTextUserNameError, errorMessage.getMessage());
                break;
            case 2:
                showTextViewError(mTextUserIdError, errorMessage.getMessage());
                break;
            case 3:
                showTextViewError(mTextEmailError, errorMessage.getMessage());
                break;
            case 4:
                showTextViewError(mTextPasswordError, errorMessage.getMessage());
                break;
            case 5:
                showTextViewError(mTextConfirmPasswordError, errorMessage.getMessage());
                break;
        }
    }

    @Override
    public void onRegisterSuccess() {
        String message = getString(R.string.register_success);
        showToast(message);
        getActivityCallback().clearBackStack();
        getActivityCallback().displayScreen(IMainView.FRAGMENT_GROUP_CHAT, true, false);
    }
}
