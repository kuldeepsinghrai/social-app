package com.ksr.socialapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ksr.socialapp.R;
import com.ksr.socialapp.adapter.CommentAdapter;
import com.ksr.socialapp.adapter.PostImagesAdapter;
import com.ksr.socialapp.model.Comment;
import com.ksr.socialapp.model.Notification;
import com.ksr.socialapp.model.Post;
import com.ksr.socialapp.model.User;
import com.ksr.socialapp.tools.Methods;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Date;

public class CommentActivity extends BaseActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private Intent intent;
    private String postId;
    private String postedBy;

    private Toolbar toolbar;
    private RecyclerView postImageRecyclerView, commentRecyclerView;

    private ImageView postCommentBT;
    private RoundedImageView profileImage;
    private TextView name,postDescription,like,comment,share;
    private EditText commentET;

    private ArrayList<Comment> commentArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        toolbar = findViewById(R.id.toolbar);
        postImageRecyclerView = findViewById(R.id.postImageRecyclerView);
        profileImage = findViewById(R.id.profileImage);
        name = findViewById(R.id.name);
        postDescription = findViewById(R.id.postDescription);
        like = findViewById(R.id.like);
        comment = findViewById(R.id.comment);
        commentET = findViewById(R.id.commentET);
        postCommentBT = findViewById(R.id.postCommentBT);
        commentRecyclerView = findViewById(R.id.commentRecyclerView);

        // custom toolbar for comments screen
        setSupportActionBar(toolbar);
        CommentActivity.this.setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



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

                        if (post.getPostImages()!=null){

                            PostImagesAdapter postImagesAdapter = new PostImagesAdapter(post.getPostImages(),getActivity());
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                            postImageRecyclerView.setLayoutManager(linearLayoutManager);
                            postImageRecyclerView.setAdapter(postImagesAdapter);
                        }

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
                //Picasso.get().load(user.getProfile()).placeholder(R.drawable.placeholder).into(profileImage);
                Glide.with(getActivity()).load(user.getProfile()).placeholder(R.drawable.placeholder).into(profileImage);
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
                                        Methods.hideSoftKeyboard(getActivity());
                                        Toast.makeText(getActivity(), "Commented!", Toast.LENGTH_SHORT).show();

                                        Notification notification = new Notification();
                                        notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                        notification.setNotificationAt(new Date().getTime());
                                        notification.setPostId(postId);
                                        notification.setPostedBy(postedBy);
                                        notification.setType("comment");

                                        FirebaseDatabase.getInstance().getReference()
                                                .child("notification")
                                                .child(postedBy)
                                                .push().setValue(notification);
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

        commentArrayList = new ArrayList<>();
        CommentAdapter commentAdapter = new CommentAdapter(getActivity(),commentArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        commentRecyclerView.setLayoutManager(linearLayoutManager);
        commentRecyclerView.setAdapter(commentAdapter);

        firebaseDatabase.getReference()
                .child("posts")
                .child(postId)
                .child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    commentArrayList.add(comment);
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected BaseActivity getActivity() {
        return this;
    }
}