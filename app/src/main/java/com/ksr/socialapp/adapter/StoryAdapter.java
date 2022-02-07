package com.ksr.socialapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devlomi.circularstatusview.CircularStatusView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ksr.socialapp.R;
import com.ksr.socialapp.model.Story;
import com.ksr.socialapp.model.User;
import com.ksr.socialapp.model.UserStories;
import com.ksr.socialapp.tools.Methods;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.viewHolder>{

    ArrayList<Story> arrayList;
    Context context;

    public StoryAdapter(ArrayList<Story> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.stories_single_item,parent,false);

        return new viewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Story story = arrayList.get(position);

        holder.statusCircle.setPortionsCount(story.getUserStories().size());

        //getting story data and showing
        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(story.getStoryBy()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(context).load(user.getProfile()).placeholder(R.drawable.placeholder).into(holder.story);
                holder.name.setText(user.getName());

                holder.story.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Opening Stories here with the StoryView Library
                        ArrayList<MyStory> myStories = new ArrayList<>();

                        for(UserStories stories : story.getUserStories()){
                            myStories.add(new MyStory(
                                   stories.getImage(),
                                   new Date( stories.getStoryAt())
                            ));
                        }
                        new StoryView.Builder(((AppCompatActivity)context).getSupportFragmentManager())
                                .setStoriesList(myStories) // Required
                                .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                                .setTitleText(user.getName()) // Default is Hidden
                                .setSubtitleText("") // Default is Hidden
                                .setTitleLogoUrl(user.getProfile()!=null? user.getProfile() : Methods.PLACEHOLDER_IMAGE_URL) // Default is Hidden
                                .setStoryClickListeners(new StoryClickListeners() {
                                    @Override
                                    public void onDescriptionClickListener(int position) {
                                        //your action
                                    }

                                    @Override
                                    public void onTitleIconClickListener(int position) {
                                        //your action
                                    }
                                }) // Optional Listeners
                                .build() // Must be called before calling show method
                                .show();
                    }
                });
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

    public class viewHolder extends RecyclerView.ViewHolder{

        private ImageView story;
        private TextView name;
        private CircularStatusView statusCircle;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            story = itemView.findViewById(R.id.story);
            name = itemView.findViewById(R.id.name);
            statusCircle = itemView.findViewById(R.id.statusCircle);
        }
    }
}
