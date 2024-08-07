package com.example.booksportfield.Activity.DucNhan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.booksportfield.Models.Account;
import com.example.booksportfield.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    EditText signupName, signupUsername, signupEmail, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //tham chieu
        thamchieu();
        action_signup();
        action_to_login();

    }
    public void thamchieu()
    {

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton = findViewById(R.id.signup_button);
    }
    public void action_signup()
    {
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("accounts");
                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String username = signupUsername.getText().toString();
                String password = signupPassword.getText().toString();
                String role = "user"; // Role mặc định là user
                String urlImage = "https://firebasestorage.googleapis.com/v0/b/booksportfield-44840.appspot.com/o/images%2F1714503394979.jpg?alt=media&token=14fdabae-753c-42ea-a29d-7826be95639f";

                // Kiểm tra tính hợp lệ của email
                if (!isValidEmail(email)) {
                    Toast.makeText(SignupActivity.this, "Vui lòng nhập địa chỉ email hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra tính hợp lệ của tên, username và password
                if (!isValidName(name)) {
                    Toast.makeText(SignupActivity.this, "Tên không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidUsername(username)) {
                    Toast.makeText(SignupActivity.this, "Username không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidPassword(password)) {
                    Toast.makeText(SignupActivity.this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                    return;
                }

                Account acc = new Account(name, email, password, role, urlImage); // Thêm role vào tài khoản
                reference.child(username).setValue(acc);
                Toast.makeText(SignupActivity.this, "Đăng Ký Tài Khoản Thành Công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    // Phương thức kiểm tra tính hợp lệ của email sử dụng regex
    public static boolean isValidEmail(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Phương thức kiểm tra tính hợp lệ của tên
    public static boolean isValidName(String name) {
        String pattern = "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚÝàáâãèéêìíòóôõùúýĂăĐđĨĩŨũƠơƯưẠạẢảẤấẦầẨẩẪẫẬậẮắẰằẲẳẴẵẶặẸẹẺẻẼẽẾếỀềỂểỄễỆệỈỉỊịỌọỎỏỐốỒồỔổỖỗỘộỚớỜờỞởỠỡỢợỤụỦủỨứỪừỬửỮữỰựỲỳỴỵỶỷỸỹ\\s]+\\s*$";
        return name.matches(pattern);
    }


    // Phương thức kiểm tra tính hợp lệ của username
    public static boolean isValidUsername(String username) {
        return username != null && username.length() >= 4 && username.matches("[a-zA-Z0-9]+");
    }

    // Phương thức kiểm tra tính hợp lệ của password
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public void action_to_login()
    {
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}


//package com.example.booksportfield.NhanActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.booksportfield.R;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import com.example.booksportfield.Models.Account;
//
//public class SignupActivity extends AppCompatActivity {
//    EditText signupName, signupUsername, signupEmail, signupPassword;
//    TextView loginRedirectText;
//    Button signupButton;
//    FirebaseDatabase database;
//    DatabaseReference reference;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signup);
//        //tham chieu
//        thamchieu();
//        action_signup();
//        action_to_login();
//
//    }
//    public void thamchieu()
//    {
//
//        signupName = findViewById(R.id.signup_name);
//        signupEmail = findViewById(R.id.signup_email);
//        signupUsername = findViewById(R.id.signup_username);
//        signupPassword = findViewById(R.id.signup_password);
//        loginRedirectText = findViewById(R.id.loginRedirectText);
//        signupButton = findViewById(R.id.signup_button);
//    }
//    public void action_signup()
//    {
//        signupButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                database = FirebaseDatabase.getInstance();
//                reference = database.getReference("accounts");
//                String name = signupName.getText().toString();
//                String email = signupEmail.getText().toString();
//                String username = signupUsername.getText().toString();
//                String password = signupPassword.getText().toString();
//                String role = "user"; // Role mặc định là user
//                String urlImage="https://firebasestorage.googleapis.com/v0/b/booksportfield-44840.appspot.com/o/images%2F1714503394979.jpg?alt=media&token=14fdabae-753c-42ea-a29d-7826be95639f";
//                Account acc = new Account(name, email, password, role,urlImage); // Thêm role vào tài khoản
//                reference.child(username).setValue(acc);
//                Toast.makeText(SignupActivity.this, "Đăng Ký Tài Khoản Thành Công", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//    public void action_to_login()
//    {
//        loginRedirectText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//}