package com.example.booksportfield.Activity.DucNhan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booksportfield.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgotPassword extends AppCompatActivity {

    private Button btnForgotPassword;
    private EditText edtUsernameForgot, edtEmailForgot;
    private DatabaseReference databaseReference;
    private TextView returnDangNhapText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        thamchieu(); // Đảm bảo rằng phương thức này được gọi để khởi tạo các thành phần giao diện

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("accounts");

        btnForgotPassword.setOnClickListener(v -> reset());
        returnDangNhapText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void thamchieu() {
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        edtEmailForgot = findViewById(R.id.edtEmailForgot);
        edtUsernameForgot = findViewById(R.id.edtUsernameForgot);
        returnDangNhapText=findViewById(R.id.returnDangNhapText);
    }

    public void reset() {
        if (edtEmailForgot == null || edtUsernameForgot == null) {
            Toast.makeText(this, "Giao diện chưa được khởi tạo đúng cách", Toast.LENGTH_SHORT).show();
            return;
        }

        String inputEmail = edtEmailForgot.getText().toString().trim();
        String inputUsername = edtUsernameForgot.getText().toString().trim();

        if (TextUtils.isEmpty(inputEmail) || TextUtils.isEmpty(inputUsername)) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child(inputUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String email = snapshot.child("email").getValue(String.class);
                    if (email != null && email.equals(inputEmail)) {
                        // Email and Username match
                        String newPassword = generateRandomPassword();
                        snapshot.getRef().child("password").setValue(newPassword);
                        sendEmail(inputEmail, newPassword);
                    } else {
                        Toast.makeText(ForgotPassword.this, "Email không đúng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ForgotPassword.this, "Tên đăng nhập không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ForgotPassword.this, "Lỗi kết nối cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String generateRandomPassword() {
        Random random = new Random();
        int password = 100000 + random.nextInt(900000);
        return String.valueOf(password);
    }

    private void sendEmail(String receiverEmail, String newPassword) {
        try {
            String stringSenderEmail = "nhanjp29kt12345@gmail.com";
            String stringPasswordSenderEmail = "gitxqhbbzqxabmxb";
            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));
            mimeMessage.setSubject("Khôi phục mật khẩu");
            mimeMessage.setText("Mật khẩu mới của bạn là: " + newPassword);

            Thread thread = new Thread(() -> {
                try {
                    Transport.send(mimeMessage);

                    runOnUiThread(() ->
                            Toast.makeText(ForgotPassword.this, "Mật khẩu đã được đặt lại và gửi tới email của bạn.", Toast.LENGTH_SHORT).show()
                    );
                    runOnUiThread(() -> {
                        edtEmailForgot.setText("");
                        edtUsernameForgot.setText("");
                    });

                } catch (MessagingException e) {
                    e.printStackTrace();
                    runOnUiThread(() ->
                            Toast.makeText(ForgotPassword.this, "Gửi email thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show()
                    );
                }
            });
            thread.start();

        } catch (AddressException e) {
            e.printStackTrace();
            Toast.makeText(this, "Địa chỉ email không hợp lệ.", Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi gửi email.", Toast.LENGTH_SHORT).show();
        }
    }
}
