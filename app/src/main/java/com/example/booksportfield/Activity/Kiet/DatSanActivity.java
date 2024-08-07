package com.example.booksportfield.Activity.Kiet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.booksportfield.Adapter.Kiet.DateAdapter;
import com.example.booksportfield.Adapter.Kiet.SliderAdapter;
import com.example.booksportfield.Adapter.ThanhBinhAdapter.UserSession;
import com.example.booksportfield.Fragment.SliderItems;
import com.example.booksportfield.Models.Account;
import com.example.booksportfield.Models.Booking;
import com.example.booksportfield.Models.Field;
import com.example.booksportfield.Models.TimeSlot;
import com.example.booksportfield.databinding.ActivityDatSanBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatSanActivity extends BaseActivity {
    private ActivityDatSanBinding binding;
    private Field object;
    private TimeSlot timeSlot;
    private Account user;
    ArrayList<TimeSlot> items = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDatSanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.imageView.setOnClickListener(v -> finish());
        initBanner();
        showTimeSlot();
        showInfoBooking();


        user = new Account();
        user.setUsername(UserSession.getInstance().getUsername());

        binding.btnDat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Booking booking = new Booking();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String currentDate = dateFormat.format(new Date());
                booking.setBooking_date(currentDate);
                booking.setBooking_status("Chờ xác nhận");
                if(timeSlot == null)
                    Toast.makeText(DatSanActivity.this, "Vui lòng chọn khung thời gian", Toast.LENGTH_SHORT).show();
                else {
                    booking.setTime_slot_id(timeSlot.getId_timeSlot());

                    booking.setPrice(object.getPrice() * (convertTimeToDecimal(timeSlot.getEnd_time()) - convertTimeToDecimal(timeSlot.getStart_time())));
                }
                booking.setUser_id(user.getUsername());
                booking.setField_id(object.getField_id());
                DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference("bookings");
                bookingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long count = dataSnapshot.getChildrenCount();

                        // Tạo key mới
                        String key = "booking" + (count + 1);
                        booking.setBooking_id(key);
                        if (booking.isValid()) {
                            bookingsRef.child(key).setValue(booking)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            // Xử lý khi thêm thành công
//                                            Toast.makeText(DatSanActivity.this, "Đặt thành công", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(DatSanActivity.this, PaymentActivity.class);
                                            intent.putExtra("booking", booking);
                                            intent.putExtra("field", object);
                                            intent.putExtra("time", timeSlot);
                                            timeSlot.setStatus("dat");
                                            DatabaseReference timeSlotRef = FirebaseDatabase.getInstance().getReference("soccer_fields").child(booking.getField_id()).child("timeslot").child(timeSlot.getId_timeSlot());
                                            timeSlotRef.child("status").setValue("dat")
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // Cập nhật RecyclerView
                                                            items.remove(timeSlot);
                                                            binding.recycleTime.setLayoutManager(new LinearLayoutManager(DatSanActivity.this, LinearLayoutManager.HORIZONTAL, false));
                                                            binding.recycleTime.setAdapter(new DateAdapter(items, object));

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            // Xử lý khi cập nhật thất bại
                                                            Toast.makeText(DatSanActivity.this, "Lỗi cập nhật trạng thái TimeSlot", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                            DatSanActivity.this.startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Xử lý khi thêm thất bại
                                            Toast.makeText(DatSanActivity.this, "Lỗi đặt sân bóng", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
//                        else if(!booking.isValid())
//                            Toast.makeText(DatSanActivity.this, "Vui lòng chọn khung thời gian", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });


            }
        });
    }
    public double convertTimeToDecimal(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        double decimalTime = hours + (double) minutes / 60;
        return decimalTime;
    }

    private void initBanner(){
        object = (Field) getIntent().getSerializableExtra("object");

        ArrayList<SliderItems> items = new ArrayList<>();
        for(int i = 0; i< object.getImage().size();i++)
            items.add(new SliderItems(object.getImage().get(i)));
        binding.viewPager2.setAdapter(new SliderAdapter(items, binding.viewPager2));
        binding.viewPager2.setClipToPadding(false);
        binding.viewPager2.setClipChildren(false);
        binding.viewPager2.setOffscreenPageLimit(3);
        binding.viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        binding.viewPager2.setPageTransformer(compositePageTransformer);
    }
    private void showTimeSlot(){
        object = (Field) getIntent().getSerializableExtra("object");

//        binding.txtTime.setText(String.format("Khung giờ: %s", time.toString()));


        if (object != null && object.getTimeslot() != null) {


            for(TimeSlot time : object.getTimeslot())
            {
                if(time != null && !time.getStatus().equals("dat"))
                    items.add(time);
            }
            binding.recycleTime.setLayoutManager(new LinearLayoutManager(DatSanActivity.this, LinearLayoutManager.HORIZONTAL, false));
            binding.recycleTime.setAdapter(new DateAdapter(items, object));
        }
    }
    @SuppressLint("SetTextI18n")
    private void showInfoBooking(){
        binding.txtTenSan.setText(object.getName());
        binding.txtAddress.setText(object.getAddress());
        binding.txtPrice.setText(object.getPrice() + "vnd");
        binding.txtTime.setText("Khung giờ: ");
    }
    @SuppressLint("SetTextI18n")
    public void updateTime(TimeSlot time) {
        binding.txtTime.setText("Khung giờ: " + time.toString());
        this.timeSlot = time;
    }


}