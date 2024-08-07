package com.example.booksportfield.Activity.TuanAnh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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

import com.example.booksportfield.Adapter.ThanhBinhAdapter.UserSession;
import com.example.booksportfield.Adapter.TuanAnhAdapter.ImageAdapter;
import com.example.booksportfield.Models.Field;
import com.example.booksportfield.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Activity_Add_Field extends AppCompatActivity {
    private ImageButton btnBack;
    private Button btnAddField;
    private EditText edtNameField;
    private EditText edtAddress;
    private EditText edtDes;
    private EditText edtPrice;
    private EditText edtTypeField;

    //Xử lý image
    private static final int REQUEST_CODE = 100;
    private Button btnChooseImages, btnUploadImages;
    private RecyclerView rvImages;
    private ImageAdapter imageAdapter;
    private List<Uri> selectedImageUris;
    private int countImage = 0;

    private String username;

    private int soLuongSan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        //Lấy dữ liệu từ intetnt
        username = intent.getStringExtra("keyString");

        setContentView(R.layout.activity_add_field);
        btnBack = findViewById(R.id.btnBackAddField);
        btnAddField = findViewById(R.id.btnAddField);
        btnAddField = findViewById(R.id.btnAddField);
        edtNameField = findViewById(R.id.edtNameField);
        edtAddress = findViewById(R.id.edtAddress);
        edtDes = findViewById(R.id.edtDes);
        edtPrice = findViewById(R.id.edtPrice);
        edtTypeField = findViewById(R.id.edtTypeField);

        //Xử lý image
        btnChooseImages = findViewById(R.id.btnChooseImages);
        rvImages = findViewById(R.id.rvImages);

        imageAdapter = new ImageAdapter();
        rvImages.setAdapter(imageAdapter);
        rvImages.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));



        selectedImageUris = new ArrayList<>();

        btnChooseImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImageUris.clear();
                imageAdapter.setImages(selectedImageUris);
                openImageChooser();
            }
        });

        //Thêm thong tin
        btnAddField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String name = edtNameField.getText().toString();
                String address = edtAddress.getText().toString();
                String description = edtDes.getText().toString();
                double price ;
                int fieldType;

                if (name.isEmpty() || address.isEmpty() || description.isEmpty()) {
                    Toast.makeText(Activity_Add_Field.this, "Hãy điền các nội dung còn thiếu", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    price = Double.parseDouble(edtPrice.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(Activity_Add_Field.this, "Invalid price", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    fieldType = Integer.parseInt(edtTypeField.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(Activity_Add_Field.this, "Invalid field type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (fieldType != 5 && fieldType != 7 && fieldType != 11) {
                    Toast.makeText(Activity_Add_Field.this, "Chỉ hộ trợ các loại sân 5, 7, 11", Toast.LENGTH_SHORT).show();
                    return;
                }

                Field field = new Field();//name, price, fieldType, description, address, "tuananh" ); //username);
                field.setName(name);
                field.setPrice((int) price);
                field.setField_type(fieldType);
                field.setDescription(description);
                field.setAddress(address);

                field.setField_owner(UserSession.getInstance().getUsername());

                if (!selectedImageUris.isEmpty()) {
                    for (Uri imageUri : selectedImageUris) {
                        uploadImageToFirebase(imageUri,field);
                        countImage = 0;
                    }
                } else {
                    Toast.makeText(Activity_Add_Field.this, "Please choose images first", Toast.LENGTH_SHORT).show();
                    return;
                }

                onClickAddItem(field);
                onBackPressed();
            }
        });

        //Btn back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý khi nút Back được bấm
                onBackPressed();
            }
        });
    }

    //Xu ly them anh vao
    private void uploadImageToFirebase(Uri imageUri, Field field) {
        if (imageUri != null) {
            String fileName = UUID.randomUUID().toString() + ".jpg";

            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("imagesField/" + fileName);

            storageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    saveImageUrlToDatabase(imageUrl,field);
                                }
                            });
                            Toast.makeText(Activity_Add_Field.this, "Tải ảnh thành công", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Activity_Add_Field.this, "Tải ảnh thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void saveImageUrlToDatabase(String imageUrl, Field field) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("soccer_fields");

        String key = String.valueOf(countImage++);
        myRef.child(field.getNodeID()).child("image").child(key).setValue(imageUrl);
    }

    //Xu ly add thong tin
// Xu ly add thong tin
    void onClickAddItem(Field item) {
        // Lấy số lượng trường từ cơ sở dữ liệu
        Activity_Add_Field.getFieldCount(new Activity_Add_Field.FieldCountListener() {
            @Override
            public void onFieldCountLoaded(int count) {
                // Sử dụng số lượng trường để tạo mã mới
                String keyItemRandom =  String.valueOf (count + 1); // Tạo mã mới bằng cách sử dụng số lượng trường + 1

                // Thiết lập mã cho trường mới
                item.setNodeID(keyItemRandom);

                // Thêm trường mới vào cơ sở dữ liệu
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("soccer_fields");
                myRef.child(keyItemRandom).setValue(item, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            // Xử lý khi thêm thành công
                            Toast.makeText(Activity_Add_Field.this, "Thêm sân mới thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            // Xử lý khi có lỗi xảy ra trong quá trình thêm
                            Toast.makeText(Activity_Add_Field.this, "Lỗi khi thêm sân mới: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onFieldCountError(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình lấy số lượng trường
                Toast.makeText(Activity_Add_Field.this, "Lỗi khi đọc số lượng trường: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface FieldCountListener {
        void onFieldCountLoaded(int count);
        void onFieldCountError(DatabaseError databaseError);
    }

    public static void getFieldCount(FieldCountListener listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fieldsRef = database.getReference("soccer_fields");

        fieldsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Lấy số lượng trường từ dataSnapshot và chuyển đổi thành int
                int count = (int) dataSnapshot.getChildrenCount();
                listener.onFieldCountLoaded(count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc số lượng trường
                listener.onFieldCountError(databaseError);
            }
        });
    }

    //Xu ly nut chon anh
    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Images"), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
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