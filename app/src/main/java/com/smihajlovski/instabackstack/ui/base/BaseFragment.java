package com.smihajlovski.instabackstack.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

    protected FragmentInteractionCallback fragmentInteractionCallback;
    protected static String currentTab;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragmentInteractionCallback = (FragmentInteractionCallback) context;
        } catch (ClassCastException e) {
            throw new RuntimeException(context.toString() + " must implement " + FragmentInteractionCallback.class.getName());
        }
    }

    @Override
    public void onDetach() {
        fragmentInteractionCallback = null;
        super.onDetach();
    }

    public interface FragmentInteractionCallback {

        void onFragmentInteractionCallback(Bundle bundle);
    }

    public static void setCurrentTab(String currentTab) {
        BaseFragment.currentTab = currentTab;
    }
}
