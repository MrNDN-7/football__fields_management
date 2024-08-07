package com.example.booksportfield.Activity.ThanhBinh;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ChangeInforActivity extends AppCompatActivity {

    private ImageButton changeInforBack, changeInforSubmit;
    private EditText changInforedtName, changeInforedtSDT, changeInforedtEmail, edtDateOfBirth;
    private Spinner spGender;
    private ImageView imgCalendar;

    private Boolean nameCheck = false, phoneCheck = false, emailCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_infor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String user = UserSession.getInstance().getUsername();

        changeInforViews();
        changeInforBack.setOnClickListener(v -> finish());
        pickDate();
        onClickSubmit(user);
        validateInput();
        if (getIntent() != null && getIntent().hasExtra("loadDataAccount")) {
            loadDataAccount(user);
        }


    }

    private void changeInforViews() {
        changeInforedtEmail = findViewById(R.id.changeInforedtEmail);
        changeInforedtSDT = findViewById(R.id.changeInforedtSDT);
        changInforedtName = findViewById(R.id.changInforedtName);
        changeInforSubmit = findViewById(R.id.changeInforSubmit);
        changeInforBack = findViewById(R.id.changeInforBack);
        imgCalendar = findViewById(R.id.imgCalendar);
        spGender = findViewById(R.id.spGender);
        edtDateOfBirth = findViewById(R.id.edtDateOfBirth);
    }

    private void btnBackAccountOnClick() {
        changeInforBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeInforActivity.this, MainActivity.class);
                intent.putExtra("openAccountFragment", true);
                startActivity(intent);
                finish();

            }
        });
    }

    private void pickDate() {
        imgCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    public void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update the EditText with the selected date
                        edtDateOfBirth.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void onClickSubmit(String user) {


        changeInforSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog(user);
            }
        });
    }

    private void showConfirmationDialog(String user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận cập nhật");
        builder.setMessage("Bạn có muốn cập nhật thông tin?");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Update the information here
                try {
                    updateInformation(user);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing or show a message indicating cancellation
                Toast.makeText(ChangeInforActivity.this, "Cập nhật bị hủy", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void updateInformation(String user) throws UnsupportedEncodingException {
        if (String.valueOf(changInforedtName.getText()) != null || changeInforedtSDT.getText().toString() != null || changeInforedtEmail.getText().toString() != null || edtDateOfBirth.getText().toString() != null || spGender.getSelectedItem().toString() != null) {
            String name = String.valueOf(changInforedtName.getText());
            String phoneNumber = changeInforedtSDT.getText().toString();
            String Email = changeInforedtEmail.getText().toString();
            String dateOfBirth = edtDateOfBirth.getText().toString();
            String gender = spGender.getSelectedItem().toString();

            String encodedUserId = URLEncoder.encode(user, "UTF-8");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("accounts/" + user);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Map<String, Object> updates = new HashMap<>();

                        // Update only fields with changes (optional optimization)
                        String existingName = (String) snapshot.child("name").getValue();
                        if (!name.equals(existingName)) {
                            updates.put("name", name);
                        }
                        updates.put("phoneNumber", phoneNumber);
                        updates.put("email", Email);
                        updates.put("dateOfBirth", dateOfBirth);
                        updates.put("gender", gender);
                        // Similar checks for other fields (phoneNumber, Email, dateOfBirth, gender)

                        databaseReference.updateChildren(updates);
                        Toast.makeText(ChangeInforActivity.this, "Cập nhật dữ liệu thành công", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(ChangeInforActivity.this, "Cập nhật dữ liệu không thành công", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }

    private boolean[] isValid = {true, true, true, true};

    private Boolean isNullEditText(EditText editText) {
        // Assuming 'editText' is the EditText field you want to check
        return editText.getText() != null && !editText.getText().toString().trim().isEmpty();

    }

    private void validateInput() {
        isValid[0] = isNullEditText(changeInforedtEmail);
        isValid[1] = isNullEditText(changeInforedtSDT);
        isValid[2] = isNullEditText(changInforedtName);
        isValid[3] = isNullEditText(edtDateOfBirth);
        updateAllValid();


        // Email Validation
        changeInforedtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValid[0] = isNullEditText(changeInforedtEmail);
                String emailInput = changeInforedtEmail.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    // Email format invalid
                    isValid[0] = false;
                    changeInforedtEmail.setError("Email không hợp lệ");
                } else {
                    // Email format valid
                    changeInforedtEmail.setError(null);
                    isValid[0] = true;
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

// Phone Number Validation
        changeInforedtSDT.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValid[1] = isNullEditText(changeInforedtSDT);
                String phoneInput = changeInforedtSDT.getText().toString().trim();
                if (!phoneInput.matches("^\\d{10}$")) { // Assuming 10-digit phone number format
                    // Phone number format invalid
                    isValid[1] = false;
                    changeInforedtSDT.setError("Số điện thoại không hợp lệ");
                } else {
                    // Phone number format valid
                    changeInforedtSDT.setError(null);
                    isValid[1] = true;
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

// Name Validation (You can use regular expressions or custom validation logic)
        changInforedtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValid[2] = isNullEditText(changInforedtName);
                String nameInput = changInforedtName.getText().toString().trim();
                String pattern = "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚÝàáâãèéêìíòóôõùúýĂăĐđĨĩŨũƠơƯưẠạẢảẤấẦầẨẩẪẫẬậẮắẰằẲẳẴẵẶặẸẹẺẻẼẽẾếỀềỂểỄễỆệỈỉỊịỌọỎỏỐốỒồỔổỖỗỘộỚớỜờỞởỠỡỢợỤụỦủỨứỪừỬửỮữỰựỲỳỴỵỶỷỸỹ\\s]+\\s*$";

                if (!nameInput.matches(pattern)) {
                    // Name format invalid
                    isValid[2] = false;
                    changInforedtName.setError("Tên không hợp lệ");
                } else {
                    // Name format valid
                    changInforedtName.setError(null);
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
        edtDateOfBirth.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValid[3] = isNullEditText(edtDateOfBirth);
                String dateInput = edtDateOfBirth.getText().toString().trim();
                updateAllValid();
//                if (!dateInput.matches("[A-Za-zÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚÝỸàáâãèéêìíòóôõùúýỹ ]{2,}")) { // Assuming name should have at least 2 characters
//                    // Name format invalid
//
//                    changInforedtName.setError("Tên không hợp lệ");
//                } else {
//                    // Name format valid
//                    changInforedtName.setError(null);
//
//                }
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
        changeInforSubmit.setEnabled(allValid);
    }

    Account accInfor = new Account();

    private void loadDataAccount(String user) {
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("accounts/" + user);
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (String.valueOf(changInforedtName.getText()) != null || changeInforedtSDT.getText().toString() != null || changeInforedtEmail.getText().toString() != null || edtDateOfBirth.getText().toString() != null || spGender.getSelectedItem().toString() != null) {

                        accInfor.setName(snapshot.child("name").getValue(String.class));
                        accInfor.setPhoneNumber(snapshot.child("phoneNumber").getValue(String.class));
                        accInfor.setEmail(snapshot.child("email").getValue(String.class));
                        accInfor.setDateOfBirth(snapshot.child("dateOfBirth").getValue(String.class));
                        accInfor.setGender(snapshot.child("gender").getValue(String.class));
                        accInfor.setRole(snapshot.child("role").getValue(String.class));
                        accInfor.setUrlImage(snapshot.child("urlImage").getValue(String.class));
                        accInfor.setPassword(snapshot.child("password").getValue(String.class));

                        setTextInfor();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void setTextInfor() {
        if (accInfor != null) {
            if (String.valueOf(changInforedtName.getText()) != null || changeInforedtSDT.getText().toString() != null || changeInforedtEmail.getText().toString() != null || edtDateOfBirth.getText().toString() != null || spGender.getSelectedItem().toString() != null) {

                changInforedtName.setText(accInfor.getName());
                changeInforedtEmail.setText(accInfor.getEmail());
                changeInforedtSDT.setText(accInfor.getPhoneNumber());
                edtDateOfBirth.setText(accInfor.getDateOfBirth());

                if (accInfor.getGender() != null) {
                    if (accInfor.getGender().equals("Nam")) {
                        spGender.setSelection(0);
                    } else if (accInfor.getGender().equals("Nữ")) {
                        spGender.setSelection(1);
                    } else if (accInfor.getGender().equals("Khác")) {
                        spGender.setSelection(2);
                    } else {
                        spGender.setSelection(0);
                    }
                }
            }
        }
    }

}