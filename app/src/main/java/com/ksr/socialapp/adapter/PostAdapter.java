package com.ksr.socialapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ksr.socialapp.R;
import com.ksr.socialapp.activities.CommentActivity;
import com.ksr.socialapp.model.Notification;
import com.ksr.socialapp.model.Post;
import com.ksr.socialapp.model.User;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Date;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHolder>{

    ArrayList<Post> arrayList;
    Context context;

    public PostAdapter(ArrayList<Post> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_single_item,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {


        Post post = arrayList.get(position);


        if (post.getPostImages()!=null){

        PostImagesAdapter postImagesAdapter = new PostImagesAdapter(post.getPostImages(),context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.postImageRecyclerView.setLayoutManager(linearLayoutManager);
        holder.postImageRecyclerView.setAdapter(postImagesAdapter);
        }

        holder.like.setText(post.getPostLike()+"");
        holder.comment.setText(post.getCommentCount()+"");

        //getting User data storing it in User model then setting them in post
        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(post.getPostedBy()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(context).load(user.getProfile()).placeholder(R.drawable.placeholder).into(holder.profile);
                holder.userName.setText(user.getName());
                holder.about.setText(user.getProfession());
                if (!post.getPostDescription().equals("")){
                    holder.postDescription.setVisibility(View.VISIBLE);
                    holder.postDescription.setText(Html.fromHtml("<b>"+user.getName()+"</b>"+"  "+post.getPostDescription()));
                }else {
                    holder.postDescription.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //when post is already liked
        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(post.getPostID())
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            holder.like.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_favorite_filled,0,0,0);
                        }else {
                            //when user hits like button
                            holder.like.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("posts")
                                            .child(post.getPostID())
                                            .child("likes")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("posts")
                                                    .child(post.getPostID())
                                                    .child("postLike")
                                                    .setValue(post.getPostLike()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    holder.like.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_favorite_filled,0,0,0);

                                                    //sending notification when liking post is successfull
                                                    Notification notification = new Notification();
                                                    notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                    notification.setNotificationAt(new Date().getTime());
                                                    notification.setPostId(post.getPostID());
                                                    notification.setPostedBy(post.getPostedBy());
                                                    notification.setType("like");
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("notification")
                                                            .child(post.getPostedBy())
                                                            .push().setValue(notification);
                                                }
                                            });
                                        }
                                    });
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postId",post.getPostID());
                intent.putExtra("postedBy",post.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        private ImageView profile;
        private RecyclerView postImageRecyclerView;
        private TextView userName,about,postDescription, like,comment,share;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.profileImage);
            postImageRecyclerView  =itemView.findViewById(R.id.postImageRecyclerView);
            userName = itemView.findViewById(R.id.userName);
            about = itemView.findViewById(R.id.about);
            postDescription = itemView.findViewById(R.id.postDescription);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);

        }
    }
}
