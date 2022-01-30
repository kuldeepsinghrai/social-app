package com.ksr.socialapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
    private ImageView coverPhoto, changeCoverPhoto;

    public ProfileFragment() {
        //required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        friendRecyclerView = view.findViewById(R.id.friendRecyclerView);
        coverPhoto = view.findViewById(R.id.coverPhoto);
        changeCoverPhoto = view.findViewById(R.id.changeCoverPhoto);

        friendArrayList = new ArrayList<>();
        friendArrayList.add(new Friend(R.drawable.profile_pic));
        friendArrayList.add(new Friend(R.drawable.profile_pic));
        friendArrayList.add(new Friend(R.drawable.profile_pic));
        friendArrayList.add(new Friend(R.drawable.profile_pic));

        FriendAdapter friendAdapter = new FriendAdapter(friendArrayList,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        friendRecyclerView.setLayoutManager(linearLayoutManager);
        friendRecyclerView.setAdapter(friendAdapter);

        changeCoverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,101);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData()!=null){
            Uri uri = data.getData();
            coverPhoto.setImageURI(uri);
        }
    }
}
