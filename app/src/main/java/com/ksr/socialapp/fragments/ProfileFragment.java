package com.ksr.socialapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.bumptech.glide.Glide;
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
import com.ksr.socialapp.activities.LoginActivity;
import com.ksr.socialapp.adapter.FollowersAdapter;
import com.ksr.socialapp.model.Follow;
import com.ksr.socialapp.model.User;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;

    private RecyclerView friendRecyclerView;
    private ArrayList<Follow> friendArrayList;
    private ImageView coverPhoto, changeCoverPhoto, logOutBT;
    private TextView userNameTV, professionTV, followers;
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
        followers = view.findViewById(R.id.followers);
        logOutBT = view.findViewById(R.id.logOutBT);

        friendArrayList = new ArrayList<>();


        FollowersAdapter friendAdapter = new FollowersAdapter(friendArrayList, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        friendRecyclerView.setLayoutManager(linearLayoutManager);
        friendRecyclerView.setAdapter(friendAdapter);

        //getting followers info of the user from database and storing them in Follow model and adding in list
        firebaseDatabase.getReference().child("Users")
                .child(firebaseAuth.getUid())
                .child("Followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Follow follow = dataSnapshot.getValue(Follow.class);
                    friendArrayList.add(follow);
                }
                friendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // getting user data from database, storing it in User model and  getting user's detail from it and setting all of it
        firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    Glide.with(getContext()).load(user.getCoverPhoto()).placeholder(R.drawable.placeholder).into(coverPhoto);
                    Glide.with(getContext()).load(user.getProfile()).placeholder(R.drawable.placeholder).into(profileImage);
                    userNameTV.setText(user.getName());
                    professionTV.setText(user.getProfession());
                    followers.setText(user.getFollowerCount() + "");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //showing alert when user click on logout button and signing out & navigating to Login Activity if user selects yes
        logOutBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Confirm Logout..!!!");
                alertDialogBuilder.setMessage("Are you sure, You want to logout?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        firebaseAuth.signOut();
                        getActivity().finish();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });


        changeCoverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating intent for result to pick image from gallery
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 101);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            //creating intent for result to pick image from gallery
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

        /*
        * setting image to the profile or cover according to req code
        * making directories of cover_photo or profile_image and storing image as logedin user's id
        * when the image is successfully uploaded, we are storing image url to our FirebaseDatabase in Users>{User id of the logedin user}>{coverPhoto or profile} and set value of that uri
        */

        if (requestCode == 101) {
            if (data != null) {
                Uri uri = data.getData();
                coverPhoto.setImageURI(uri);
                final StorageReference storageReference = firebaseStorage.getReference().child("cover_photo").child(FirebaseAuth.getInstance().getUid());
                storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Cover photo saved!", Toast.LENGTH_SHORT).show();
                        //storing image url to our database in Users>{User id of the logedin user}>{coverPhoto}
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
            if (data != null) {
                Uri uri = data.getData();
                profileImage.setImageURI(uri);
                final StorageReference storageReference = firebaseStorage.getReference().child("profile_image").child(FirebaseAuth.getInstance().getUid());
                storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Profile photo saved!", Toast.LENGTH_SHORT).show();
                        //storing image url to our database in Users>{User id of the logedin user}>{profile}
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

