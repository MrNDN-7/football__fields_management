package com.example.booksportfield;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.booksportfield.Activity.Kiet.BaseActivity;
import com.example.booksportfield.Adapter.Kiet.ChiTietSanAdapter;
import com.example.booksportfield.Adapter.Kiet.SliderAdapter;
import com.example.booksportfield.Fragment.SliderItems;
import com.example.booksportfield.Models.Field;
import com.example.booksportfield.Models.Comment;
import com.example.booksportfield.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }
//    private void initComment(){
//        DatabaseReference data = database.getReference("comments");
//        ArrayList<Comment> comments = new ArrayList<>();
//        data.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    for(DataSnapshot issue :snapshot.getChildren()){
//                        comments.add(issue.getValue(Comment.class));
//                        Log.d("Comment", String.valueOf(comments.size()));
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }
//    private void initFields(){
//        DatabaseReference data = database.getReference("soccer_fields");
//        binding.progressBarField.setVisibility(View.VISIBLE);
//        ArrayList<ChiTiet> items = new ArrayList<>();
//        data.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    for(DataSnapshot issue :snapshot.getChildren()){
//                        ChiTiet chiTiet = issue.getValue(ChiTiet.class);
//                        chiTiet.setField_id(issue.getKey());
//                        items.add(chiTiet);
//
//                    }
//                    if(!items.isEmpty())
//                    {
//                        binding.recyclerView2.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
//                        binding.recyclerView2.setAdapter(new ChiTietSanAdapter(items));
//
//                        binding.progressBarField.setVisibility(View.GONE);
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//    private void initBanner(){
//        DatabaseReference data = database.getReference("soccer_fields");
//        binding.progressBarSearch.setVisibility(View.VISIBLE);
//        ArrayList<SliderItems> items = new ArrayList<>();
//        data.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    for(DataSnapshot issue :snapshot.getChildren()){
//                        ChiTiet field = issue.getValue(ChiTiet.class); // Lấy URL hình ảnh từ Firebase
//                        SliderItems sliderItem = new SliderItems(); // Tạo đối tượng SliderItems từ URL
//                        sliderItem.setUrl(field.getImage().get(0));
//                        items.add(sliderItem);
//
//                    }
//                    banners(items);
//                    binding.progressBarSearch.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//    public void banners(ArrayList<SliderItems> items){
//        binding.viewPage2.setAdapter(new SliderAdapter(items, binding.viewPage2));
//        binding.viewPage2.setClipToPadding(false);
//        binding.viewPage2.setClipChildren(false);
//        binding.viewPage2.setOffscreenPageLimit(3);
//        binding.viewPage2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
//
//        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
//        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
//
//        binding.viewPage2.setPageTransformer(compositePageTransformer);
//    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chi_tiet_san);
//
//        viewPager2 = findViewById(R.id.slider);
//        imageList = new ArrayList<>();
//
//        // Thay thế 'data' bằng reference đến cơ sở dữ liệu Firebase của bạn
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//
//        // Lấy dữ liệu từ Firebase
//        databaseReference.child("soccer-fields").child("field1").child("image").child("image1").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String imageUrl = dataSnapshot.getValue(String.class);
//                if (imageUrl != null) {
//                    imageList.add(new Image_Slider(imageUrl));
//                    // Sau khi thêm dữ liệu vào danh sách, cần cập nhật Adapter
//                    adapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e("firebase", "Error getting data", databaseError.toException());
//            }
//        });
//
//        // Thêm một hình ảnh mặc định (nếu cần)
//        imageList.add(new Image_Slider("https://firebasestorage.googleapis.com/v0/b/booksportfield-44840.appspot.com/o/memeAvt.jpg?alt=media&token=7509b10c-a0e6-45e6-834a-d203fe480536"));
//
//        adapter = new ImageAdapter(imageList, viewPager2);
//        viewPager2.setAdapter(adapter);
//    }

}