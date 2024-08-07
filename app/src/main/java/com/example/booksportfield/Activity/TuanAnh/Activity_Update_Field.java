package com.example.booksportfield.Activity.TuanAnh;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.booksportfield.Adapter.TuanAnhAdapter.ImageAdapter;
import com.example.booksportfield.Adapter.TuanAnhAdapter.ImageGetAdapter;
import com.example.booksportfield.Models.Field;
import com.example.booksportfield.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Activity_Update_Field extends AppCompatActivity {
    private Field fieldCurrent;
    private Button btnUpdateField;
    private Button btnChoseImage;
    private ImageButton btnBack;
    private EditText edtNameField;
    private EditText edtAddress;
    private EditText edtDes;
    private EditText edtPrice;
    private EditText edtTypeField;

    private ImageGetAdapter imageGetAdapter;
    private RecyclerView rvImages;
    private RecyclerView rvImagesUpload;


    //Upload lại image
    private ImageAdapter imageAdapter;
    private List<Uri> selectedImageUris;
    private int countImage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_field);

        fieldCurrent = (Field) getIntent().getSerializableExtra("itemObject");
        initUI();
        getFieldUpdate();

        btnChoseImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                imageAdapter = new ImageAdapter();
                rvImages.setAdapter(imageAdapter);
                rvImages.setLayoutManager(new GridLayoutManager(Activity_Update_Field.this, 1, GridLayoutManager.HORIZONTAL, false));
                selectedImageUris = new ArrayList<>();
                openImageChooser();
            }
        });

        btnUpdateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin cập nhật từ EditText
                String name = edtNameField.getText().toString().trim();
                String address = edtAddress.getText().toString().trim();
                String description = edtDes.getText().toString().trim();
                double price;
                int fieldType;

                if (TextUtils.isEmpty(edtNameField.getText().toString())) {
                    edtNameField.setError("Vui lòng điền tên");
                    return;
                }

                if (TextUtils.isEmpty(edtAddress.getText().toString())) {
                    edtAddress.setError("Vui lòng điền địa chỉ");
                    return;
                }

                if (TextUtils.isEmpty(edtDes.getText().toString())) {
                    edtDes.setError("Vui lòng điền mô tả");
                    return;
                }

                if( TextUtils.isEmpty(edtPrice.getText().toString()))
                {
                    edtPrice.setError("Không được để trống");
                    return;
                }

                if( TextUtils.isEmpty(edtTypeField.getText().toString()))
                {
                    edtTypeField.setError("Không được để trống");
                    return;
                }

                try {
                    price = Double.parseDouble(edtPrice.getText().toString());
                } catch (NumberFormatException e) {
                    edtPrice.setError("Vui lòng điền đúng dạng");
                    return;
                }

                try {
                    fieldType = Integer.parseInt(edtTypeField.getText().toString());
                } catch (NumberFormatException e) {
                    edtTypeField.setError("Vui lòng điền đúng dạng");
                    return;
                }

                if (fieldType != 5 && fieldType != 7 && fieldType != 11) {
                    edtTypeField.setError("Hệ thống chỉ hộ trợ các loại sân 5 , 7 , 11");
                    return;
                }

                // Cập nhật đối tượng Field với thông tin mới
                fieldCurrent.setName(name);
                fieldCurrent.setAddress(address);
                fieldCurrent.setDescription(description);
                fieldCurrent.setPrice((int) price);
                fieldCurrent.setField_type(fieldType);

                // Kiểm tra nếu có hình ảnh mới được chọn
                if (selectedImageUris != null) {
                    // Nếu có hình ảnh mới, tải lên Firebase Storage và lấy URL
                    deleteOldImageUrls(fieldCurrent);
                    //Tải ảnh lên
                    uploadImagesToStorage(selectedImageUris, fieldCurrent);
                } else {
                    // Nếu không có hình ảnh mới, chỉ cập nhật các trường khác
                    updateFieldOnDatabase(fieldCurrent);
                }
                onBackPressed();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý khi nút Back được bấm
                onBackPressed();
            }
        });
    }

//    void UpdateField() {
//
//    }

    private void deleteOldImageUrls(Field field) {
        List<String> oldImageUrls = field.getImage();
        oldImageUrls.clear(); // Xóa tất cả các URL hình ảnh cũ
    }

    private void uploadImagesToStorage(List<Uri> imageUris, Field field) {
        List<String> imageUrls = new ArrayList<>();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("imagesField");

        for (Uri imageUri : imageUris) {
            String fileName = UUID.randomUUID().toString() + ".jpg";
            StorageReference fileRef = storageRef.child(fileName);

            fileRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    imageUrls.add(imageUrl);
                                    if (imageUrls.size() == imageUris.size()) {
                                        field.getImage().addAll(imageUrls);
                                        updateFieldOnDatabase(field);
                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Activity_Update_Field.this, "Tải ảnh thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void updateFieldOnDatabase(Field field) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("soccer_fields");

        String key = field.getNodeID();
        myRef.child(key).setValue(field);
        Toast.makeText(Activity_Update_Field.this, "Cập nhập thành công", Toast.LENGTH_SHORT).show();
    }

    void getFieldUpdate()
    {
        // Thiết lập giá trị ban đầu cho các EditText
        edtNameField.setText(fieldCurrent.getName());
        edtAddress.setText(fieldCurrent.getAddress());
        edtDes.setText(fieldCurrent.getDescription());
        edtPrice.setText(String.valueOf(fieldCurrent.getPrice()));
        edtTypeField.setText(String.valueOf(fieldCurrent.getField_type()));

        imageGetAdapter = new ImageGetAdapter(fieldCurrent.getImage());
        rvImages.setAdapter(imageGetAdapter);
        rvImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    void initUI()
    {
        btnBack = findViewById(R.id.btnBackUpdateField);
        btnUpdateField = findViewById(R.id.btnUpdateField);
        btnChoseImage = findViewById(R.id.btnChoseImageUpdate);
        edtNameField = findViewById(R.id.edtNameField);
        edtAddress = findViewById(R.id.edtAddress);
        edtDes = findViewById(R.id.edtDes);
        edtPrice = findViewById(R.id.edtPrice);
        edtTypeField = findViewById(R.id.edtTypeField);
        rvImages = findViewById(R.id.rvImages);
    }

    //upload image
    //Xu ly nut chon anh
    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Images"), 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            // Xóa các ảnh đã chọn trước đó
            selectedImageUris.clear();

            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    selectedImageUris.add(imageUri);
                }
                imageAdapter.setImages(selectedImageUris);
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                selectedImageUris.add(imageUri);
                imageAdapter.setImages(selectedImageUris);
            }
        }
    }
}