package com.example.booksportfield.Activity.DucNhan;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.booksportfield.Adapter.ThanhBinhAdapter.UserSession;
import com.example.booksportfield.MainActivity;
import com.example.booksportfield.Models.Account;
import com.example.booksportfield.Adapter.DucNhanAdapter.AccountAdapter;
import com.example.booksportfield.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    private ListView listView;
    private AccountAdapter adapter;
    private ArrayList<Account> accountArrayList;
    private ArrayList<Account> originalAccountArrayList; // Danh sách ban đầu
    private String loggedInUsername;
    private SearchView searchView;
    private ImageButton imageButtonThoatTinNhan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Lấy thông tin đăng nhập
        loggedInUsername = UserSession.getInstance().getUsername();

        // Ánh xạ các thành phần UI
        listView = findViewById(R.id.listViewAccount);
        searchView = findViewById(R.id.searchViewAccount);
        imageButtonThoatTinNhan=findViewById(R.id.imageButtonThoatTinNhan);

        // Khởi tạo ArrayList cho danh sách tài khoản
        accountArrayList = new ArrayList<>();
        originalAccountArrayList = new ArrayList<>(); // Khởi tạo danh sách ban đầu

        // Khởi tạo Adapter cho ListView
        adapter = new AccountAdapter(UserActivity.this, accountArrayList, loggedInUsername);
        listView.setAdapter(adapter);

        // Lắng nghe sự kiện khi thay đổi nội dung của SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter danh sách tài khoản khi có sự thay đổi trong SearchView
                if (TextUtils.isEmpty(newText)) {
                    // Khôi phục lại danh sách ban đầu khi search view trống
                    accountArrayList.clear();
                    accountArrayList.addAll(originalAccountArrayList);
                    adapter.notifyDataSetChanged();
                } else {
                    // Filter danh sách tài khoản theo tên
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });

        // Lấy danh sách tài khoản từ Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("accounts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                accountArrayList.clear();
                originalAccountArrayList.clear(); // Xóa danh sách ban đầu trước khi cập nhật
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String accountUsername = snapshot.getKey();
                    Account account = snapshot.getValue(Account.class);
                    if (account != null && !accountUsername.equals(loggedInUsername) && !account.getRole().equals("admin")) {
                        account.setUsername(accountUsername);
                        accountArrayList.add(account);
                        originalAccountArrayList.add(account); // Thêm vào danh sách ban đầu
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });
        imageButtonThoatTinNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}