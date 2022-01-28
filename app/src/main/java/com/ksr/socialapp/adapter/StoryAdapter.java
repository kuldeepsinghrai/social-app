package com.ksr.socialapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ksr.socialapp.R;
import com.ksr.socialapp.model.StoryModel;

import java.util.ArrayList;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.viewHolder>{

    ArrayList<StoryModel> arrayList;
    Context context;

    public StoryAdapter(ArrayList<StoryModel> arrayList, Context context) {
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
        StoryModel storyModel = arrayList.get(position);
        holder.story.setImageResource(storyModel.getStory());
        holder.name.setText(storyModel.getName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        private ImageView story;
        private TextView name;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            story = itemView.findViewById(R.id.story);
            name = itemView.findViewById(R.id.name);
        }
    }
}
