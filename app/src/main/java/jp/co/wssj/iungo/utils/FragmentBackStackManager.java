package jp.co.wssj.iungo.utils;

import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;

import java.util.LinkedList;

import jp.co.wssj.iungo.BuildConfig;

/**
 * Created by hieup on 5/20/2017.
 */

public class FragmentBackStackManager {

    public static final String KEY_RETURNED_EXTRA_BUNDLE = BuildConfig.APPLICATION_ID + ".RETURNED_EXTRA_BUNDLE";

    private static final String TAG = "FragmentBackStackManager";

    private int mEnterAnim, mExitAnim, mPopEnterAnim, mPopExitAnim;

    private final LinkedList<BackStack> mBackStacks = new LinkedList<>();

    private final FragmentManager mFragmentManager;

    private final int mContainerViewId;

    public FragmentBackStackManager(@NonNull FragmentManager fragmentManager, @IdRes int containerViewId) {
        Utils.requireNonNull(fragmentManager, "FragmentManager must not be null");
        mFragmentManager = fragmentManager;
        mContainerViewId = containerViewId;
    }

    public void clearBackStack() {
        Logger.d(TAG, "#clearBackStack");
        mBackStacks.clear();
    }

    public void replaceFragment(Fragment fragment, boolean hasAnimation, boolean addToBackStack) {
        Logger.d(TAG, "#replaceFragment");
        if (fragment != null) {
            if (fragment.getArguments() == null) {
                fragment.setArguments(new Bundle());
            }
            Fragment currentFragment = mFragmentManager.findFragmentById(mContainerViewId);
            if (currentFragment != null && currentFragment.getArguments() != null) {
                currentFragment.getArguments().remove(KEY_RETURNED_EXTRA_BUNDLE);
            }
            String tag = String.valueOf(System.currentTimeMillis());
            mBackStacks.add(new BackStack(currentFragment, addToBackStack, tag));
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            if (hasAnimation) {
                ft.setCustomAnimations(mEnterAnim, mExitAnim, mPopEnterAnim, mPopExitAnim);
            }
            ft.replace(mContainerViewId, fragment, fragment.getClass().getName());
            ft.addToBackStack(tag);
            ft.commitAllowingStateLoss();
        }
    }

    public void replaceFragment(Fragment fragment, View sharedElement) {
        Logger.d(TAG, "#replaceFragmentSharedImage");
        if (fragment != null) {
            if (fragment.getArguments() == null) {
                fragment.setArguments(new Bundle());
            }
            Fragment currentFragment = mFragmentManager.findFragmentById(mContainerViewId);
            if (currentFragment != null && currentFragment.getArguments() != null) {
                currentFragment.getArguments().remove(KEY_RETURNED_EXTRA_BUNDLE);
            }
            String tag = String.valueOf(System.currentTimeMillis());
            mBackStacks.add(new BackStack(currentFragment, true, tag));
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.replace(mContainerViewId, fragment, fragment.getClass().getName());
            if (sharedElement != null) {
                ft.addSharedElement(sharedElement, ViewCompat.getTransitionName(sharedElement));
            }
            ft.addToBackStack(tag);
            ft.commitAllowingStateLoss();
        }
    }

    public boolean popBackStackImmediate() {
        Logger.d(TAG, "#popBackStackImmediate");
        BackStack backStack;
        do {
            backStack = mBackStacks.pollLast();
        } while (backStack != null && !backStack.mIsAddToBackStack);
        return backStack != null && backStack.mFragment != null && mFragmentManager.popBackStackImmediate(backStack.mTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public boolean popBackStackImmediate(Bundle returnedBundle) {
        if (returnedBundle == null) {
            return popBackStackImmediate();
        }
        Logger.d(TAG, "#popBackStackImmediateWithBundle");
        BackStack backStack;
        do {
            backStack = mBackStacks.pollLast();
        } while (backStack != null && !backStack.mIsAddToBackStack);
        if (backStack != null && backStack.mFragment != null) {
            Bundle baseBundle = backStack.mFragment.getArguments();
            if (baseBundle != null) {
                baseBundle.putBundle(KEY_RETURNED_EXTRA_BUNDLE, returnedBundle);
            }
            return mFragmentManager.popBackStackImmediate(backStack.mTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        return false;
    }

    public <F extends Fragment> boolean popBackStackImmediate(Class<F> clazz) {
        if (clazz != null) {
            BackStack entity = null;
            for (int i = mBackStacks.size() - 1; i >= 0; i--) {
                entity = mBackStacks.get(i);
                if (TextUtils.equals(entity.mFragment.getClass().getName(), clazz.getName()) && entity.mIsAddToBackStack) {
                    break;
                }
            }
            if (entity != null) {
                BackStack backStack;
                do {
                    backStack = mBackStacks.pollLast();
                } while (backStack != entity);
                return backStack.mFragment != null && mFragmentManager.popBackStackImmediate(backStack.mTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
        return false;
    }

    public void setCustomAnimations(@AnimRes int enter,
                                    @AnimRes int exit) {
        setCustomAnimations(enter, exit, 0, 0);
    }

    public void setCustomAnimations(@AnimRes int enter,
                                    @AnimRes int exit, @AnimRes int popEnter, @AnimRes int popExit) {
        Logger.d(TAG, "#setCustomAnimations");
        mEnterAnim = enter;
        mExitAnim = exit;
        mPopEnterAnim = popEnter;
        mPopExitAnim = popExit;
    }

    private static final class BackStack {

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
