package com.ksr.socialapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ksr.socialapp.R;
import com.ksr.socialapp.model.Post;

import java.util.ArrayList;

public class CreatePostImageAdapter extends RecyclerView.Adapter<CreatePostImageAdapter.viewHolder>{

    ArrayList<Uri> arrayList;
    Context context;

    public CreatePostImageAdapter(ArrayList<Uri> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.create_post_image_single_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
            Uri uri = arrayList.get(position);
            holder.postImage.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView postImage;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.postImage);
        }
    }
}
