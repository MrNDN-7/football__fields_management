package com.example.booksportfield.Adapter.Kiet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.booksportfield.Fragment.Image_Slider;
import com.example.booksportfield.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<Image_Slider> imgs;
    private ViewPager2 viewPager2;

    public ImageAdapter(List<Image_Slider> imgs, ViewPager2 viewPager2) {
        this.imgs = imgs;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.setImageView(imgs.get(position).getImageUrl());
    }

    @Override
    public int getItemCount() {
        return imgs == null ? 0 : imgs.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView imageView;
        public ImageViewHolder(@NonNull View view){
            super(view);
            imageView = view.findViewById(R.id.image_list);

        }

        public void setImageView(String imageUrl) {
            Glide.with(imageView.getContext())
                    .load(imageUrl)
                    .into(imageView);
        }
    }
}
