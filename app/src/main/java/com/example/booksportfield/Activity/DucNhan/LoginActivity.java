package com.example.booksportfield.Activity.DucNhan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.booksportfield.Activity.ThanhBinh.MainActivity;
import com.example.booksportfield.Activity.ThanhBinh.SubmitUpdateRoleOfAdminActivity;
import com.example.booksportfield.Adapter.ThanhBinhAdapter.UserSession;
import com.example.booksportfield.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    EditText loginUsername, loginPassword;
    Button loginButton;
    TextView signupRedirectText,txterror,forgotPassText;
    RadioButton radioOwnerField, radioUser ,radioAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        thamchieu();
        action_to_signup();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() | !validatePassword() | !validateUserType()) {
                } else {
                    checkUser();
                }
            }
        });

        // Bắt sự kiện khi chọn radioOwnerField
        radioOwnerField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioUser.setChecked(false);
                radioAdmin.setChecked(false);
            }
        });

        // Bắt sự kiện khi chọn radioUser
        radioUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioOwnerField.setChecked(false);
                radioAdmin.setChecked(false);
            }
        });
        radioAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioUser.setChecked(false);
                radioOwnerField.setChecked(false);
            }
        });
        forgotPassText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    public void thamchieu() {
        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        radioOwnerField = findViewById(R.id.radioOwnerField);
        radioUser = findViewById(R.id.radioUser);
        radioAdmin = findViewById(R.id.radioAdmin);
        txterror=findViewById(R.id.txterror);
        forgotPassText=findViewById(R.id.forgotPassText);
    }

    public Boolean validateUsername() {
        String val = loginUsername.getText().toString();
        if (val.isEmpty()) {
            loginUsername.setError("Tên đăng nhập không được trống!!");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Mật khẩu không được trống!!");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void action_to_signup() {
        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    public String getUserType() {
        if (radioOwnerField.isChecked()) {
            return "ownerField";
        } else if (radioUser.isChecked()) {
            return "user";
        } else if (radioAdmin.isChecked()) {
            return "admin";
        } else {
            // Trường hợp không được chọn
            return null;
        }
    }

    public Boolean validateUserType() {
        if (!radioOwnerField.isChecked() && !radioUser.isChecked() && !radioAdmin.isChecked()) {
            txterror.setText("Bạn phải chọn loại người dùng");
            return false;
        } else {
            radioOwnerField.setError(null);
            radioUser.setError(null);
            radioAdmin.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();
        String userType = getUserType();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("accounts");

        reference.child(userUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String passwordFromDB = snapshot.child("password").getValue(String.class);
                    String roleFromDB = snapshot.child("role").getValue(String.class);

                    if (passwordFromDB != null && passwordFromDB.equals(userPassword)) {
                        if (userType == null || !userType.equals(roleFromDB)) {
                            if (userType == null) {
                                txterror.setText("Bạn phải chọn loại người dùng");
                            } else {
                                // Vai trò không khớp với tài khoản
                                if (roleFromDB.equals("ownerField")) {
                                    txterror.setText("Tài khoản người dùng không đúng!!!");
                                } else if (roleFromDB.equals("user")) {
                                    txterror.setText("Tài khoản người dùng không đúng!!!");
                                } else {
                                    txterror.setText("Tài khoản người dùng không đúng!!!");                                }
                            }
                        } else{
                            String nameFromDB = snapshot.child("name").getValue(String.class);
                            String emailFromDB = snapshot.child("email").getValue(String.class);

                            if ("ownerField".equals(userType)) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                intent.putExtra("name", nameFromDB);
                                intent.putExtra("email", emailFromDB);
                                intent.putExtra("username", userUsername);
                                UserSession.getInstance().setUsername(userUsername);
                                intent.putExtra("password", passwordFromDB);
                                intent.putExtra("loggedInUsername", userUsername);
                                startActivity(intent);
                            } else if ("admin".equals(userType)){
                                Intent intent = new Intent(LoginActivity.this, SubmitUpdateRoleOfAdminActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                intent.putExtra("name", nameFromDB);
                                intent.putExtra("email", emailFromDB);
                                intent.putExtra("username", userUsername);
                                UserSession.getInstance().setUsername(userUsername);
                                intent.putExtra("password", passwordFromDB);
                                intent.putExtra("loggedInUsername", userUsername);
                                startActivity(intent);
                            }else
                            {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("name", nameFromDB);
                                intent.putExtra("email", emailFromDB);
                                intent.putExtra("username", userUsername);
                                UserSession.getInstance().setUsername(userUsername);
                                intent.putExtra("password", passwordFromDB);
                                intent.putExtra("loggedInUsername", userUsername);
                                startActivity(intent);
                            }
                        }
                    } else {
                        loginPassword.setError("Thông tin mật khẩu không hợp lệ");
                        loginPassword.requestFocus();
                    }
                } else {
                    loginUsername.setError("Người Dùng Không Tồn Tại");
                    loginUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi khi truy vấn bị hủy bỏ
            }
        });
    }

}

//package com.example.booksportfield;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.RadioButton;
//import android.widget.TextView;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class LoginActivity extends AppCompatActivity {
//    EditText loginUsername, loginPassword;
//    Button loginButton;
//    TextView signupRedirectText;
//
//    RadioButton radioOwnerField,radioUser;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        thamchieu();
//        action_to_signup();
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!validateUsername() | !validatePassword()) {
//                } else {
//                    checkUser();
//                }
//            }
//        });
//
//    }
//
//
//    public void thamchieu()
//    {
//        loginUsername = findViewById(R.id.login_username);
//        loginPassword = findViewById(R.id.login_password);
//        loginButton = findViewById(R.id.login_button);
//        signupRedirectText = findViewById(R.id.signupRedirectText);
//        radioOwnerField=findViewById(R.id.radioOwnerField);
//        radioUser=findViewById(R.id.radioUser);
//    }
//
//    public Boolean validateUsername() {
//        String val = loginUsername.getText().toString();
//        if (val.isEmpty()) {
//            loginUsername.setError("Tên đăng nhập không được trống!!");
//            return false;
//        } else {
//            loginUsername.setError(null);
//            return true;
//        }
//    }
//    public Boolean validatePassword(){
//        String val = loginPassword.getText().toString();
//        if (val.isEmpty()) {
//            loginPassword.setError("Mật khẩu không được trống!!");
//            return false;
//        } else {
//            loginPassword.setError(null);
//            return true;
//        }
//    }
//
//    public void action_to_signup()
//    {
//        signupRedirectText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
//                startActivity(intent);
//            }
//        });
//
//    }
//    public void checkUser() {
//        String userUsername = loginUsername.getText().toString().trim();
//        String userPassword = loginPassword.getText().toString().trim();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("accounts");
//
//        reference.child(userUsername).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    String passwordFromDB = snapshot.child("password").getValue(String.class);
//                    if (passwordFromDB != null && passwordFromDB.equals(userPassword)) {
//                        String nameFromDB = snapshot.child("name").getValue(String.class);
//                        String emailFromDB = snapshot.child("email").getValue(String.class);
//
//                        Intent intent = new Intent(LoginActivity.this, OwnerFieldActivity.class);
//                        intent.putExtra("name", nameFromDB);
//                        intent.putExtra("email", emailFromDB);
//                        intent.putExtra("username", userUsername);
//                        intent.putExtra("password", passwordFromDB);
//                        startActivity(intent);
//
////                        // Chuyển đến MessageFragment khi đăng nhập thành công
////                        Intent messageIntent = new Intent(LoginActivity.this, MessageActivity.class);
////                        messageIntent.putExtra("username", userUsername);
////                        startActivity(messageIntent);
//                    } else {
//                        loginPassword.setError("Thông tin không hợp lệ");
//                        loginPassword.requestFocus();
//                    }
//                } else {
//                    loginUsername.setError("Người Dùng Không Tồn Tại");
//                    loginUsername.requestFocus();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Xử lý lỗi khi truy vấn bị hủy bỏ
//            }
//        });
//    }
//
//
//
//}