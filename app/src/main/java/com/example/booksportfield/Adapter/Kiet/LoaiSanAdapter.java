package com.example.booksportfield.Adapter.Kiet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksportfield.Activity.Kiet.ChiTietSan;
import com.example.booksportfield.Activity.Kiet.DatSanActivity;
import com.example.booksportfield.Models.Field;
import com.example.booksportfield.Models.History;
import com.example.booksportfield.databinding.ItemLichSuBinding;

import java.io.Serializable;
import java.util.ArrayList;

public class LoaiSanAdapter extends RecyclerView.Adapter<LoaiSanAdapter.Viewholder>{
    ArrayList<Field> fields;
    Context context;

    public LoaiSanAdapter(ArrayList<Field> fields) {
        this.fields = fields;
    }

    @NonNull
    @Override
    public LoaiSanAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ItemLichSuBinding binding = ItemLichSuBinding.inflate(LayoutInflater.from(context), parent, false);
        return new LoaiSanAdapter.Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LoaiSanAdapter.Viewholder holder, int position) {
        holder.binding.ratingBarLichSu.setRating((float) fields.get(position).getRating());
        holder.binding.txtTenSan.setText(fields.get(position).getName());
        holder.binding.textView.setText("Loại sân: "+ fields.get(position).getField_type());
        holder.binding.txtMoTa.setText(fields.get(position).getDescription());
        holder.binding.btnDatLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChiTietSan.class);
                // Chuyển dữ liệu đến activity DatSanActivity
                intent.putExtra("object", (Serializable) fields.get(position));
                context.startActivity(intent);
            }
        });
        holder.binding.btnDanhGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChiTietSan.class);
                // Chuyển dữ liệu đến activity DatSanActivity
                intent.putExtra("object", (Serializable) fields.get(position));
                intent.putExtra("isCmt", true);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fields.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ItemLichSuBinding binding;
        public Viewholder(@NonNull ItemLichSuBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
