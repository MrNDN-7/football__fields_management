package com.example.booksportfield.Activity.ThanhBinh;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.booksportfield.Adapter.ThanhBinhAdapter.UserSession;
import com.example.booksportfield.Fragment.ThanhBinhFragment.AccountFragment;
import com.example.booksportfield.Fragment.ThanhBinhFragment.FavoriteFieldFragment;
import com.example.booksportfield.Fragment.ThanhBinhFragment.HomeFragment;
import com.example.booksportfield.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    private String username;

    private static String usernameMainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        username = getIntent().getStringExtra("username");
        username = UserSession.getInstance().getUsername();

        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        homeFragment.setArguments(bundle);
        favoriteFieldFragment.setArguments(bundle);
        accountFragment.setArguments(bundle);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.bottomHome);

//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.flFragment, new HomeFragment())
//                .commit();
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.flFragment, new AccountFragment())
//                .commit();

//        if (getIntent() != null && getIntent().hasExtra("username")) {
//            callAccountFragment();
//        }

    }

    HomeFragment homeFragment = new HomeFragment();
    AccountFragment accountFragment = new AccountFragment();

    FavoriteFieldFragment favoriteFieldFragment = new FavoriteFieldFragment();



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        if (itemId == R.id.bottomHome ) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, homeFragment)
                    .commit();
            return true;
        }else if (itemId == R.id.bottomLike) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, favoriteFieldFragment)
                    .commit();
            return true;
//        }else if (itemId == R.id.bottomHistory) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.flFragment, accountFragment)
//                    .commit();
//            return true;
        }
        else if (itemId == R.id.bottomAccount) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, accountFragment)
                    .commit();
            return true;
        }

        return false;
    }

    public void callAccountFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AccountFragment newFragment = new AccountFragment();
        fragmentTransaction.replace(R.id.flFragment, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottomAccount);
    }
}