package com.ksr.socialapp.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ksr.socialapp.fragments.InnerNotificationsFragment;
import com.ksr.socialapp.fragments.RequestsFragment;

public class NotificationsVeiwPagerAdapter extends FragmentPagerAdapter {

    public NotificationsVeiwPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new InnerNotificationsFragment();

            case 1:
                return new RequestsFragment();
            default:
                return new InnerNotificationsFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position==0){
            title="NOTIFICATIONS";
        }else if (position==1){
            title="REQUESTS";
        }
        return title;
    }
}
