package com.example.booksportfield.Activity.ThanhBinh;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.booksportfield.Adapter.ThanhBinhAdapter.UserSession;
import com.example.booksportfield.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;


public class FormUpdateRoleActivity extends AppCompatActivity {
    EditText edtPhoneNumber, edtAddress;
    CheckBox checkBox;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_update_role);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtAddress = findViewById(R.id.edtAddress);
        checkBox = findViewById(R.id.checkBox);
        btnSubmit = findViewById(R.id.btnSubmit);
        String user = UserSession.getInstance().getUsername();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra nếu ô nhập liệu không được bỏ trống và checkbox được check
                if (validateInput() && checkBox.isChecked()) {
                    // Lấy dữ liệu từ ô nhập liệu
                    String phoneNumber = edtPhoneNumber.getText().toString();
                    String address = edtAddress.getText().toString();

                    Map<String, Object> newData = new HashMap<>();
                    newData.put("phoneNumber", phoneNumber);
                    newData.put("addressField", address);

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("updaterole/" + user);
                    databaseReference.setValue(newData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(FormUpdateRoleActivity.this, "Số điện thoại: " + phoneNumber + "\nĐịa chỉ sân: " + address, Toast.LENGTH_SHORT).show();

                        }
                    });

                } else {
                    // Hiển thị thông báo nếu ô nhập liệu bị bỏ trống hoặc checkbox không được check
                    Toast.makeText(FormUpdateRoleActivity.this, "Vui lòng nhập đầy đủ thông tin và đồng ý với các điều khoản", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean validateInput() {
        String phoneNumber = edtPhoneNumber.getText().toString();
        String address = edtAddress.getText().toString();

        // Kiểm tra nếu ô nhập liệu không được bỏ trống và checkbox được check
        return !TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(address);
    }
}