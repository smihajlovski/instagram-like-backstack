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

    private static final String TAG_SEPARATOR = ":";

    /**
     * Add the initial fragment, in most cases the first tab in BottomNavigationView
     */
    public static void addInitialTabFragment(FragmentManager fragmentManager,
                                             Map<String, Stack<String>> tagStacks,
                                             String tag,
                                             Fragment fragment,
                                             int layoutId,
                                             boolean shouldAddToStack) {
        fragmentManager
                .beginTransaction()
                .add(layoutId, fragment, fragment.getClass().getName() + TAG_SEPARATOR + fragment.hashCode())
                .commit();
        if (shouldAddToStack) tagStacks.get(tag).push(fragment.getClass().getName() + TAG_SEPARATOR + fragment.hashCode());
    }

    /**
     * Add additional tab in BottomNavigationView on click, apart from the initial one and for the first time
     */
    public static void addAdditionalTabFragment(FragmentManager fragmentManager,
                                                Map<String, Stack<String>> tagStacks,
                                                String tag,
                                                Fragment show,
                                                Fragment hide,
                                                int layoutId,
                                                boolean shouldAddToStack) {
        fragmentManager
                .beginTransaction()
                .add(layoutId, show, show.getClass().getName() + TAG_SEPARATOR + show.hashCode())
                .show(show)
                .hide(hide)
                .commit();
        if (shouldAddToStack) tagStacks.get(tag).push(show.getClass().getName() + TAG_SEPARATOR + show.hashCode());
    }

    /**
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

    /**
     * Add fragment in the particular tab stack and show it, while hiding the one that was before
     */
    public static void addShowHideFragment(FragmentManager fragmentManager,
                                           Map<String, Stack<String>> tagStacks,
                                           String tag,
                                           Fragment show,
                                           Fragment hide,
                                           int layoutId,
                                           boolean shouldAddToStack) {
        fragmentManager
                .beginTransaction()
                .add(layoutId, show, show.getClass().getName() + TAG_SEPARATOR + show.hashCode())
                .show(show)
                .hide(hide)
                .commit();
        if (shouldAddToStack) tagStacks.get(tag).push(show.getClass().getName() + TAG_SEPARATOR + show.hashCode());
    }

    public static void removeFragment(FragmentManager fragmentManager, Fragment show, Fragment remove) {
        fragmentManager
                .beginTransaction()
                .remove(remove)
                .show(show)
                .commit();
    }

    /**
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
