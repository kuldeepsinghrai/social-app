package com.ksr.socialapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.ksr.socialapp.model.Post;
import com.ksr.socialapp.model.User;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Date;

public class AddFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;

    private Button postBT;
    private EditText postDescriptionET;
    private ImageView postImage, addImage;
    private RoundedImageView profileImage;
    private TextView name, profession;
    private Uri uri;

    private ProgressDialog dialog;

    public AddFragment() {
        //Required Empty Constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        dialog = new ProgressDialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        //dialog to show when post is uploading
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Post Uploading...");
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        postBT = view.findViewById(R.id.postBT);
        postDescriptionET = view.findViewById(R.id.postDescriptionET);
        postImage = view.findViewById(R.id.postImage);
        addImage = view.findViewById(R.id.addImage);
        profileImage = view.findViewById(R.id.profileImage);
        name = view.findViewById(R.id.name);
        profession = view.findViewById(R.id.profession);

        //getting current logedin user data (profile name about) & storing it in User model and seting them
        firebaseDatabase.getReference().child("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            User user = snapshot.getValue(User.class);
                            //Picasso.get().load(user.getProfile()).placeholder(R.drawable.placeholder).into(profileImage);
                            Glide.with(getContext()).load(user.getProfile()).placeholder(R.drawable.placeholder).into(profileImage);
                            name.setText(user.getName());
                            profession.setText(user.getProfession());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating intent for result to pick image from gallery
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 101);
            }
        });

        postBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uri != null) {
                    dialog.show();

                    //In storage, making a new directory "posts" where we making directories according to user id and inside that storing image as current time
                    final StorageReference storageReference = firebaseStorage.getReference().child("posts")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child(new Date().getTime() + "");


                    //when image is stored successfully, storing image data in Post model
                    storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Post post = new Post();
                                    post.setPostImage(uri.toString());
                                    post.setPostedBy(FirebaseAuth.getInstance().getUid());
                                    post.setPostDescription(postDescriptionET.getText().toString());
                                    post.setPostedAt(new Date().getTime());

                                    //creating new node of posts and storing all data from model in it
                                    firebaseDatabase.getReference().child("posts")
                                            .push()
                                            .setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            //navigating user to home screen when posted successfully
                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
                                            dialog.dismiss();
                                            Toast.makeText(getContext(), "Posted Successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    });

                }else {
                    Toast.makeText(getContext(), "PLease select photo!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //setting image to screen according to req
        if (requestCode == 101) {
            if (data != null) {
                uri = data.getData();
                postImage.setImageURI(uri);
                postBT.setEnabled(true);
            } else {
                postBT.setEnabled(false);
            }

        }
    }
}
