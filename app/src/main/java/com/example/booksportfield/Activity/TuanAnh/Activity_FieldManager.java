package com.example.booksportfield.Activity.TuanAnh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.booksportfield.Adapter.ThanhBinhAdapter.UserSession;
import com.example.booksportfield.Adapter.TuanAnhAdapter.FieldManagerAdapter;
import com.example.booksportfield.Models.Field;
import com.example.booksportfield.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Activity_FieldManager extends AppCompatActivity {
    private RecyclerView rcvFieldManager;
    private List<Field> mListField;
    private FieldManagerAdapter mItemAdapter;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_field_rentalmanager);
        btnBack = findViewById(R.id.btnBackFieldManager);

        rcvFieldManager = findViewById(R.id.rcv_field);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvFieldManager.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rcvFieldManager.addItemDecoration(dividerItemDecoration);

        mListField = new ArrayList<>();
        mItemAdapter = new FieldManagerAdapter(mListField, new FieldManagerAdapter.IClickListener(){
            @Override
            public void onClickUpDateItem(Field item) {
                Intent intentUpdateForm = new Intent(Activity_FieldManager.this, Activity_Update_Field.class);
                intentUpdateForm.putExtra("itemObject", item);

                startActivity(intentUpdateForm);
            }

            @Override
            public void onClickSlotTime(Field item) {
                Intent intentUpdateForm = new Intent(Activity_FieldManager.this, Activity_TimeSlot.class);
                intentUpdateForm.putExtra("itemObject",item );

                startActivity(intentUpdateForm);
            }
        });

        //Nút back cho buttn
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý khi nút Back được bấm
                onBackPressed();
            }
        });

        rcvFieldManager.setAdapter(mItemAdapter);

        interactWithItems();
    }

    private void interactWithItems()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("soccer_fields");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Field item = snapshot.getValue(Field.class);
                if(item != null)
                {
                    item.setNodeID(snapshot.getKey());
                    if(item.getField_owner().equals(UserSession.getInstance().getUsername()))
                    {
                        mListField.add(item);
                        mItemAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}