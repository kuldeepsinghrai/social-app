package com.ksr.socialapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ksr.socialapp.R;
import com.ksr.socialapp.adapter.NotificationAdapter;
import com.ksr.socialapp.model.Notification;

import java.util.ArrayList;

public class InnerNotificationsFragment extends Fragment {

    private RecyclerView innerNotificationsRecyclerView;
    private ArrayList<Notification> notificationArrayList;

    public InnerNotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inner_notifications, container, false);

        innerNotificationsRecyclerView = view.findViewById(R.id.innerNotificationsRecyclerView);

        notificationArrayList = new ArrayList<>();
        notificationArrayList.add(new Notification(R.drawable.placeholder,"<b>Kuldeep Singh Rai</b> Comment on your photo","Just Now"));
        notificationArrayList.add(new Notification(R.drawable.placeholder,"<b>Manpreet Singh</b> liked your photo","1 min"));
        notificationArrayList.add(new Notification(R.drawable.placeholder,"<b>Sonu</b> sent you a request","30 min"));
        notificationArrayList.add(new Notification(R.drawable.placeholder,"Kuldeep Singh Rai Comment on your photo","35 min"));
        notificationArrayList.add(new Notification(R.drawable.placeholder,"Manpreet Singh liked your photo","40 min"));
        notificationArrayList.add(new Notification(R.drawable.placeholder,"Sonu sent you a request","1 hr"));
        notificationArrayList.add(new Notification(R.drawable.placeholder,"Kuldeep Singh Rai Comment on your photo","2 hr"));
        notificationArrayList.add(new Notification(R.drawable.placeholder,"Manpreet Singh liked your photo","2 hr"));
        notificationArrayList.add(new Notification(R.drawable.placeholder,"Sonu sent you a request","2 hr"));
        notificationArrayList.add(new Notification(R.drawable.placeholder,"Kuldeep Singh Rai Comment on your photo","2 hr"));
        notificationArrayList.add(new Notification(R.drawable.placeholder,"Manpreet Singh liked your photo","2 hr"));
        notificationArrayList.add(new Notification(R.drawable.placeholder,"Sonu sent you a request","3 hr"));

        NotificationAdapter notificationAdapter = new NotificationAdapter(notificationArrayList,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        innerNotificationsRecyclerView.setLayoutManager(linearLayoutManager);
        innerNotificationsRecyclerView.setAdapter(notificationAdapter);



        return view;
    }
}