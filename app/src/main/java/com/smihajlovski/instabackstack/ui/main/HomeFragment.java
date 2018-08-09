package com.smihajlovski.instabackstack.ui.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smihajlovski.instabackstack.R;
import com.smihajlovski.instabackstack.databinding.FragmentHomeBinding;
import com.smihajlovski.instabackstack.ui.base.BaseFragment;
import com.smihajlovski.instabackstack.utils.FragmentUtils;

import static com.smihajlovski.instabackstack.common.Constants.HOME_FRAGMENT;

public class HomeFragment extends BaseFragment {

    public static final String ACTION_DASHBOARD = HOME_FRAGMENT + "action.dashboard";
    private FragmentHomeBinding binder;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        init();
        return binder.getRoot();
    }

    private void init() {
        binder.button.setOnClickListener(v -> FragmentUtils.sendActionToActivity(ACTION_DASHBOARD, currentTab, true, fragmentInteractionCallback));
    }
}
