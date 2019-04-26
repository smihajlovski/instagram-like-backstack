package com.smihajlovski.instabackstack.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.smihajlovski.instabackstack.R;
import com.smihajlovski.instabackstack.databinding.FragmentHomeBinding;
import com.smihajlovski.instabackstack.ui.base.BaseFragment;
import com.smihajlovski.instabackstack.utils.FragmentUtils;

import static com.smihajlovski.instabackstack.common.Constants.EXTRA_IS_ROOT_FRAGMENT;
import static com.smihajlovski.instabackstack.common.Constants.HOME_FRAGMENT;

public class HomeFragment extends BaseFragment {

    static final String ACTION_DASHBOARD = HOME_FRAGMENT + "action.dashboard";
    private FragmentHomeBinding binder;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(boolean isRoot) {
        Bundle args = new Bundle();
        args.putBoolean(EXTRA_IS_ROOT_FRAGMENT, isRoot);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
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
