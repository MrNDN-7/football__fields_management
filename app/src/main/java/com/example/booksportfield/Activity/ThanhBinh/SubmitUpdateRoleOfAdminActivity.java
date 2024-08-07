package com.example.booksportfield.Activity.ThanhBinh;

import static android.app.PendingIntent.getActivity;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.booksportfield.Activity.DucNhan.LoginActivity;
import com.example.booksportfield.Adapter.ThanhBinhAdapter.UpdateRoleAdapter;

import com.example.booksportfield.Adapter.ThanhBinhAdapter.UserSession;
import com.example.booksportfield.Models.UpdateRole;
import com.example.booksportfield.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SubmitUpdateRoleOfAdminActivity extends AppCompatActivity {
ListView lvAcceptUpdateRole;
ImageButton logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_submit_update_role_of_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String user = UserSession.getInstance().getUsername();
        lvAcceptUpdateRole = findViewById(R.id.lvAcceptUpdateRole);
        logout = findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
        List<UpdateRole> lsUpdate = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("updaterole");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lsUpdate.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
//                    UpdateRole updateRole = new UpdateRole();
//                    updateRole.setUserId(dataSnapshot.getKey());
//                   updateRole.setPhoneNumber(dataSnapshot.getValue(String.class));
//                   updateRole.setAddressField(dataSnapshot.getValue(String.class));
//                    lsUpdate.add(updateRole);

                   UpdateRole updateRole1 = dataSnapshot.getValue(UpdateRole.class);
                   updateRole1.setUserId(dataSnapshot.getKey());


                   lsUpdate.add(updateRole1);
                }

                if(lsUpdate != null)
                {
                    UpdateRoleAdapter ud = new UpdateRoleAdapter((Context) SubmitUpdateRoleOfAdminActivity.this, lsUpdate);
                    lvAcceptUpdateRole.setAdapter(ud);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void logoutUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đăng xuất");
        builder.setMessage("Bạn có muốn đăng xuất khỏi ứng dụng?");
        builder.setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xóa thông tin đăng nhập hoặc thực hiện các tác vụ đăng xuất khác
                // ...

                // Đóng tất cả các Activity và trở về màn hình đăng nhập hoặc màn hình chính
                Intent intent = new Intent(SubmitUpdateRoleOfAdminActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity() ;
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Không làm gì cả, đóng hộp thoại
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}