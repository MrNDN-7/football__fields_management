package com.example.booksportfield.Activity.ThanhBinh;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.booksportfield.Adapter.ThanhBinhAdapter.UserSession;
import com.example.booksportfield.Models.Account;
import com.example.booksportfield.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ChangePassActivity extends AppCompatActivity {

    private ImageButton changePassBack, changePassSubmit;
    private EditText etConfirmPassword, etNewPassword, etCurrentPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_pass);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        changePassViews();
        changePassBack.setOnClickListener(v -> finish());
        String user = UserSession.getInstance().getUsername();
        if (getIntent() != null && getIntent().hasExtra("loadDataAccount")) {
            loadDataAccount(user);
        }

        onClickSubmit(user);
        validateInput();


    }

    private void changePassViews() {
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        changePassSubmit = findViewById(R.id.changePassSubmit);
        changePassBack = findViewById(R.id.changePassBack);
    }

    private void btnBackAccountOnClick() {
        changePassBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePassActivity.this, MainActivity.class);
                intent.putExtra("openAccountFragment", true);
                startActivity(intent);
                finish();

            }
        });
    }

    Account accInfor = new Account();
    private void loadDataAccount(String user){
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("accounts/" + user );
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    accInfor.setName(snapshot.child("name").getValue(String.class));
                    accInfor.setPhoneNumber(snapshot.child("phoneNumber").getValue(String.class));
                    accInfor.setEmail(snapshot.child("email").getValue(String.class));
                    accInfor.setDateOfBirth(snapshot.child("dateOfBirth").getValue(String.class));
                    accInfor.setGender(snapshot.child("gender").getValue(String.class));
                    accInfor.setRole(snapshot.child("role").getValue(String.class));
                    accInfor.setUrlImage(snapshot.child("urlImage").getValue(String.class));
                    accInfor.setPassword(snapshot.child("password").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private boolean[] isValid = {true, true, true};

    private Boolean isNullEditText(EditText editText)
    {
        // Assuming 'editText' is the EditText field you want to check
        return editText.getText() != null && !editText.getText().toString().trim().isEmpty();

    }
    private void validateInput()
    {

        isValid[0] = isNullEditText(etCurrentPassword);
        isValid[1] = isNullEditText(etNewPassword);
        isValid[2] = isNullEditText(etConfirmPassword);

        updateAllValid();


        etCurrentPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValid[0] = isNullEditText(etCurrentPassword);
                String currPass = etCurrentPassword.getText().toString().trim();
                if(accInfor != null)
                {
                    if (!currPass.equals(accInfor.getPassword())) {
                        isValid[0] =false;
                        etCurrentPassword.setError("Mật khẩu hiện tại không chính xác");
                    } else {
                        // Email format valid
                        etCurrentPassword.setError(null);
                        isValid[0] =true;
                    }
                }
                else
                {
                    etCurrentPassword.setError("Tài khoản không tồn tại");
                }
                updateAllValid();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used for this purpose
            }


        });

        etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValid[1] = isNullEditText(etNewPassword);
                String newPassword = etNewPassword.getText().toString().trim();
                int minLength = 8;
                if (newPassword.length() < minLength) { // Assuming 10-digit phone number format
                    // Phone number format invalid
                    isValid[1] = false;
                    etNewPassword.setError("Mật khẩu không hợp lệ");
                } else {
                    // Phone number format valid
                    etNewPassword.setError(null);
                    isValid[1] =true;
                }
                updateAllValid();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used for this purpose
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used for this purpose
            }
        });


        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValid[2] = isNullEditText(etConfirmPassword);
                String cfpass = etConfirmPassword.getText().toString().trim();
                String newPass = etNewPassword.getText().toString().trim();
                if (!cfpass.equals(newPass)) {
                    isValid[2] = false;
                    etConfirmPassword.setError("Mật khẩu nhập lại không trùng khớp");
                } else {

                    etConfirmPassword.setError(null);
                    isValid[2] = true;
                }
                updateAllValid();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used for this purpose
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used for this purpose
            }
        });


    }
    private void updateAllValid() {
        boolean allValid = true;
        for (boolean fieldValid : isValid) {
            if (!fieldValid) {
                allValid = false;
                break;
            }
        }
        changePassSubmit.setEnabled(allValid);
    }

    private void showConfirmationDialog(String user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận cập nhật");
        builder.setMessage("Bạn có muốn cập nhật thông tin?");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Update the information here
                updatePassword(user);

            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing or show a message indicating cancellation
                Toast.makeText(ChangePassActivity.this, "Cập nhật bị hủy", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void updatePassword(String user)
    {
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("accounts/"+ user);
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String newPass = etNewPassword.getText().toString().trim();
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("password", newPass);
                    dataRef.updateChildren(updates);

                    resetEdt();
                    Toast.makeText(ChangePassActivity.this, "Cập nhật dữ liệu thành công", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(ChangePassActivity.this, "Cập nhật dữ liệu không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChangePassActivity.this, "Cập nhật dữ liệu không thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onClickSubmit(String user)
    {
        changePassSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog(user);
            }
        });
    }

    private void resetEdt()
    {
        etNewPassword.setText("");
        etConfirmPassword.setText("");
        etCurrentPassword.setText("");
        etCurrentPassword.requestFocus();
    }
}