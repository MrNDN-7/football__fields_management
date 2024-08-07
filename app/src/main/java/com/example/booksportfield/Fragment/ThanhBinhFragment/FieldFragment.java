package com.example.booksportfield.Fragment.ThanhBinhFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.booksportfield.Activity.Kiet.ChiTietSan;
import com.example.booksportfield.Adapter.ThanhBinhAdapter.UserSession;
import com.example.booksportfield.Models.Field;
import com.example.booksportfield.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class FieldFragment extends Fragment {
    private static final String ARG_FIELD = "arg_field";
    private final boolean[] isLiked = new boolean[1];
    private Field field;

    private String nodeID;

    private ImageButton icon_like;
    private RoundedImageView imgfield;
    private TextView itemName_field, itemDescribe_field, itemType_field;
    private RatingBar rating_bar;
private String user;

    //    public static Fragment newInstance(Field field) {
//        FieldFragment fragment = new FieldFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(ARG_FIELD, (Serializable) field);
//        fragment.setArguments(args);
//        return fragment;
//    }
    public static FieldFragment newInstance(Field field, String username) {
        FieldFragment fragment = new FieldFragment();
        Bundle args = new Bundle();
        args.putSerializable("field", field);
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            field = (Field) getArguments().getSerializable("field");
            user = getArguments().getString("username");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field, container, false);
        fieldFragment(view);
//        if (getArguments() != null) {
//            field = (Field) getArguments().getSerializable("field");
//            user = getArguments().getString("username");
//        }
        user = UserSession.getInstance().getUsername();
        itemName_field.setText(field.getName());
        itemType_field.setText("Sân " + field.getField_type());
        itemDescribe_field.setText(field.getDescription());
        rating_bar.setRating((float) field.getRating());
        if (field.getImage() != null && !field.getImage().isEmpty()) {
            // Lấy URL của hình ảnh đầu tiên từ danh sách
            String firstImageUrl = field.getImage().get(0);
            // Hiển thị hình ảnh đầu tiên trong danh sách
            Picasso.get().load(firstImageUrl).into(imgfield);


        }
        nodeID = field.getNodeID();
//        user = "user1";

        getFavoritField(user, nodeID, new OnGetFavoriteFieldListener() {

            @Override
            public void onGetFavoriteField(boolean isFavorite) {
                isLiked[0] = isFavorite;
                int imageResource = isLiked[0] ? R.drawable.nolove : R.drawable.love;
                Picasso.get().load(imageResource).into(icon_like);
            }
        });


        iconLikeOnClick(user, nodeID);
        callDetailField();

        return view;
    }

    public void fieldFragment(View root) {
        imgfield = root.findViewById(R.id.imgfield);
        icon_like = root.findViewById(R.id.icon_like);
        itemName_field = root.findViewById(R.id.itemName_field);
        itemType_field = root.findViewById(R.id.itemType_field);
        itemDescribe_field = root.findViewById(R.id.itemDescribe_field);
        rating_bar = root.findViewById(R.id.rating_bar);
    }

    private void iconLikeOnClick(String user, String field) {
        icon_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đảo ngược trạng thái thích
                isLiked[0] = !isLiked[0];

                // Chọn ảnh phù hợp dựa trên trạng thái mới
                int imageResource = isLiked[0] ? R.drawable.nolove : R.drawable.love;

                // Đặt ảnh cho icon bằng Picasso
                Picasso.get().load(imageResource).into(icon_like);

                DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference("favorites/" + user);
                favoritesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(field)) {
                            // Nếu có, cập nhật giá trị của trường đó
                            Map<String, Object> updates = new HashMap<>();
                            updates.put(field, isLiked[0]);
                            favoritesRef.updateChildren(updates);
                        } else {
                            // Nếu không, thêm mới trường field với giá trị là isLiked[0]
                            favoritesRef.child(field).setValue(isLiked[0]);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    private void getFavoritField(String user, String field, final OnGetFavoriteFieldListener listener) {

        DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference("favorites/" + user);

        favoritesRef.child(field).addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Kiểm tra xem nếu dataSnapshot không tồn tại
                if (dataSnapshot.exists()) {
                    // Lấy giá trị của trường dữ liệu
                    boolean isFavorite = dataSnapshot.getValue(Boolean.class);
                    listener.onGetFavoriteField(isFavorite);

                } else {
                    listener.onGetFavoriteField(false);
                    // Trường dữ liệu không tồn tại hoặc không có dữ liệu
                    Log.d("Field Data", "Không tồn tại trường dữ liệu có tên là " + field);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu có
                listener.onGetFavoriteField(false);
                Log.e("Firebase Error", "Lỗi khi truy vấn dữ liệu: " + error.getMessage());
            }
        });

    }

    public interface OnGetFavoriteFieldListener   {
        void onGetFavoriteField(boolean isFavorite);
    }

    private void callDetailField() {
        imgfield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChiTietSan.class);

                intent.putExtra("object", field);

                intent.putExtras(intent);
                startActivity(intent);
            }
        });
    }
}