package com.example.booksportfield.Activity.Kiet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.booksportfield.Activity.ThanhBinh.MainActivity;
import com.example.booksportfield.Adapter.ThanhBinhAdapter.UserSession;
import com.example.booksportfield.Models.Booking;
import com.example.booksportfield.Models.Field;
import com.example.booksportfield.Models.TimeSlot;
import com.example.booksportfield.databinding.ActivityPaymentBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PaymentActivity extends BaseActivity {

    private ActivityPaymentBinding binding;
    private Booking booking;
    private Field field;

    private TimeSlot time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        showInfoPayment();

    }
    public void showInfoPayment(){
        booking = (Booking)  getIntent().getSerializableExtra("booking");
        field = (Field) getIntent().getSerializableExtra("field");
        time = (TimeSlot) getIntent().getSerializableExtra("time");
        binding.btnReturn.setOnClickListener(v -> finish());
        binding.txtDiaChi.setText(field.getAddress());
        binding.txtFieldname.setText(field.getName());
        binding.txtGiaSan.setText((String.format("%s vnd", booking.getPrice())));
        binding.txtTimeSlot.setText(time.toString());
        binding.btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PaymentActivity.this, "Đặt thành công, vui lòng thanh toán khi đến sân", Toast.LENGTH_SHORT).show();
                DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("bookings").child(booking.getBooking_id());
                bookingRef.child("booking_status").setValue("dat").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Trạng Thái Đặt", "Thành công");
                        Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                        intent.putExtra("username", UserSession.getInstance().getUsername());
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Trạng Thái Đặt", "Thất bại");
                    }
                });
            }
        });
    }
}