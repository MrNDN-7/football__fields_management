package com.example.booksportfield.Adapter.Kiet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksportfield.Activity.Kiet.ChiTietSan;
import com.example.booksportfield.Activity.Kiet.DatSanActivity;
import com.example.booksportfield.Activity.Kiet.LichSu;
import com.example.booksportfield.Models.Booking;
import com.example.booksportfield.Models.Comment;
import com.example.booksportfield.Models.Field;
import com.example.booksportfield.Models.History;
import com.example.booksportfield.databinding.DateSlotBinding;
import com.example.booksportfield.databinding.ItemHistoryBinding;
import com.example.booksportfield.databinding.ItemLichSuBinding;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class LichSuAdapter extends RecyclerView.Adapter<LichSuAdapter.Viewholder>{

    ArrayList<History> lichSus;
    ArrayList<Field> fields;
    Context context;

    public LichSuAdapter(ArrayList<History> lichSus, ArrayList<Field> fields ) {
        this.lichSus = lichSus;
        this.fields = fields;
    }

    @NonNull
    @Override
    public LichSuAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ItemHistoryBinding binding = ItemHistoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LichSuAdapter.Viewholder holder, int position) {
//        if (lichSus.get(position).getId_time_slot() != null) {
            holder.binding.ratingBarHistory.setRating((float) lichSus.get(position).getRating());
            holder.binding.txtTenSanLichSu.setText(lichSus.get(position).getTenSan());
            holder.binding.txtLoaiSanLichSu.setText("Loại sân: " + lichSus.get(position).getLoaiSan());
            holder.binding.txtNgayDatSan.setText(lichSus.get(position).getNgayDat());
            String khungTime = lichSus.get(position).getId_time_slot();
            Field fieldSele = new Field();
            for (Field field : fields) {
                if (field.getField_id().equals(lichSus.get(position).getId_field())) {
                    fieldSele = field;
                    break;
                }
            }

            holder.binding.txtKhungTime.setText(String.format("Khung giờ:%s", fieldSele.getTimeById(lichSus.get(position).getId_time_slot())));
            if (isPastDate(lichSus.get(position).getNgayHetHan())) {
                holder.binding.btnHuySan.setVisibility(View.GONE); // Ẩn nút hủy nếu thời gian đã qua
            } else {
                holder.binding.btnHuySan.setVisibility(View.VISIBLE); // Hiện nút hủy nếu thời gian chưa đến
            }
            holder.binding.btnDatLaiLichSu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChiTietSan.class);
                    // Chuyển dữ liệu đến activity DatSanActivity
                    intent.putExtra("object", (Serializable) fields.get(position));
                    context.startActivity(intent);
                }
            });
            holder.binding.btnHuySan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((LichSu) context).huyDatSan(lichSus.get(position));
                }
            });
        }


    @Override
    public int getItemCount() {
        return lichSus.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ItemHistoryBinding binding;
        public Viewholder(@NonNull ItemHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    @SuppressLint("NewApi")
    private boolean isPastDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            // Chuyển đổi chuỗi date thành đối tượng LocalDate
            LocalDate inputDate = LocalDate.parse(date, formatter);

            // Lấy ngày hiện tại
            LocalDate currentDate = LocalDate.now();

            // So sánh ngày hiện tại với ngày được truyền vào
            return currentDate.isAfter(inputDate);
        } catch (Exception e) {
            // Xử lý ngoại lệ nếu chuỗi date không hợp lệ
            System.out.println("Invalid date format or date string: " + date);
            e.printStackTrace();
            return false;
        }
    }
}
