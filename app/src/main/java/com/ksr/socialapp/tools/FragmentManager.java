package com.ksr.socialapp.tools;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.ksr.socialapp.R;
import com.ksr.socialapp.fragments.AddFragment;
import com.ksr.socialapp.fragments.HomeFragment;
import com.ksr.socialapp.fragments.NotificationsFragment;
import com.ksr.socialapp.fragments.ProfileFragment;
import com.ksr.socialapp.fragments.SearchFragment;

public class FragmentManager implements androidx.fragment.app.FragmentManager.OnBackStackChangedListener {

    private androidx.fragment.app.FragmentManager fragmentManager;


    public FragmentManager(androidx.fragment.app.FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        this.fragmentManager.addOnBackStackChangedListener(this);

    }


    public void addFragment(Fragment fragment, int containerId, String fragmentName) {
        FragmentTransaction localFragmentTransaction = this.fragmentManager.beginTransaction();
        localFragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        localFragmentTransaction.add(containerId, fragment, fragmentName).addToBackStack(fragmentName).commit();

    }

    public void addFragment(Fragment fragment, int containerId, String fragmentName, boolean clearStack) {

        if (clearStack) {
            clearStack();
        }

        FragmentTransaction localFragmentTransaction = this.fragmentManager.beginTransaction();
        localFragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        localFragmentTransaction.replace(containerId, fragment, fragmentName).addToBackStack(fragmentName).commit();
    }


    public void clearStack() {
        for (int i = 0; i < this.fragmentManager.getBackStackEntryCount(); i++) {
            this.fragmentManager.popBackStackImmediate();
        }
    }


    @Override
    public void onBackStackChanged() {


    }


    public void replaceFragment(Fragment fragment, int containerId, String fragmentName, boolean clearStack) {
        if (clearStack) {
            clearStack();
        }

        FragmentTransaction localFragmentTransaction = this.fragmentManager.beginTransaction();
        localFragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        localFragmentTransaction.replace(containerId, fragment, fragmentName).commit();
    }


    public void openHomeFragment(boolean addToBackStack, boolean clearBackStack) {
        String fragmentTag = HomeFragment.class.getSimpleName();
        Fragment fragment = new HomeFragment();
        if (addToBackStack) {
            addFragment(fragment, R.id.container, fragmentTag, clearBackStack);
        } else {
            replaceFragment(fragment, R.id.container, fragmentTag, clearBackStack);
        }
    }


    public void openAddFragment(boolean addToBackStack, boolean clearBackStack) {
        String fragmentTag = AddFragment.class.getSimpleName();
        Fragment fragment = new AddFragment();
        if (addToBackStack) {
            addFragment(fragment, R.id.container, fragmentTag, clearBackStack);
        } else {
            replaceFragment(fragment, R.id.container, fragmentTag, clearBackStack);
        }
    }

    public void openNotificationsFragment(boolean addToBackStack, boolean clearBackStack) {
        String fragmentTag = NotificationsFragment.class.getSimpleName();
        Fragment fragment = new NotificationsFragment();
        if (addToBackStack) {
            addFragment(fragment, R.id.container, fragmentTag, clearBackStack);
        } else {
            replaceFragment(fragment, R.id.container, fragmentTag, clearBackStack);
        }
    }

    public void openProfileFragment(boolean addToBackStack, boolean clearBackStack) {
            String fragmentTag = ProfileFragment.class.getSimpleName();
            Fragment fragment = new ProfileFragment();
            if (addToBackStack) {
                addFragment(fragment, R.id.container, fragmentTag, clearBackStack);
            } else {
                replaceFragment(fragment, R.id.container, fragmentTag, clearBackStack);
            }
        }

    public void openSearchFragment(boolean addToBackStack, boolean clearBackStack) {
            String fragmentTag = SearchFragment.class.getSimpleName();
            Fragment fragment = new SearchFragment();
            if (addToBackStack) {
                addFragment(fragment, R.id.container, fragmentTag, clearBackStack);
            } else {
                replaceFragment(fragment, R.id.container, fragmentTag, clearBackStack);
            }
        }



}
