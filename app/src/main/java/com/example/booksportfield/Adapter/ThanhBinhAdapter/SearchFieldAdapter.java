package com.example.booksportfield.Adapter.ThanhBinhAdapter;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.booksportfield.Activity.ThanhBinh.ChangeAvtActivity;
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
import java.util.List;
import java.util.Map;

public class SearchFieldAdapter extends ArrayAdapter<Field> {

    private Context mContext;
    private List<Field> mFieldList;

    ImageButton imgField;
    private final boolean[] isLiked = new boolean[1];
    private Field field;

    private String nodeID;

    private ImageButton icon_like;
    private RoundedImageView imgfield;
    private TextView itemName_field, itemDescribe_field, itemType_field;
    private RatingBar rating_bar;

    public SearchFieldAdapter(Context context, List<Field> fieldList) {
        super(context, 0, fieldList);
        mContext = context;
        mFieldList = fieldList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.activity_field_item, parent, false);
        } else {
            listItem = convertView;
        }

        imgfield = listItem.findViewById(R.id.favorite_imgfield);
        icon_like = listItem.findViewById(R.id.favorite_icon_like);
        itemName_field = listItem.findViewById(R.id.favorite_itemName_field);
        itemType_field = listItem.findViewById(R.id.favorite_itemType_field);
        itemDescribe_field = listItem.findViewById(R.id.favorite_itemDescribe_field);
        rating_bar = listItem.findViewById(R.id.favorite_rating_bar);
        isLiked[0] = false;

        field = mFieldList.get(position);
        if (field != null) {
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
        }
        String user = UserSession.getInstance().getUsername();

        imgfield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(onItemClickListener != null)
                {
                    onItemClickListener.onItemClick(field);
                }

            }
        });

        icon_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLiked[0] = !isLiked[0];

                // Chọn ảnh phù hợp dựa trên trạng thái mới
                int imageResource = isLiked[0] ? R.drawable.nolove : R.drawable.love;
                Picasso.get().load(imageResource).into(icon_like);
            }
        });

        return listItem;
    }

    private void getFavoritField(String user, String field, final SearchFieldAdapter.OnGetFavoriteFieldListener listener) {

        DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference("favorites/" + user);

        favoritesRef.child(field).addListenerForSingleValueEvent(new ValueEventListener() {


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

    public interface OnGetFavoriteFieldListener {
        void onGetFavoriteField(boolean isFavorite);
    }

    private void callDetailField() {
        imgfield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(mContext.getApplicationContext(), ChangeAvtActivity.class);
                // Tạo một Bundle để đóng gói dữ liệu cần truyền
                Bundle bundle = new Bundle();
                bundle.putString("key", "value"); // Thêm dữ liệu vào bundle, key là tên của dữ liệu, value là giá trị của dữ liệu

                // Đặt Bundle vào Intent
                intent.putExtras(bundle);
                mContext.startActivity(intent);


            }
        });
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
                favoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {


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

    public interface OnItemClickListener {
        void onItemClick(Field field);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}