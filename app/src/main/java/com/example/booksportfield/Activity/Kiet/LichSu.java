package com.example.booksportfield.Activity.Kiet;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.booksportfield.Adapter.Kiet.LichSuAdapter;
import com.example.booksportfield.Adapter.ThanhBinhAdapter.UserSession;
import com.example.booksportfield.Models.Booking;
import com.example.booksportfield.Models.Field;
import com.example.booksportfield.Models.History;
import com.example.booksportfield.databinding.ActivityLichSuBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LichSu extends BaseActivity {
    ActivityLichSuBinding binding;
    ArrayList<History> histories = new ArrayList<>();
    ArrayList<Field> fields = new ArrayList<>();
    ArrayList<Field> fields1 = new ArrayList<>();
    ArrayList<Booking> bookings = new ArrayList<>();
    String id_Time = "";
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLichSuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user_id = UserSession.getInstance().getUsername();

        initFields();
        initHistory();
        binding.btnBack.setOnClickListener(v -> finish());


    }
    public void huyDatSan(History lichSu){//Thêm việc cập nhật timeSlot khi hủy

        histories.remove(lichSu);
        binding.recycleLichSu.setAdapter(new LichSuAdapter(histories, fields1));
        binding.recycleLichSu.setLayoutManager(new LinearLayoutManager(LichSu.this, LinearLayoutManager.VERTICAL, false));
        DatabaseReference timeSlotRef = FirebaseDatabase.getInstance().getReference("bookings").child(lichSu.getBooking_id());
        timeSlotRef.child("booking_status").setValue("Đã Hủy")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Cập nhật RecyclerView
                        Log.d("Huy dat san", "Thanh cong");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý khi cập nhật thất bại
                        Toast.makeText(LichSu.this, "Lỗi cập nhật trạng thái Booking", Toast.LENGTH_SHORT).show();
                    }
                });

        for(int i = 0; i < bookings.size();i++) {

            for (int j = 0; j < fields.size(); j++) {
                if(bookings.get(i).getField_id().equals(fields.get(j).getField_id()))
                {
                    for(int k = 0; k < fields.get(j).getTimeslot().size();k++) {
                        if(bookings.get(i).getTime_slot_id().equals(fields.get(j).getTimeslot().get(k).getId_timeSlot()))
                            id_Time = bookings.get(i).getTime_slot_id();
                    }
                }
            }
        }
        DatabaseReference field = FirebaseDatabase.getInstance().getReference("soccer_fields").child(lichSu.getId_field()).child("timeslot").child(id_Time);
        field.child("status").setValue("chuadat").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(LichSu.this, "Hủy sân thành công", Toast.LENGTH_SHORT);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
    private void initHistory(){
        DatabaseReference data = database.getReference("bookings");

        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue :snapshot.getChildren()){
                        Booking booking = issue.getValue(Booking.class);
                        booking.setBooking_id(issue.getKey());
                        if(booking.getUser_id().equals(user_id) && !booking.getBooking_status().equals("Đã Hủy"))
                            bookings.add(booking);



                    }
                    for(int i = 0; i < bookings.size();i++)
                    {
                        histories.add(new History());
                        histories.get(i).setUser_id(user_id);
                        for(int j = 0; j <= fields.size();j++)
                        {
                            if(bookings.get(i).getField_id().equals(fields.get(j).getField_id()))
                            {

                                fields1.add(fields.get(j));
                                histories.get(i).setLoaiSan(fields.get(j).getField_type());
                                histories.get(i).setRating(fields.get(j).getRating());
                                histories.get(i).setTenSan(fields.get(j).getName());
                                histories.get(i).setBooking_id(bookings.get(i).getBooking_id());
                                histories.get(i).setId_field(fields.get(j).getField_id());

                                for(int k = 0; k < fields.get(j).getTimeslot().size();k++) {

                                        if(bookings.get(i).getTime_slot_id().equals(fields.get(j).getTimeslot().get(k).getId_timeSlot())) {
                                            histories.get(i).setNgayHetHan(fields.get(j).getTimeslot().get(k).getBooking_date());
                                            histories.get(i).setId_time_slot(fields.get(j).getTimeslot().get(k).getId_timeSlot());
                                    }

                                }
                                break;

                            }

                        }
                    }
                    for(int i = 0; i<bookings.size();i++){
                        histories.get(i).setNgayDat(bookings.get(i).getBooking_date());
                    }
                    binding.recycleLichSu.setAdapter(new LichSuAdapter(histories, fields1));
                    binding.recycleLichSu.setLayoutManager(new LinearLayoutManager(LichSu.this, LinearLayoutManager.VERTICAL, false));



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initFields() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference data = database.getReference("soccer_fields");
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Field field = issue.getValue(Field.class);
                        field.setField_id(issue.getKey());
                        fields.add(field);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}