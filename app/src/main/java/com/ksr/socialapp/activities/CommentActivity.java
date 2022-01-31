package com.ksr.socialapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ksr.socialapp.R;
import com.ksr.socialapp.model.Comment;
import com.ksr.socialapp.model.Post;
import com.ksr.socialapp.model.User;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class CommentActivity extends BaseActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private Intent intent;
    private String postId;
    private String postedBy;

    private ImageView postImage,postCommentBT;
    private RoundedImageView profileImage;
    private TextView name,postDescription,like,comment,share;
    private EditText commentET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        postImage = findViewById(R.id.postImage);
        profileImage = findViewById(R.id.profileImage);
        name = findViewById(R.id.name);
        postDescription = findViewById(R.id.postDescription);
        like = findViewById(R.id.like);
        comment = findViewById(R.id.comment);
        share = findViewById(R.id.share);
        commentET = findViewById(R.id.commentET);
        postCommentBT = findViewById(R.id.postCommentBT);


        intent = getIntent();
        postId = intent.getStringExtra("postId");
        postedBy = intent.getStringExtra("postedBy");


        firebaseDatabase.getReference()
                .child("posts")
                .child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Post post = snapshot.getValue(Post.class);
                        Picasso.get().load(post.getPostImage()).placeholder(R.drawable.placeholder).into(postImage);
                        postDescription.setText(post.getPostDescription());
                        like.setText(post.getPostLike()+"");
                        comment.setText(post.getCommentCount()+"");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        firebaseDatabase.getReference()
                .child("Users")
                .child(postedBy).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Picasso.get().load(user.getProfile()).placeholder(R.drawable.placeholder).into(profileImage);
                name.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        postCommentBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment comment = new Comment();
                comment.setCommentBody(commentET.getText().toString());
                comment.setCommentedAt(new Date().getTime());
                comment.setCommentedBy(FirebaseAuth.getInstance().getUid());
                firebaseDatabase.getReference()
                        .child("posts")
                        .child(postId)
                        .child("comments")
                        .push()
                        .setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        firebaseDatabase.getReference()
                                .child("posts")
                                .child(postId)
                                .child("commentCount").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int commentCount=0;
                                if (snapshot.exists()){
                                    commentCount = snapshot.getValue(Integer.class);
                                }
                                firebaseDatabase.getReference()
                                        .child("posts")
                                        .child(postId)
                                        .child("commentCount")
                                        .setValue(commentCount+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        commentET.setText("");
                                        Toast.makeText(getActivity(), "Commented!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });
            }
        });


    }

    @Override
    protected BaseActivity getActivity() {
        return this;
    }
}