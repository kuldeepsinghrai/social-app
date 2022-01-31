package com.ksr.socialapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ksr.socialapp.R;
import com.ksr.socialapp.activities.CommentActivity;
import com.ksr.socialapp.model.Post;
import com.ksr.socialapp.model.User;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
        if (!post.getPostDescription().equals("")){
            holder.postDescription.setVisibility(View.VISIBLE);
            holder.postDescription.setText(post.getPostDescription());
        }
        Picasso.get().load(post.getPostImage()).placeholder(R.drawable.placeholder).into(holder.postImage);
        holder.like.setText(post.getPostLike()+"");
        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(post.getPostedBy()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Picasso.get()
                        .load(user.getProfile())
                        .placeholder(R.drawable.placeholder)
                        .into(holder.profile);
                holder.userName.setText(user.getName());
                holder.about.setText(user.getProfession());
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
                context.startActivity(new Intent(context, CommentActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        private ImageView profile;
        private RoundedImageView postImage;
        private TextView userName,about,postDescription, like,comment,share;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.profileImage);
            postImage  =itemView.findViewById(R.id.postImage);
            userName = itemView.findViewById(R.id.userName);
            about = itemView.findViewById(R.id.about);
            postDescription = itemView.findViewById(R.id.postDescription);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            share = itemView.findViewById(R.id.share);

        }
    }
}
