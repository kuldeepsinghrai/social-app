package com.ksr.socialapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ksr.socialapp.R;
import com.ksr.socialapp.adapter.NotificationsVeiwPagerAdapter;

public class NotificationFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    public NotificationFragment() {
        //Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification,container,false);

        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new NotificationsVeiwPagerAdapter(getFragmentManager()));

        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        return view;



    }
}
