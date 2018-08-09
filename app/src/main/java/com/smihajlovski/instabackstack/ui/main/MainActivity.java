package com.smihajlovski.instabackstack.ui.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.smihajlovski.instabackstack.R;
import com.smihajlovski.instabackstack.databinding.ActivityMainBinding;
import com.smihajlovski.instabackstack.ui.base.BaseFragment;
import com.smihajlovski.instabackstack.ui.base.BaseFragment.FragmentInteractionCallback;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static com.smihajlovski.instabackstack.common.Constants.ACTION;
import static com.smihajlovski.instabackstack.common.Constants.DATA_KEY_1;
import static com.smihajlovski.instabackstack.common.Constants.DATA_KEY_2;
import static com.smihajlovski.instabackstack.common.Constants.TAB_DASHBOARD;
import static com.smihajlovski.instabackstack.common.Constants.TAB_HOME;
import static com.smihajlovski.instabackstack.common.Constants.TAB_NOTIFICATIONS;
import static com.smihajlovski.instabackstack.utils.FragmentUtils.addAdditionalTabFragment;
import static com.smihajlovski.instabackstack.utils.FragmentUtils.addInitialTabFragment;
import static com.smihajlovski.instabackstack.utils.FragmentUtils.addShowHideFragment;
import static com.smihajlovski.instabackstack.utils.FragmentUtils.removeFragment;
import static com.smihajlovski.instabackstack.utils.FragmentUtils.showHideTabFragment;
import static com.smihajlovski.instabackstack.utils.StackListManager.updateStackIndex;
import static com.smihajlovski.instabackstack.utils.StackListManager.updateStackToIndexFirst;
import static com.smihajlovski.instabackstack.utils.StackListManager.updateTabStackIndex;

public class MainActivity extends AppCompatActivity implements FragmentInteractionCallback {

    private Map<String, Stack<Fragment>> stacks;
    private String currentTab;
    private ActivityMainBinding binder;
    private List<String> stackList;
    private List<String> menuStacks;
    private Fragment currentFragment;
    private Fragment homeFragment;
    private Fragment dashboardFragment;
    private Fragment notificationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_main);
        createStacks();
    }

    @Override
    public void onFragmentInteractionCallback(Bundle bundle) {
        String action = bundle.getString(ACTION);

        if (action != null) {
            switch (action) {
                case HomeFragment.ACTION_DASHBOARD:
                    showFragment(bundle, new DashboardFragment());
                    break;
                case DashboardFragment.ACTION_NOTIFICATION:
                    showFragment(bundle, new NotificationsFragment());
                    break;
                case NotificationsFragment.ACTION_DASHBOARD:
                    showFragment(bundle, new DashboardFragment());
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        resolveBackPressed();
    }

    private void createStacks() {
        binder.bottomNavigationView.inflateMenu(R.menu.bottom_nav_tabs);
        binder.bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        homeFragment = new HomeFragment();
        dashboardFragment = new DashboardFragment();
        notificationFragment = new NotificationsFragment();

        stacks = new LinkedHashMap<>();
        stacks.put(TAB_HOME, new Stack<>());
        stacks.put(TAB_DASHBOARD, new Stack<>());
        stacks.put(TAB_NOTIFICATIONS, new Stack<>());

        menuStacks = new ArrayList<>();
        menuStacks.add(TAB_HOME);

        stackList = new ArrayList<>();
        stackList.add(TAB_HOME);
        stackList.add(TAB_DASHBOARD);
        stackList.add(TAB_NOTIFICATIONS);

        binder.bottomNavigationView.setSelectedItemId(R.id.tab_home);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.tab_home:
                selectedTab(TAB_HOME);
                return true;
            case R.id.tab_dashboard:
                selectedTab(TAB_DASHBOARD);
                return true;
            case R.id.tab_notifications:
                selectedTab(TAB_NOTIFICATIONS);
                return true;
        }
        return false;
    };

    private void selectedTab(String tabId) {

        currentTab = tabId;
        BaseFragment.setCurrentTab(currentTab);

        if (stacks.get(tabId).size() == 0) {
            /*
             * First time this tab is selected. So add first fragment of that tab.
             * We are adding a new fragment which is not present in stack. So add to stack is true.
             */
            switch (tabId) {
                case TAB_HOME:
                    addInitialTabFragment(getSupportFragmentManager(), stacks, TAB_HOME, homeFragment, R.id.frame_layout, true);
                    resolveStackLists(tabId);
                    assignCurrentFragment(homeFragment);
                    break;
                case TAB_DASHBOARD:
                    addAdditionalTabFragment(getSupportFragmentManager(), stacks, TAB_DASHBOARD, dashboardFragment, currentFragment, R.id.frame_layout, true);
                    resolveStackLists(tabId);
                    assignCurrentFragment(dashboardFragment);
                    break;
                case TAB_NOTIFICATIONS:
                    addAdditionalTabFragment(getSupportFragmentManager(), stacks, TAB_NOTIFICATIONS, notificationFragment, currentFragment, R.id.frame_layout, true);
                    resolveStackLists(tabId);
                    assignCurrentFragment(notificationFragment);
                    break;
            }
        } else {
            /*
             * We are switching tabs, and target tab already has at least one fragment.
             * Show the target fragment
             */
            showHideTabFragment(getSupportFragmentManager(), stacks.get(tabId).lastElement(), currentFragment);
            resolveStackLists(tabId);
            assignCurrentFragment(stacks.get(tabId).lastElement());
        }
    }

    private void popFragment() {
        /*
         * Select the second last fragment in current tab's stack,
         * which will be shown after the fragment transaction given below
         */
        Fragment fragment = stacks.get(currentTab).elementAt(stacks.get(currentTab).size() - 2);

        /*pop current fragment from stack */
        stacks.get(currentTab).pop();

        removeFragment(getSupportFragmentManager(), fragment, currentFragment);

        assignCurrentFragment(fragment);
    }

    private void resolveBackPressed() {
        int stackValue = 0;
        if (stacks.get(currentTab).size() == 1) {
            Stack<Fragment> value = stacks.get(stackList.get(1));
            if (value.size() > 1) {
                stackValue = value.size();
                popAndNavigateToPreviousMenu();
            }
            if (stackValue <= 1) {
                if (menuStacks.size() > 1) {
                    navigateToPreviousMenu();
                } else {
                    finish();
                }
            }
        } else {
            popFragment();
        }
    }

    /*Pops the last fragment inside particular tab and goes to the second tab in the stack*/
    private void popAndNavigateToPreviousMenu() {
        String tempCurrent = stackList.get(0);
        currentTab = stackList.get(1);
        BaseFragment.setCurrentTab(currentTab);
        binder.bottomNavigationView.setSelectedItemId(resolveTabPositions(currentTab));
        showHideTabFragment(getSupportFragmentManager(), stacks.get(currentTab).lastElement(), currentFragment);
        assignCurrentFragment(stacks.get(currentTab).lastElement());
        updateStackToIndexFirst(stackList, tempCurrent);
        menuStacks.remove(0);
    }

    private void navigateToPreviousMenu() {
        menuStacks.remove(0);
        currentTab = menuStacks.get(0);
        BaseFragment.setCurrentTab(currentTab);
        binder.bottomNavigationView.setSelectedItemId(resolveTabPositions(currentTab));
        showHideTabFragment(getSupportFragmentManager(), stacks.get(currentTab).lastElement(), currentFragment);
        assignCurrentFragment(stacks.get(currentTab).lastElement());
    }

    /*
     * Add a fragment to the stack of a particular tab
     */
    private void showFragment(Bundle bundle, Fragment fragmentToAdd) {
        String tab = bundle.getString(DATA_KEY_1);
        boolean shouldAdd = bundle.getBoolean(DATA_KEY_2);
        addShowHideFragment(getSupportFragmentManager(), stacks, tab, fragmentToAdd, getCurrentFragmentFromShownStack(), R.id.frame_layout, shouldAdd);
        assignCurrentFragment(fragmentToAdd);
    }

    private int resolveTabPositions(String currentTab) {
        int tabIndex = 0;
        switch (currentTab) {
            case TAB_HOME:
                tabIndex = R.id.tab_home;
                break;
            case TAB_DASHBOARD:
                tabIndex = R.id.tab_dashboard;
                break;
            case TAB_NOTIFICATIONS:
                tabIndex = R.id.tab_notifications;
                break;
        }
        return tabIndex;
    }

    private void resolveStackLists(String tabId) {
        updateStackIndex(stackList, tabId);
        updateTabStackIndex(menuStacks, tabId);
    }

    private Fragment getCurrentFragmentFromShownStack() {
        return stacks.get(currentTab).elementAt(stacks.get(currentTab).size() - 1);
    }

    private void assignCurrentFragment(Fragment current) {
        currentFragment = current;
    }
}
