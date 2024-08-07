package com.example.booksportfield.Activity.Kiet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.booksportfield.Adapter.Kiet.LichSuAdapter;
import com.example.booksportfield.Adapter.Kiet.LoaiSanAdapter;
import com.example.booksportfield.Models.Field;
import com.example.booksportfield.R;
import com.example.booksportfield.databinding.ActivityLoaiSanBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class LoaiSanActivity extends BaseActivity {
    List<String> items = new ArrayList<>();
    ArrayList<Field> fieldAll = new ArrayList<>();

    ActivityLoaiSanBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoaiSanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        findFieldByType();
        binding.imageView3.setOnClickListener(v -> finish());
        items.add("Sân 5");
        items.add("Sân 7");
        items.add("Sân 11");
        items.add("Tất cả");

        // Tạo ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);

        // Thiết lập loại dropdown cho Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spinner.setAdapter(adapter);

        // Lấy giá trị từ Intent





    }
    private void findFieldByType() {



        DatabaseReference databaseRef = database.getReference("soccer_fields");

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Field field = snapshot.getValue(Field.class);
                        field.setField_id(snapshot.getKey());
                        fieldAll.add(field);
                    }
                    int selectedFieldType = 0;
                    String Type = (String) getIntent().getSerializableExtra("fieldType");
                    if(Type.equals("allField"))
                    {
                        int position = items.indexOf("Tất cả");
                        // Nếu vị trí tồn tại, gán giá trị cho Spinner
                        if (position != -1) {
                            binding.spinner.setSelection(position);
                        }
                        binding.recycleLoaiSan.setLayoutManager(new LinearLayoutManager(LoaiSanActivity.this, LinearLayoutManager.VERTICAL, false));
                        binding.recycleLoaiSan.setAdapter(new LoaiSanAdapter(fieldAll));
                    }
                    else {
                        selectedFieldType = Integer.parseInt(Type);

                        if (selectedFieldType != 0) {
                            // Tìm vị trí của giá trị trong danh sách items
                            int position = items.indexOf("Sân " + selectedFieldType);
                            // Nếu vị trí tồn tại, gán giá trị cho Spinner
                            if (position != -1) {
                                binding.spinner.setSelection(position);
                            }
                        }
                        ArrayList<Field> fields = new ArrayList<>();
                        for (Field field : fieldAll) {
                            if (field.getField_type() == selectedFieldType)
                                fields.add(field);
                        }
                        binding.recycleLoaiSan.setLayoutManager(new LinearLayoutManager(LoaiSanActivity.this, LinearLayoutManager.VERTICAL, false));
                        binding.recycleLoaiSan.setAdapter(new LoaiSanAdapter(fields));
                    }
                    binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedItem = items.get(position);
                            // Xác định loại sân dựa trên tên
                            int fieldType = 0;
                            switch (selectedItem) {
                                case "Sân 5":
                                    fieldType = 5;
                                    break;
                                case "Sân 7":
                                    fieldType = 7;
                                    break;
                                case "Sân 11":
                                    fieldType = 11;
                                    break;
                                case "Tất cả":
                                    fieldType = -1;
                                    binding.recycleLoaiSan.setLayoutManager(new LinearLayoutManager(LoaiSanActivity.this, LinearLayoutManager.VERTICAL, false));
                                    binding.recycleLoaiSan.setAdapter(new LoaiSanAdapter(fieldAll));
                                    break;
                            }
                            if(fieldType != -1)
                            {
                                ArrayList<Field> fields = new ArrayList<>();
                                for(Field field :fieldAll)
                                {
                                    if(field.getField_type() == fieldType)
                                        fields.add(field);
                                }
                                binding.recycleLoaiSan.setLayoutManager(new LinearLayoutManager(LoaiSanActivity.this, LinearLayoutManager.VERTICAL, false));
                                binding.recycleLoaiSan.setAdapter(new LoaiSanAdapter(fields));
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            binding.recycleLoaiSan.setLayoutManager(new LinearLayoutManager(LoaiSanActivity.this, LinearLayoutManager.VERTICAL, false));
                            binding.recycleLoaiSan.setAdapter(new LoaiSanAdapter(fieldAll));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });

        return;
    }

}