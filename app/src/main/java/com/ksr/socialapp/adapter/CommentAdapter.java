package com.ksr.socialapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ksr.socialapp.R;
import com.ksr.socialapp.model.Comment;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.viewHolder>{

    Context context;
    ArrayList<Comment> arrayList;

    public CommentAdapter(Context context, ArrayList<Comment> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        private TextView comment,time;
        private RoundedImageView profileImage;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.comment);
            time = itemView.findViewById(R.id.time);
            profileImage = itemView.findViewById(R.id.profileImage);
        }
    }
}
