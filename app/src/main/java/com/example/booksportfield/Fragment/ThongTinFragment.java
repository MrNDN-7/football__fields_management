package com.example.booksportfield.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.booksportfield.Activity.DucNhan.ChatActivity;
import com.example.booksportfield.Activity.DucNhan.LocationField;
import com.example.booksportfield.Activity.Kiet.DatSanActivity;
import com.example.booksportfield.Adapter.ThanhBinhAdapter.UserSession;
import com.example.booksportfield.Models.Account;
import com.example.booksportfield.Models.Field;
import com.example.booksportfield.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;


public class ThongTinFragment extends Fragment {



    public ThongTinFragment() {
        // Required empty public constructor
    }
    Account acc= new Account();
    String owner;




    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo các thành phần giao diện
        TextView txtName = view.findViewById(R.id.txtName);
        TextView txtAddress = view.findViewById(R.id.txtAddress);
        TextView txtPrice = view.findViewById(R.id.txtPrice);
        TextView txtOwner = view.findViewById(R.id.txtOwner);
        TextView txtRating = view.findViewById(R.id.txtRating);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        Button btnOrder = view.findViewById(R.id.btnOrder);
        TextView txtChat = view.findViewById(R.id.txtChat);

        // Lấy dữ liệu từ Bundle
        Bundle bundle = getArguments();
        Field object = (Field) bundle.getSerializable("object");

        if(UserSession.getInstance().getUsername().equals(object.getField_owner()))
        {
            txtChat.setVisibility(View.GONE);
        }
        if (bundle != null) {

            String name = object.getName();
            String address = object.getAddress();
            double price = object.getPrice();
            owner =object.getField_owner();
            double rating = object.getRating();

            // Hiển thị thông tin lên TextView và RatingBar
            txtName.setText(name);
            txtAddress.setText("Địa chỉ: " + address);
            txtPrice.setText("Giá: " + price + " VNĐ/h");
            txtOwner.setText("Chủ sân: "  + owner);
            txtRating.setText(String.valueOf(rating));
            ratingBar.setRating((float) rating);

            LoadAccByUsername(owner);
        }


        // Xử lý sự kiện khi nhấn vào nút Đặt Sân
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DatSanActivity.class);
                // Chuyển dữ liệu đến activity DatSanActivity
                intent.putExtra("object", (Serializable) object);
                startActivity(intent);
            }
        });
        txtChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);

                intent.putExtra("name", acc.getName());
                intent.putExtra("username", owner);
                intent.putExtra("imageUrl", acc.getUrlImage());
                intent.putExtra("loggedInUsername", UserSession.getInstance().getUsername());
                startActivity(intent);
            }
        });

        txtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LocationField.class);

                intent.putExtra("tolocation", object.getAddress());

                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_thong_tin, container, false);
    }

    private void LoadAccByUsername(String username)

    {
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("accounts/" + username);
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    acc.setUsername(username);
                    acc = snapshot.getValue(Account.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}