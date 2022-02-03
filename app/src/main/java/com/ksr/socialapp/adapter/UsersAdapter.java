package com.ksr.socialapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ksr.socialapp.R;
import com.ksr.socialapp.model.Follow;
import com.ksr.socialapp.model.Notification;
import com.ksr.socialapp.model.User;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Date;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.viewHolder> {

    Context context;
    ArrayList<User> arrayList;

    public UsersAdapter(Context context, ArrayList<User> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.users_single_item, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        User user = arrayList.get(position);
        //Picasso.get().load(user.getProfile()).placeholder(R.drawable.placeholder).into(holder.profileImage);
        Glide.with(context).load(user.getProfile()).placeholder(R.drawable.placeholder).into(holder.profileImage);
        holder.name.setText(user.getName());
        holder.profession.setText(user.getProfession());

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(user.getUserID())
                .child("Followers")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            holder.followBT.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.followed_btn_bg));
                            holder.followBT.setText("Following");
                            holder.followBT.setTextColor(context.getResources().getColor(R.color.white));
                            holder.followBT.setEnabled(false);
                        } else {
                            holder.followBT.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Follow follow = new Follow();
                                    follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
                                    follow.setFollowedAt(new Date().getTime());

                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Users")
                                            .child(user.getUserID())
                                            .child("Followers")
                                            .child(FirebaseAuth.getInstance().getUid())

                                            .setValue(follow).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("Users")
                                                    .child(user.getUserID())
                                                    .child("FollowerCount")
                                                    .setValue(user.getFollowerCount() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    holder.followBT.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.followed_btn_bg));
                                                    holder.followBT.setText("Following");
                                                    holder.followBT.setTextColor(context.getResources().getColor(R.color.white));
                                                    holder.followBT.setEnabled(false);
                                                    Toast.makeText(context, "You followed " + user.getName(), Toast.LENGTH_SHORT).show();

                                                    Notification notification = new Notification();
                                                    notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                    notification.setNotificationAt(new Date().getTime());
                                                    notification.setType("follow");
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("notification")
                                                            .child(user.getUserID())
                                                            .push()
                                                            .setValue(notification);

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

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView profileImage;
        private TextView name, profession;
        private Button followBT;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            name = itemView.findViewById(R.id.name);
            profession = itemView.findViewById(R.id.profession);
            followBT = itemView.findViewById(R.id.followBT);
        }
    }

}
