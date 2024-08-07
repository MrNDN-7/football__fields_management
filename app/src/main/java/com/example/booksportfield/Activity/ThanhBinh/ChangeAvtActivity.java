package com.example.booksportfield.Activity.ThanhBinh;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.booksportfield.Adapter.ThanhBinhAdapter.UserSession;
import com.example.booksportfield.Models.Account;
import com.example.booksportfield.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import android.Manifest;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChangeAvtActivity extends AppCompatActivity {
    private Button btnUploadImage, btnChooseImage;
    private ImageButton changeAvtBack;

    private CircleImageView ivAvatar;

    Uri uriImage ;
    Uri selectedImageUri;

    private static final int REQUEST_IMAGE_PICKER = 1000;
    private static final int REQUEST_PERMISSION_CODE = 999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_avt);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String user = UserSession.getInstance().getUsername();
//        loadDataAccount(user);
        changeAvtViews(user);
        btnBackAccountOnClick();
        if (getIntent() != null && getIntent().hasExtra("loadDataAccount")) {
            loadDataAccount(user);
        }
        onClickUploadImage();

    }

    private void changeAvtViews(String user) {
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        ivAvatar = findViewById(R.id.ivAvatar);
        changeAvtBack = findViewById(R.id.changeAvtBack);
        onClickBtnUploadImage(user);
    }

    private void btnBackAccountOnClick() {
        changeAvtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeAvtActivity.this, MainActivity.class);
                intent.putExtra("openAccountFragment", true);
                startActivity(intent);
                finish();

            }
        });
    }

    Account accInfor = new Account();

    private void loadDataAccount(String user) {
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("accounts/" + user);
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    accInfor.setUsername(user);
                    accInfor.setName(snapshot.child("name").getValue(String.class));
                    accInfor.setPhoneNumber(snapshot.child("phoneNumber").getValue(String.class));
                    accInfor.setEmail(snapshot.child("email").getValue(String.class));
                    accInfor.setDateOfBirth(snapshot.child("dateOfBirth").getValue(String.class));
                    accInfor.setGender(snapshot.child("gender").getValue(String.class));
                    accInfor.setRole(snapshot.child("role").getValue(String.class));
                    accInfor.setUrlImage(snapshot.child("urlImage").getValue(String.class));
                    accInfor.setPassword(snapshot.child("password").getValue(String.class));
                    setChangeAvt();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void setChangeAvt() {

        String urlImage = accInfor.getUrlImage();
        Picasso.get().load(urlImage).into(ivAvatar);
    }

    private void onClickUploadImage() {
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangeAvtActivity.this, "load image", Toast.LENGTH_SHORT).show();

                if (ContextCompat.checkSelfPermission(ChangeAvtActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ChangeAvtActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
                } else {
                    openImagePicker();
                }

            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_PICKER);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            Picasso.get().load(selectedImageUri).into(ivAvatar);
            // Set the selected image to your CircleImageView
            // circleImageView.setImageURI(selectedImageUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Ứng dụng cần quyền truy cập vào ảnh để hoạt động.", Toast.LENGTH_SHORT).show();

            }
        }
    }



    private void onClickBtnUploadImage(String user) {
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedImageUri != null)
                {
                    uploadImageToFirebaseStorage(selectedImageUri, user);
                    Log.d("URI", "uri " + selectedImageUri);
                }
                else {
                    Log.d("URI", "không tìm thấy uri");

                }

            }
        });




    }

    private void uploadImageToFirebaseStorage(Uri uriImage, String user) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String fileName = System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child("images/" + fileName);

        UploadTask uploadTask = imageRef.putFile(uriImage);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String urlImage = uri.toString();
                        uploadImageUrlToRealtimeDatabase(urlImage, user);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Xử lý lỗi nếu có
            }
        });
    }

    private void uploadImageUrlToRealtimeDatabase(String urlImage, String user) {
        DatabaseReference dateRef = FirebaseDatabase.getInstance().getReference("accounts/" + user);
        Map<String, Object> updates = new HashMap<>();
        updates.put("urlImage", urlImage);

        dateRef.updateChildren(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Xử lý lỗi nếu có
                    }
                });
        Toast.makeText(this, "Cập nhật ảnh đại diện thành công.", Toast.LENGTH_SHORT).show();

    }

    private Uri getImage(CircleImageView circleImageView) {
        circleImageView.setDrawingCacheEnabled(true);
        circleImageView.buildDrawingCache();
        Bitmap bitmap = circleImageView.getDrawingCache();
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageData = baos.toByteArray();

            String tempFilePath = Environment.getExternalStorageDirectory() + File.separator + "temp_image.jpg";
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(tempFilePath);
                fos.write(imageData);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File imageFile = new File(tempFilePath);
            return Uri.fromFile(imageFile);
        } else {
            return null;
        }
    }
}