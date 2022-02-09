package com.ksr.socialapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ksr.socialapp.R;

import java.util.ArrayList;
import java.util.List;

public class PostImagesAdapter extends RecyclerView.Adapter<PostImagesAdapter.viewHolder> {

    List<String> list;
    Context context;

    public PostImagesAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_image_single_item, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Glide.with(context).load(list.get(position)).placeholder(R.drawable.placeholder).into(holder.postImage);


        if (list.size()!=1){
            holder.imageCountTV.setText((position + 1) + "/" + list.size());
            holder.imageCountTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private ImageView postImage;
        private TextView imageCountTV;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.postImage);
            imageCountTV = itemView.findViewById(R.id.imageCountTV);
        }
    }
}
