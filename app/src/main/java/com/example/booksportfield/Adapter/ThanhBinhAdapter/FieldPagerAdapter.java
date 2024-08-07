package com.example.booksportfield.Adapter.ThanhBinhAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.booksportfield.Fragment.ThanhBinhFragment.FieldFragment;
import com.example.booksportfield.Models.Field;

import java.util.List;

public class FieldPagerAdapter extends FragmentPagerAdapter {
    private List<Field> fieldList;
    private String user;

    public FieldPagerAdapter(@NonNull FragmentManager fm, List<Field> fieldList, String username) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fieldList = fieldList;
        this.user = username;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Field field = fieldList.get(position);
        return FieldFragment.newInstance(field, user);
    }

    @Override
    public int getCount() {
        return fieldList.size();
    }
}
//
//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        // Lấy URL hình ảnh từ Field tại vị trí position
//        String imageUrl = fieldList.get(position).getImage().get(0);
//
//        // Tạo ImageView để hiển thị hình ảnh
//        ImageView imageView = new ImageView(context);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//
//        // Sử dụng Picasso để tải hình ảnh từ URL và đặt làm hình nền cho ImageView
//        Picasso.get().load(imageUrl).into(imageView);
//
//        // Thêm ImageView vào ViewPager
//        container.addView(imageView);
//
//        return imageView;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        // Loại bỏ ImageView khỏi ViewPager khi không cần thiết
//    }
