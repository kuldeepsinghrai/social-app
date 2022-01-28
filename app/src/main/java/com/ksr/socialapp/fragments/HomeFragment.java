package com.ksr.socialapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ksr.socialapp.R;
import com.ksr.socialapp.adapter.DashboardAdapter;
import com.ksr.socialapp.adapter.StoryAdapter;
import com.ksr.socialapp.model.Dashboard;
import com.ksr.socialapp.model.StoryModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView storyRecyclerView , dashboardRecyclerView;
    ArrayList<StoryModel> storyList;
    ArrayList<Dashboard> dashboardList;

    public HomeFragment() {
        //Required Public Constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        storyRecyclerView = view.findViewById(R.id.storiesRecyclerView);
        storyList = new ArrayList<>();
        storyList.add(new StoryModel(R.drawable.profile_pic,"Aryan"));
        storyList.add(new StoryModel(R.drawable.profile_pic,"Sonu"));
        storyList.add(new StoryModel(R.drawable.profile_pic,"Manpreet"));
        storyList.add(new StoryModel(R.drawable.profile_pic,"Manpreet"));
        storyList.add(new StoryModel(R.drawable.profile_pic,"Manpreet"));


        StoryAdapter storyAdapter = new StoryAdapter(storyList,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        storyRecyclerView.setLayoutManager(linearLayoutManager);
        storyRecyclerView.setNestedScrollingEnabled(false);
        storyRecyclerView.setAdapter(storyAdapter);


        dashboardRecyclerView = view.findViewById(R.id.dashboardRecyclerView);
        dashboardList = new ArrayList<>();
        dashboardList.add(new Dashboard(R.drawable.profile_pic,R.drawable.profile_pic,"Kuldeep Singh", "Android Developer", "30","4","2"));
        dashboardList.add(new Dashboard(R.drawable.profile_pic,R.drawable.profile_pic,"Manpreet Singh", "Android Tester", "35","4","2"));

        DashboardAdapter dashboardAdapter = new DashboardAdapter(dashboardList,getContext());
        LinearLayoutManager dasboardLlm =new LinearLayoutManager(getContext());
        dashboardRecyclerView.setLayoutManager(dasboardLlm);
        dashboardRecyclerView.setNestedScrollingEnabled(false);
        dashboardRecyclerView.setAdapter(dashboardAdapter);

        return view;

    }
}
