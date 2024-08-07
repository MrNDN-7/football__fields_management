package com.example.booksportfield.Activity.ThanhBinh;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.booksportfield.Activity.Kiet.ChiTietSan;
import com.example.booksportfield.Adapter.ThanhBinhAdapter.SearchFieldAdapter;
import com.example.booksportfield.Models.Field;
import com.example.booksportfield.Models.TimeSlot;
import com.example.booksportfield.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class seach_fields extends AppCompatActivity {
    private EditText edt_search_field_page;
    private ImageButton btn_back, btnsearch;
    private ListView lvSearchHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seach_fields);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search_field_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edt_search_field_page = findViewById(R.id.edt_search_field_page);
        btn_back = findViewById(R.id.btn_back);
        lvSearchHome = findViewById(R.id.lvSearchHome);
        btnsearch = findViewById(R.id.btnsearch);
        edt_search_field_page.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edt_search_field_page, InputMethodManager.SHOW_IMPLICIT);
        // Yêu cầu hệ thống hiển thị bàn phím khi EditText nhận focus
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        btnBackHomeOnClick();
        inputKeyWord();
        btnSearchOnclick();
    }

    private void btnBackHomeOnClick() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(seach_fields.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void btnBackAccountOnClick() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(seach_fields.this, MainActivity.class);
                intent.putExtra("openAccountFragment", true);
                startActivity(intent);
                finish();

            }
        });
    }

    private void inputKeyWord() {
        edt_search_field_page.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().toLowerCase();
                Log.d("edtSearch", "hehee" + searchText);
                //searchData(searchText);
                // Hiển thị Toast để kiểm tra dữ liệu được nhập

            }
        });
    }

    private Query query;

    void btnSearchOnclick() {
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = edt_search_field_page.getText().toString().trim();
                if (!searchText.isEmpty()) {
                    // Nếu ô nhập dữ liệu không trống, thực hiện các hành động cần thiết
                    loadDataToListView(searchText);
                } else {
                    // Nếu ô nhập dữ liệu trống, hiển thị Toast thông báo
                    Toast.makeText(getApplicationContext(), "Ô nhập dữ liệu không được để trống", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    void loadDataToListView(String searchText) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("soccer_fields");

        if (searchText.isEmpty()) {

            Toast.makeText(this, "Ô nhập dữ liệu không được để trống" + searchText, Toast.LENGTH_SHORT).show();

        } else {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Field> listField = new ArrayList<>();
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        Map<String, Object> fieldData = (Map<String, Object>) childSnapshot.getValue();
                        String name = (String) fieldData.get("name");
                        String id = childSnapshot.getKey();

                        if (name != null && (name.equalsIgnoreCase(searchText) || name.toLowerCase().contains(searchText.toLowerCase()))) {
                            Field field = new Field();
                            field.setNodeID(id);
                            field = childSnapshot.getValue(Field.class);
                            field.setField_id(id);


                            listField.add(field);
                        }
                    }



                    if (listField != null && !listField.isEmpty()) {
                        // Tạo một Adapter cho ListView
                        SearchFieldAdapter adapter = new SearchFieldAdapter(getApplicationContext(), listField);
                        lvSearchHome = findViewById(R.id.lvSearchHome);
                        adapter.setOnItemClickListener(new SearchFieldAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Field field) {
                                Intent intent = new Intent(seach_fields.this, ChiTietSan.class);
                                intent.putExtra("object", field);
                                startActivity(intent);
                            }
                        });

                        // Gán Adapter cho ListView

                        lvSearchHome.setAdapter(adapter);
                    } else {
                        lvSearchHome = findViewById(R.id.lvSearchHome);
                        lvSearchHome.setAdapter(null);
                        Toast.makeText(getApplicationContext(), "Không tìm thấy sân nào", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


    }

    private void setAdapterhe(List<Field> list) {
        lvSearchHome = findViewById(R.id.lvSearchHome);
        Drawable drawable = getResources().getDrawable(R.drawable.avatardefault);
        lvSearchHome.setBackgroundDrawable(drawable);
        SearchFieldAdapter adapter = new SearchFieldAdapter(this, list);
        lvSearchHome.setAdapter(adapter);
    }

}