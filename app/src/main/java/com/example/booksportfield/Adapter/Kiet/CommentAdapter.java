package com.example.booksportfield.Adapter.Kiet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksportfield.Models.Comment;
import com.example.booksportfield.databinding.RatingItemBinding;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Viewholder> {

    ArrayList<Comment> comments;
    Context context;

    public CommentAdapter(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        RatingItemBinding binding = RatingItemBinding.inflate(LayoutInflater.from(context), parent, false);

        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.Viewholder holder, int position) {
        holder.binding.txtComment.setText(comments.get(position).getComment());
//        holder.binding.txtRate.setText(comments.get(position).getRating()+"");
//        holder.binding.txtNameUser.setText(comments.get(position).getUser_name());
//        Glide.with(context).load(comments.get(position).getImage_user()).transform(new GranularRoundedCorners(100, 100, 100, 100)).into(holder.binding.imgAvt);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        RatingItemBinding binding;
        public Viewholder(RatingItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
