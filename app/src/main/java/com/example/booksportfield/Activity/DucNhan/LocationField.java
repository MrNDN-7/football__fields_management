package com.example.booksportfield.Activity.DucNhan;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.booksportfield.Activity.Kiet.ChiTietSan;
import com.example.booksportfield.MainActivity;
import com.example.booksportfield.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class LocationField extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    private SearchView mapSearchView;
    private ImageButton imageButtonThoatMap;

    private Button btnTrack;

    String location;
    String tolocation;
    String currentlocation;

    private EditText txtcurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_field);
        tolocation = getIntent().getStringExtra("tolocation");

        // Ánh xạ
        mapSearchView = findViewById(R.id.mapSearch);
        imageButtonThoatMap = findViewById(R.id.imageButtonThoatMap);
        btnTrack = findViewById(R.id.btnTrack);
        txtcurrentLocation = findViewById(R.id.txtcurrentLocation);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(LocationField.this);

        btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentlocation = txtcurrentLocation.getText().toString();
                if (currentlocation.equals("")) {
                    Toast.makeText(LocationField.this, "Hãy nhập địa chỉ của bạn", Toast.LENGTH_SHORT).show();
                } else {
                    getTrack(currentlocation, tolocation);
                }
            }
        });

        ThoatSearchMap();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setCompassEnabled(true);
        myMap.getUiSettings().setZoomGesturesEnabled(false);
        myMap.getUiSettings().setScrollGesturesEnabled(false);

        // Gán giá trị từ tolocation cho location
        location = tolocation;
        List<Address> addressList = null;
        if (location != null) {
            Geocoder geocoder = new Geocoder(LocationField.this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                myMap.addMarker(new MarkerOptions().position(latLng).title(location));
                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            } else {
                Toast.makeText(LocationField.this, "Không thể tìm thấy địa chỉ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void ThoatSearchMap() {
        imageButtonThoatMap.setOnClickListener(v -> finish());
    }

    public void getTrack(String from, String to) {
        try {
            Uri uri = Uri.parse("https://www.google.com/maps/dir/" + from + "/" + to);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException exception) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}