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

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ksr.socialapp.R;
import com.ksr.socialapp.adapter.UsersAdapter;
import com.ksr.socialapp.model.User;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private ArrayList<User> userArrayList = new ArrayList<>();
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private ShimmerRecyclerView usersRecyclerView;

    public SearchFragment() {
        //Required empty public constructer
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        usersRecyclerView = view.findViewById(R.id.usersRecyclerView);

        //showing simmer effect until data loads
        ShimmerRecyclerView shimmerSearchUsersRecycler = (ShimmerRecyclerView) view.findViewById(R.id.usersRecyclerView);
        shimmerSearchUsersRecycler.showShimmerAdapter();

        UsersAdapter usersAdapter = new UsersAdapter(getContext(),userArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        usersRecyclerView.setLayoutManager(linearLayoutManager);

        // getting all users of the app from database and showing them in the list
        firebaseDatabase.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userArrayList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    user.setUserID(dataSnapshot.getKey());
                    //excluding current logedin user from the list
                    if (!dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getUid())){
                        userArrayList.add(user);
                    }

                }
                usersAdapter.notifyDataSetChanged();
                //hiding shimmer adapter & showing our adapter when data is load
                shimmerSearchUsersRecycler.hideShimmerAdapter();
                usersRecyclerView.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;


    }
}
