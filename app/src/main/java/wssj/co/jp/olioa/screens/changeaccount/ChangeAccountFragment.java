package wssj.co.jp.olioa.screens.changeaccount;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;

/**
 * Created by Nguyen Huu Ta on 5/7/2017.
 */

public class ChangeAccountFragment extends BaseFragment<IChangeAccountView, ChangeAccountPresenter>
        implements IChangeAccountView {

    private static final String TAG = "ChangeAccountFragment";

    private EditText mInputAccount, mInputNewPassword, mInputConfirmPassword;

    private TextView mButtonSave;


    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_CHANGE_ACCOUNT;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.change_account_fragment;
    }

    @Override
    public String getAppBarTitle() {
        return "アカウント登録";
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return false;
    }

    @Override
    protected ChangeAccountPresenter onCreatePresenter(IChangeAccountView view) {
        return new ChangeAccountPresenter(view);
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getNavigationMenuId() {
        return R.id.menu_change_password;
    }

    @Override
    protected IChangeAccountView onCreateView() {
        return this;
    }


    @Override
    protected void initViews(View view) {
        mInputAccount = view.findViewById(R.id.accountName);
        mInputNewPassword = view.findViewById(R.id.newPassword);
        mInputConfirmPassword = view.findViewById(R.id.confirmPassword);
        mButtonSave = view.findViewById(R.id.buttonSave);

    }

    @Override
    protected void initAction() {
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAccount();
            }
        });
    }

    @Override
    protected void initData() {

    }

    private void changeAccount(){
        String inputAccount = mInputAccount.getText().toString().trim();
        String password = mInputNewPassword.getText().toString().trim();
        String confirm = mInputConfirmPassword.getText().toString();
        getPresenter().changeAccount(inputAccount,password,confirm);
    }

}
