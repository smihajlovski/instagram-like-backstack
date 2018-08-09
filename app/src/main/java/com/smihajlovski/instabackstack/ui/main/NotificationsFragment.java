package com.smihajlovski.instabackstack.ui.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smihajlovski.instabackstack.R;
import com.smihajlovski.instabackstack.databinding.FragmentNotificationsBinding;
import com.smihajlovski.instabackstack.ui.base.BaseFragment;

import static com.smihajlovski.instabackstack.common.Constants.NOTIFICATIONS_FRAGMENT;
import static com.smihajlovski.instabackstack.common.Constants.TAB_NOTIFICATIONS;
import static com.smihajlovski.instabackstack.utils.FragmentUtils.sendActionToActivity;

public class NotificationsFragment extends BaseFragment {

    public static final String ACTION_DASHBOARD = NOTIFICATIONS_FRAGMENT + "action.dashboard";
    private FragmentNotificationsBinding binder;

    public NotificationsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_notifications, container, false);
        init();
        return binder.getRoot();
    }

    private void init() {
        binder.button.setOnClickListener(v -> sendActionToActivity(ACTION_DASHBOARD, currentTab, true, fragmentInteractionCallback));
    }
}
