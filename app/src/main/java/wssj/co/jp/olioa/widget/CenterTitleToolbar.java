package wssj.co.jp.olioa.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.utils.Constants;

/**
 * Created by HieuPT on 5/15/2017.
 */

public class CenterTitleToolbar extends Toolbar {

    private TextView mTitleTextView;

    private ImageButton mExtraNavigationButton;

    private ImageView mIconNotification;

    private TextView mCountNotification;

    private CharSequence mTitleText;

    private RelativeLayout mLayoutNotification;

    public CenterTitleToolbar(Context context) {
        this(context, null);
    }

    public CenterTitleToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.toolbarStyle);
    }

    public CenterTitleToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundResource(R.color.colorBackground_Actionbar);
        inflate(context, R.layout.action_bar_title, this);
        inflate(context, R.layout.action_bar_extra_navigation_button, this);
        initView();
    }

    private void initView() {
        mTitleTextView = (TextView) findViewById(R.id.action_bar_title);
        mExtraNavigationButton = (ImageButton) findViewById(R.id.action_bar_extra_button);
        mIconNotification = (ImageView) findViewById(R.id.iconNotification);
        mCountNotification = (TextView) findViewById(R.id.countNotification);
        mLayoutNotification = (RelativeLayout) findViewById(R.id.layoutNotification);
        mLayoutNotification.setVisibility(GONE);
        setTitle(Constants.EMPTY_STRING);
    }

    public void setTitleActionBar(CharSequence title) {
        mTitleText = title;
        mTitleTextView.setText(title);
    }

    public CharSequence getTitleActionBar() {
        return mTitleText;
    }

    public void setShowExtraNavigationButton(boolean isShown) {
        mExtraNavigationButton.setVisibility(isShown ? VISIBLE : GONE);
    }

    public void setShowIconNotificationButton(boolean isShown) {
        mLayoutNotification.setVisibility(GONE);
    }

    public boolean isExtraNavigationButtonShown() {
        return mExtraNavigationButton.getVisibility() == VISIBLE;
    }

    public void setExtraNavigationOnClickListener(OnClickListener listener) {
        mExtraNavigationButton.setOnClickListener(listener);
    }

    public void setIconNotificationClickListener(OnClickListener listener) {
        mLayoutNotification.setOnClickListener(listener);
    }

    public void setExtraNavigationIcon(@DrawableRes int resId) {
        setExtraNavigationIcon(ContextCompat.getDrawable(getContext(), resId));
    }

    public void setExtraNavigationIcon(Drawable drawable) {
        mExtraNavigationButton.setImageDrawable(drawable);
    }

    public ImageView getIconNotification() {
        return mIconNotification;
    }

    public void setNumberNotificationUnRead(int number) {
        if (number > 0) {
            mCountNotification.setVisibility(VISIBLE);
            String text = String.valueOf(number);
            if (number > 99) {
                number = 99;
                text = number + "<sup><font size=6>+</font></sup>";
            }
            mCountNotification.setTextSize(8f);
            mCountNotification.setText(Html.fromHtml(text));
        } else {
            mCountNotification.setVisibility(GONE);
        }
    }

    public void setShowNumberNotification(boolean isShow) {
        mCountNotification.setVisibility(isShow ? VISIBLE : GONE);
    }
}
