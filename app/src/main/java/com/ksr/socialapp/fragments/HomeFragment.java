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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ksr.socialapp.R;
import com.ksr.socialapp.adapter.PostAdapter;
import com.ksr.socialapp.adapter.StoryAdapter;
import com.ksr.socialapp.model.Post;
import com.ksr.socialapp.model.StoryModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    private RecyclerView storyRecyclerView , dashboardRecyclerView;
    private ArrayList<StoryModel> storyList;
    private ArrayList<Post> postArrayList;

    public HomeFragment() {
        //Required Public Constructor
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
        postArrayList = new ArrayList<>();

        PostAdapter postAdapter = new PostAdapter(postArrayList,getContext());
        LinearLayoutManager dasboardLlm =new LinearLayoutManager(getContext());
        dashboardRecyclerView.setLayoutManager(dasboardLlm);
        dashboardRecyclerView.setNestedScrollingEnabled(false);
        dashboardRecyclerView.setAdapter(postAdapter);

        firebaseDatabase.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Post post = dataSnapshot.getValue(Post.class);
                    post.setPostID(dataSnapshot.getKey());
                    postArrayList.add(post);
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;

    }
}
