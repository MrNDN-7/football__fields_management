package com.example.booksportfield.Activity.DucNhan;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.booksportfield.Fragment.DucNhanFragment.NotificationFragment;
import com.example.booksportfield.R;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        NotificationFragment messageFragment = new NotificationFragment();
        Bundle bundle = new Bundle();
        // Truyền username từ Intent
        String username = getIntent().getStringExtra("username");
        bundle.putString("username", username);
        messageFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, messageFragment);
        transaction.commit();
    }
}
