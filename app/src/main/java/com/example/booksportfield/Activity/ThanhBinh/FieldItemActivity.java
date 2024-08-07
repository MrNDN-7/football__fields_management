package com.example.booksportfield.Activity.ThanhBinh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.booksportfield.Adapter.ThanhBinhAdapter.SearchFieldAdapter;
import com.example.booksportfield.Models.Field;
import com.example.booksportfield.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class FieldItemActivity extends AppCompatActivity {
    private Field field;

    private String nodeID;

    private ImageButton icon_like;
    private RoundedImageView imgfield;
    private TextView itemName_field, itemDescribe_field, itemType_field;
    private RatingBar rating_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_field_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.favorite_pageSPB), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fieldFragment();



    }

    public void fieldFragment() {
        imgfield = findViewById(R.id.favorite_imgfield);
        icon_like = findViewById(R.id.favorite_icon_like);
        itemName_field = findViewById(R.id.favorite_itemName_field);
        itemType_field = findViewById(R.id.favorite_itemType_field);
        itemDescribe_field = findViewById(R.id.favorite_itemDescribe_field);
        rating_bar = findViewById(R.id.favorite_rating_bar);
    }
}