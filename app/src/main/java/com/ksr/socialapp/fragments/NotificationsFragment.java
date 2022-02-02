package com.ksr.socialapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ksr.socialapp.R;
import com.ksr.socialapp.adapter.NotificationAdapter;
import com.ksr.socialapp.model.Notification;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private ShimmerRecyclerView innerNotificationsRecyclerView;
    private ArrayList<Notification> notificationArrayList;
    private FirebaseDatabase firebaseDatabase;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        innerNotificationsRecyclerView = view.findViewById(R.id.innerNotificationsRecyclerView);


        ShimmerRecyclerView shimmerNotificationRecycler = (ShimmerRecyclerView) view.findViewById(R.id.innerNotificationsRecyclerView);
        shimmerNotificationRecycler.showShimmerAdapter();
        notificationArrayList = new ArrayList<>();


        NotificationAdapter notificationAdapter = new NotificationAdapter(notificationArrayList,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        innerNotificationsRecyclerView.setLayoutManager(linearLayoutManager);


        firebaseDatabase.getReference()
                .child("notification")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        notificationArrayList.clear();
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                            Notification notification = dataSnapshot.getValue(Notification.class);
                            notification.setNotificationId(dataSnapshot.getKey());
                            notificationArrayList.add(notification);
                        }
                        notificationAdapter.notifyDataSetChanged();
                        innerNotificationsRecyclerView.setAdapter(notificationAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        return view;
    }
}