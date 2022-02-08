package com.ksr.socialapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.ksr.socialapp.adapter.CreatePostImageAdapter;
import com.ksr.socialapp.adapter.FollowersAdapter;
import com.ksr.socialapp.model.Post;
import com.ksr.socialapp.model.User;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class AddFragment extends Fragment implements OnSuccessListener<UploadTask.TaskSnapshot>, OnFailureListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;

    private Button postBT;
    private EditText postDescriptionET;
    private ImageView postImage, addImage;
    private RecyclerView postImageRecyclerView;
    private RoundedImageView profileImage;
    private TextView name, profession;
    //private Uri uri;

    private ArrayList<CustomImage> imagesList;
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
        postImageRecyclerView = view.findViewById(R.id.postImageRecyclerView);

        imagesList = new ArrayList<>();

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
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select File "), 101);
            }
        });

        postBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imagesList != null) {
                    dialog.show();
                    startUploadingImages();
                } else {
                    Toast.makeText(getContext(), "PLease select photo!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;


    }

    private void startUploadingImages() {

        CustomImage firstImage = imagesList.get(0);
        uploadSingleImage(firstImage);

        //In storage, making a new directory "posts" where we making directories according to user id and inside that storing image as current time

        /*for (int i = 0; i < imagesList.size(); i++) {
            CustomImage customImage = imagesList.get(i);

            if (!customImage.isUploaded()) {
                //when image is stored successfully, storing image data in Post model
                storageReference.child(String.valueOf(i + 1)).putFile(customImage.getUri()).addOnSuccessListener(AddFragment.this).addOnFailureListener(AddFragment.this);
            }
        }*/

    }

    private CustomImage getNextFileForUpload() {
        for (int i = 0; i < imagesList.size(); i++) {
            CustomImage customImage = imagesList.get(i);
            if (!customImage.isUploaded()) {
                return customImage;
            }
        }
        return null;
    }

    private void uploadSingleImage(CustomImage customImage) {
        if (customImage == null)
            return;
        inProcessing = customImage;
        customImage.storageReference.putFile(customImage.getUri()).addOnSuccessListener(AddFragment.this).addOnFailureListener(AddFragment.this);

    }

    private boolean hasAllFilesUploaded() {
        boolean uploaded = true;
        for (int i = 0; i < imagesList.size(); i++) {
            CustomImage customImage = imagesList.get(i);
            if (!customImage.isUploaded()) {
                uploaded = false;
                break;
            }
        }
        return uploaded;
    }

    private CustomImage inProcessing;

    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


        if (inProcessing != null) {
            inProcessing.setUploaded(true);
        }
        CustomImage customImage = getNextFileForUpload();
        if (customImage != null) {
            uploadSingleImage(customImage);
        } else {


            if (hasAllFilesUploaded()) {
                //Create Post and upload

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (dialog != null && dialog.isShowing())
                            dialog.dismiss();

                        List<String> iL = new ArrayList<>();

                        for (int i = 0; i < imagesList.size(); i++) {
                            CustomImage ci = imagesList.get(i);
                            if (ci.getUrl() != null)
                                iL.add(ci.getUrl());
                        }
                        createPost(iL);
                    }
                }, 5000);

            } else {
                //Error

                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();

            }
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {

        if (dialog != null && dialog.isShowing())
            dialog.dismiss();


    }


    private void createPost(List<String> imagesUrl) {
        Post post = new Post();
        //post.setPostImage(uri.toString());
        post.setPostedBy(FirebaseAuth.getInstance().getUid());
        post.setPostDescription(postDescriptionET.getText().toString());
        post.setPostedAt(new Date().getTime());

        //List<String> pImgList = new ArrayList<>();
        //pImgList.add("https://images.pexels.com/photos/992734/pexels-photo-992734.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");
        //pImgList.add("https://images.pexels.com/photos/992734/pexels-photo-992734.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");
        //pImgList.add("https://images.pexels.com/photos/992734/pexels-photo-992734.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");

        if (imagesUrl != null && !imagesUrl.isEmpty())
            post.setPostImages(imagesUrl);

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

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //setting image to screen according to req
        if (requestCode == 101) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                final StorageReference storageReference = firebaseStorage.getReference().child("posts")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child(new Date().getTime() + "");

                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    String fName = getRandomString();
                    CustomImage customImage = new CustomImage(storageReference.child(fName), imageUri, fName);
                    imagesList.add(customImage);
                }


                CreatePostImageAdapter createPostImageAdapter = new CreatePostImageAdapter(imagesList, getContext());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                postImageRecyclerView.setLayoutManager(linearLayoutManager);
                postImageRecyclerView.setAdapter(createPostImageAdapter);
                postBT.setEnabled(true);

//                uri = data.getData();
//                postImage.setImageURI(uri);

            } else {
                postBT.setEnabled(false);
            }

        }
    }


    protected String getRandomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }


    public class CustomImage {
        private StorageReference storageReference;
        String fileName;
        private boolean isUploaded;
        private String url;
        private Uri uri;

        public CustomImage(StorageReference baseStorageRef, Uri uri, String fileName) {
            this.storageReference = baseStorageRef;
            this.uri = uri;
            this.fileName = fileName;
        }

        private void fetchUrl() {
            if (storageReference != null)
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        url = uri.toString();
                    }
                });
        }

        public boolean isUploaded() {
            return isUploaded;
        }

        public void setUploaded(boolean uploaded) {
            isUploaded = uploaded;
            if (isUploaded == true)
                fetchUrl();
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Uri getUri() {
            return uri;
        }

        public void setUri(Uri uri) {
            this.uri = uri;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

    }
}
