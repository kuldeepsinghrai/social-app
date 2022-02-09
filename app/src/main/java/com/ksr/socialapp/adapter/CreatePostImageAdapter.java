package com.ksr.socialapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ksr.socialapp.R;
import com.ksr.socialapp.fragments.AddFragment;

import java.util.ArrayList;

public class CreatePostImageAdapter extends RecyclerView.Adapter<CreatePostImageAdapter.viewHolder> {

    ArrayList<AddFragment.CustomImage> arrayList;
    Context context;

    public CreatePostImageAdapter(ArrayList<AddFragment.CustomImage> arrayList, Context context) {
        this.arrayList = arrayList;
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
        AddFragment.CustomImage customImage = arrayList.get(position);
        Uri uri = customImage.getUri();
        holder.postImage.setImageURI(uri);

        if (arrayList.size()!=1) {
            holder.imageCountTV.setText((position + 1) + "/" + arrayList.size());
            holder.imageCountTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
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
