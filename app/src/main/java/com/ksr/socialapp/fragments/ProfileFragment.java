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
import com.ksr.socialapp.adapter.FriendAdapter;
import com.ksr.socialapp.model.Friend;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private RecyclerView friendRecyclerView;
    private ArrayList<Friend> friendArrayList;

    public ProfileFragment() {
        //required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        friendRecyclerView = view.findViewById(R.id.friendRecyclerView);

        friendArrayList = new ArrayList<>();
        friendArrayList.add(new Friend(R.drawable.profile_pic));
        friendArrayList.add(new Friend(R.drawable.profile_pic));
        friendArrayList.add(new Friend(R.drawable.profile_pic));
        friendArrayList.add(new Friend(R.drawable.profile_pic));

        FriendAdapter friendAdapter = new FriendAdapter(friendArrayList,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        friendRecyclerView.setLayoutManager(linearLayoutManager);
        friendRecyclerView.setAdapter(friendAdapter);

        return view;

    }
}
