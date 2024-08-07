package com.example.booksportfield.Adapter.TuanAnhAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksportfield.Models.Field;
import com.example.booksportfield.Models.TimeSlot;
import com.example.booksportfield.R;

import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotHolder> {
    private List<TimeSlot> mListItem;
    private IClickListener mIClickListener;
    private Field field;

    public interface IClickListener{
        void onClickUpDateItem(TimeSlot item);
    }

    public TimeSlotAdapter(List<TimeSlot> mListItem, IClickListener listener, Field field)
    {
        this.mListItem = mListItem;
        this.mIClickListener = listener;
        this.field = field;
    }

    @NonNull
    @Override
    public TimeSlotHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_field_timeslot, parent,false);
        return new TimeSlotHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotHolder holder, int position) {
        TimeSlot item = mListItem.get(position);
        if(item == null)
        {
            return;
        }
        holder.tvName.setText("Tên sân: " + field.getName() );
        String status = item.getStatus();
        if (status.equals("dat")) {
            holder.tvStatus.setText("Đặt");
        } else {
            holder.tvStatus.setText("Chưa Đặt");
        }
        holder.tvBookingDate.setText("Ngày đặt: " + item.getBooking_date());
        holder.tvTimeSlot.setText("Thời gian bắt đầu : " + item.getStart_time() + " giờ"+ "\nThời gian kết thúc : " + item.getEnd_time() + " giờ");



        holder.btnUpdateTimeSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mIClickListener.onClickUpDateItem(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mListItem!=null)
        {
            return mListItem.size();
        }
        return 0;
    }

    public class TimeSlotHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvStatus;
        private TextView tvTimeSlot;
        private TextView tvBookingDate;
        private Button btnUpdateTimeSlot;

        public TimeSlotHolder(@NonNull View itemView) {
            super(itemView);
            this.tvName = itemView.findViewById(R.id.txtNameTimeSlot);
            this.tvStatus = itemView.findViewById(R.id.txtStatus);
            this.tvTimeSlot = itemView.findViewById(R.id.txtTimeSlot);
            this.tvBookingDate = itemView.findViewById(R.id.txtBookingDate);
            this.btnUpdateTimeSlot = itemView.findViewById(R.id.btnUpdateTimeSlot);
        }
    }
}
