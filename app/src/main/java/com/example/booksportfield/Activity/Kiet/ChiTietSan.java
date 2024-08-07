package com.example.booksportfield.Activity.Kiet;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import android.os.Bundle;

import com.example.booksportfield.Adapter.Kiet.SliderAdapter;
import com.example.booksportfield.Fragment.CommentFragment;
import com.example.booksportfield.Fragment.SliderItems;
import com.example.booksportfield.Fragment.ThongTinFragment;
import com.example.booksportfield.Models.Field;
import com.example.booksportfield.Models.Comment;
import com.example.booksportfield.databinding.ActivityChiTietSanBinding;

import java.util.ArrayList;
import java.util.List;

public class ChiTietSan extends BaseActivity {
    ActivityChiTietSanBinding binding;
    private Field object;
    ArrayList<Comment> comments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChiTietSanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getBundles();
        banners();
        setUpViewPager();
    }
    private void getBundles(){
        object = (Field) getIntent().getSerializableExtra("object");

//        binding.txtAddress.setText(object.getAddress());
//        binding.txtName.setText(object.getName());
//        binding.txtOwner.setText(object.getField_owner());
//        binding.txtPrice.setText("vnđ "+object.getPrice());
//        binding.txtRating.setText(""+object.getRating());
//        binding.ratingBar.setRating(object.getRating());
        binding.btnReturn.setOnClickListener(v -> finish());
//        binding.btnOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ChiTietSan.this, DatSanActivity.class);
//                intent.putExtra("object", object);
//                ChiTietSan.this.startActivity(intent);
//
//
//            }
//        });
    }

    public void banners(){
        ArrayList<SliderItems> items = new ArrayList<>();
        for(int i = 0; i< object.getImage().size();i++)
            items.add(new SliderItems(object.getImage().get(i)));
        binding.viewPage2.setAdapter(new SliderAdapter(items, binding.viewPage2));
        binding.viewPage2.setClipToPadding(false);
        binding.viewPage2.setClipChildren(false);
        binding.viewPage2.setOffscreenPageLimit(3);
        binding.viewPage2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        binding.viewPage2.setPageTransformer(compositePageTransformer);
    }
    private void setUpViewPager()
    {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        ThongTinFragment tab1 = new ThongTinFragment();
        CommentFragment tab2 = new CommentFragment();

        Bundle bundle = new Bundle();
//        bundle.putString("Address", object.getAddress());
//        bundle.putString("Name", object.getName());
//        bundle.putString("Owner", object.getField_owner());
//        bundle.putString("Price", "vnđ " + object.getPrice());
//        bundle.putFloat("Rating", object.getRating());
//        bundle.putStringArrayList("Image", object.getImage());
        bundle.putSerializable("object", object);


        tab1.setArguments(bundle);
        tab2.setArguments(bundle);

        adapter.addFrag(tab1, "Thông tin");
        adapter.addFrag(tab2, "Đánh Giá");

        binding.viewPage.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPage);
        ;
        if(getIntent().getSerializableExtra("isCmt") != null )
        {
            boolean isCmt = (boolean) getIntent().getSerializableExtra("isCmt");
            if(isCmt)
                binding.viewPage.setCurrentItem(1);
        }


    }
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(@NonNull FragmentManager fm)
        {
            super(fm);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
        public void addFrag(Fragment fm, String title){
            fragmentList.add(fm);
            fragmentTitleList.add(title);

        }
        @Override
        public CharSequence getPageTitle(int position){
            return fragmentTitleList.get(position);
        }
    }

}