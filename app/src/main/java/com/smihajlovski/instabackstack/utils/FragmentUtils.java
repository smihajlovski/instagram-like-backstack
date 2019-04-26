package com.smihajlovski.instabackstack.utils;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.smihajlovski.instabackstack.ui.base.BaseFragment.FragmentInteractionCallback;

import java.util.Map;
import java.util.Stack;

import static com.smihajlovski.instabackstack.common.Constants.ACTION;
import static com.smihajlovski.instabackstack.common.Constants.DATA_KEY_1;
import static com.smihajlovski.instabackstack.common.Constants.DATA_KEY_2;

public class FragmentUtils {

    /*
     * Add the initial fragment, in most cases the first tab in BottomNavigationView
     */
    public static void addInitialTabFragment(FragmentManager fragmentManager,
                                             Map<String, Stack<Fragment>> stacks,
                                             String tag,
                                             Fragment fragment,
                                             int layoutId,
                                             boolean shouldAddToStack) {
        if (shouldAddToStack) stacks.get(tag).push(fragment);
        fragmentManager
                .beginTransaction()
                .add(layoutId, fragment)
                .commit();
    }

    /*
     * Add additional tab in BottomNavigationView on click, apart from the initial one and for the first time
     */
    public static void addAdditionalTabFragment(FragmentManager fragmentManager,
                                                Map<String, Stack<Fragment>> stacks,
                                                String tag,
                                                Fragment show,
                                                Fragment hide,
                                                int layoutId,
                                                boolean shouldAddToStack) {
        if (shouldAddToStack) stacks.get(tag).push(show);
        fragmentManager
                .beginTransaction()
                .add(layoutId, show)
                .show(show)
                .hide(hide)
                .commit();
    }

    /*
     * Hide previous and show current tab fragment if it has already been added
     * In most cases, when tab is clicked again, not for the first time
     */
    public static void showHideTabFragment(FragmentManager fragmentManager,
                                           Fragment show,
                                           Fragment hide) {
        fragmentManager
                .beginTransaction()
                .hide(hide)
                .show(show)
                .commit();
    }

    /*
     * Add fragment in the particular tab stack and show it, while hiding the one that was before
     */
    public static void addShowHideFragment(FragmentManager fragmentManager,
                                           Map<String, Stack<Fragment>> stacks,
                                           String tag,
                                           Fragment show,
                                           Fragment hide,
                                           int layoutId,
                                           boolean shouldAddToStack) {
        if (shouldAddToStack) stacks.get(tag).push(show);
        fragmentManager
                .beginTransaction()
                .add(layoutId, show)
                .show(show)
                .hide(hide)
                .commit();
    }

    public static void removeFragment(FragmentManager fragmentManager, Fragment show, Fragment remove) {
        fragmentManager
                .beginTransaction()
                .remove(remove)
                .show(show)
                .commit();
    }

    /*
     * Send action from fragment to activity
     */
    public static void sendActionToActivity(String action, String tab, boolean shouldAdd, FragmentInteractionCallback fragmentInteractionCallback) {
        Bundle bundle = new Bundle();
        bundle.putString(ACTION, action);
        bundle.putString(DATA_KEY_1, tab);
        bundle.putBoolean(DATA_KEY_2, shouldAdd);
        fragmentInteractionCallback.onFragmentInteractionCallback(bundle);
    }
}
