package jp.co.wssj.iungo.utils;

import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.LinkedList;

/**
 * Created by hieup on 5/20/2017.
 */

public class FragmentBackStackManager {

    private int mEnterAnim, mExitAnim, mPopEnterAnim, mPopExitAnim;

    private final LinkedList<BackStack> mBackStacks = new LinkedList<>();

    private final FragmentManager mFragmentManager;

    private final int mContainerViewId;

    public FragmentBackStackManager(FragmentManager fragmentManager, @IdRes int containerViewId) {
        if (fragmentManager == null) {
            throw new IllegalArgumentException("FragmentManager must not be null");
        }
        mFragmentManager = fragmentManager;
        mContainerViewId = containerViewId;
    }

    public void clearBackStack() {
        for (BackStack backStack : mBackStacks) {
            if (backStack != null) {
                backStack.mIsAddToBackStack = false;
            }
        }
    }

    public void replaceFragment(Fragment fragment, boolean hasAnimation, boolean addToBackStack) {
        if (fragment != null) {
            String tag = String.valueOf(System.currentTimeMillis());
            mBackStacks.add(new BackStack(mFragmentManager.findFragmentById(mContainerViewId),
                    addToBackStack, tag));
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            if (hasAnimation) {
                ft.setCustomAnimations(mEnterAnim, mExitAnim, mPopEnterAnim, mPopExitAnim);
            }
            ft.replace(mContainerViewId, fragment, fragment.getClass().getName());
            ft.addToBackStack(tag);
            ft.commitAllowingStateLoss();
        }
    }

    public boolean popBackStackImmediate() {
        BackStack backStack;
        do {
            backStack = mBackStacks.pollLast();
        } while (backStack != null && !backStack.mIsAddToBackStack);
        return backStack != null && backStack.mFragment != null && mFragmentManager.popBackStackImmediate(backStack.mTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void setCustomAnimations(@AnimRes int enter,
                                    @AnimRes int exit) {
        setCustomAnimations(enter, exit, 0, 0);
    }

    public void setCustomAnimations(@AnimRes int enter,
                                    @AnimRes int exit, @AnimRes int popEnter, @AnimRes int popExit) {
        mEnterAnim = enter;
        mExitAnim = exit;
        mPopEnterAnim = popEnter;
        mPopExitAnim = popExit;
    }

    private class BackStack {

        private boolean mIsAddToBackStack;

        private final Fragment mFragment;

        private final String mTag;

        private BackStack(Fragment fragment, boolean isAddToBackStack, String tag) {
            mFragment = fragment;
            mIsAddToBackStack = isAddToBackStack;
            mTag = tag;
        }
    }
}
