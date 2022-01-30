package com.ksr.socialapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ksr.socialapp.R;
import com.ksr.socialapp.adapter.FriendAdapter;
import com.ksr.socialapp.model.Friend;
import com.ksr.socialapp.model.User;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;

    private RecyclerView friendRecyclerView;
    private ArrayList<Friend> friendArrayList;
    private ImageView coverPhoto, changeCoverPhoto;
    private TextView userNameTV, professionTV;
    private RoundedImageView profileImage;

    public ProfileFragment() {
        //required empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        friendRecyclerView = view.findViewById(R.id.friendRecyclerView);
        userNameTV = view.findViewById(R.id.userNameTV);
        professionTV = view.findViewById(R.id.professionTV);
        coverPhoto = view.findViewById(R.id.coverPhoto);
        profileImage = view.findViewById(R.id.profileImage);
        changeCoverPhoto = view.findViewById(R.id.changeCoverPhoto);

        friendArrayList = new ArrayList<>();
        friendArrayList.add(new Friend(R.drawable.profile_pic));
        friendArrayList.add(new Friend(R.drawable.profile_pic));
        friendArrayList.add(new Friend(R.drawable.profile_pic));
        friendArrayList.add(new Friend(R.drawable.profile_pic));

        FriendAdapter friendAdapter = new FriendAdapter(friendArrayList, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        friendRecyclerView.setLayoutManager(linearLayoutManager);
        friendRecyclerView.setAdapter(friendAdapter);


        firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    Picasso.get().load(user.getCoverPhoto()).placeholder(R.drawable.placeholder).into(coverPhoto);
                    Picasso.get().load(user.getProfile()).placeholder(R.drawable.placeholder).into(profileImage);
                    userNameTV.setText(user.getName());
                    professionTV.setText(user.getProfession());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        changeCoverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 101);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 102);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (data.getData() != null) {
                Uri uri = data.getData();
                coverPhoto.setImageURI(uri);
                final StorageReference storageReference = firebaseStorage.getReference().child("cover_photo").child(FirebaseAuth.getInstance().getUid());
                storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Cover photo saved!", Toast.LENGTH_SHORT).show();
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid()).child("coverPhoto").setValue(uri.toString());

                            }
                        });
                    }
                });
            }

        }

        if (requestCode == 102) {

            if (data.getData() != null) {
                Uri uri = data.getData();
                profileImage.setImageURI(uri);
                final StorageReference storageReference = firebaseStorage.getReference().child("profile_image").child(FirebaseAuth.getInstance().getUid());
                storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Profile photo saved!", Toast.LENGTH_SHORT).show();
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid()).child("profile").setValue(uri.toString());

                            }
                        });
                    }
                });
            }
        }

    }

}

