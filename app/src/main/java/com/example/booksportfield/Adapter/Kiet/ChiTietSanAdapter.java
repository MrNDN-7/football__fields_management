package com.example.booksportfield.Adapter.Kiet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.booksportfield.Activity.Kiet.ChiTietSan;
import com.example.booksportfield.Models.Field;
import com.example.booksportfield.databinding.ActivitySanItemBinding;

import java.util.ArrayList;

public class ChiTietSanAdapter extends RecyclerView.Adapter<ChiTietSanAdapter.Viewholder> {
    private ArrayList<Field> items;
    private Context context;

    public ChiTietSanAdapter(ArrayList<Field> items) {
        this.items = items;
    }


    @NonNull
    @Override
    public ChiTietSanAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ActivitySanItemBinding chiTietSan = ActivitySanItemBinding.inflate(LayoutInflater.from(context),parent, false);
        return new Viewholder(chiTietSan);
    }

    @Override
    public void onBindViewHolder(@NonNull ChiTietSanAdapter.Viewholder holder, int position) {
        holder.binding.txtTenSan.setText(items.get(position).getName());
        holder.binding.simpleRatingBar.setRating((float) items.get(position).getRating());
        holder.binding.owner.setText(items.get(position).getField_owner());
        Glide.with(context).load(items.get(position).getImage().get(0)).into(holder.binding.img);
        holder.binding.img.setScaleType(ImageView.ScaleType.FIT_XY);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChiTietSan.class);
                intent.putExtra("object", items.get(position));
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ActivitySanItemBinding binding;

        public Viewholder(@NonNull ActivitySanItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
