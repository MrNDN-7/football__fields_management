package com.example.booksportfield.Adapter.Kiet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksportfield.Activity.Kiet.DatSanActivity;
import com.example.booksportfield.Models.Field;
import com.example.booksportfield.Models.TimeSlot;
import com.example.booksportfield.databinding.DateSlotBinding;

import java.util.ArrayList;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.Viewholder> {
    private ArrayList<TimeSlot> times;
    private Field object;
    private Context context;


    public DateAdapter(ArrayList<TimeSlot> times, Field object){
        this.times = times;
        for(int i = 0; i < this.times.size();i++)
        {
            if(this.times.get(i) == null)
            {
                this.times.remove(i);
            }
        }
        this.object = object;
    }
    @NonNull
    @Override
    public DateAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        DateSlotBinding binding = DateSlotBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DateAdapter.Viewholder holder, int position) {
        if(times.get(position) != null) {
            holder.binding.textView4.setText(times.get(position).toString());
            holder.binding.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((DatSanActivity)context).updateTime(times.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return times.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder {
        DateSlotBinding binding;
        public Viewholder(@NonNull DateSlotBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
        }
    }
}
