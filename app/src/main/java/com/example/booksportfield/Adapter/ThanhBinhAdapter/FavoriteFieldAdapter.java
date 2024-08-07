package com.example.booksportfield.Adapter.ThanhBinhAdapter;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.booksportfield.Activity.Kiet.ChiTietSan;
import com.example.booksportfield.Activity.ThanhBinh.ChangeAvtActivity;
import com.example.booksportfield.Fragment.ThanhBinhFragment.FavoriteFieldItemFragment;
import com.example.booksportfield.Models.Field;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class FavoriteFieldAdapter extends BaseAdapter {

    private Context context;
    private List<Field> favoriteFieldsList;

    RoundedImageView roundedImageView;

    public FavoriteFieldAdapter(Context context, List<Field> favoriteFieldsList) {
        this.context = context;
        this.favoriteFieldsList = favoriteFieldsList;
    }

    @Override
    public int getCount() {
        return favoriteFieldsList.size();
    }

    @Override
    public Object getItem(int position) {
        return favoriteFieldsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Field field = favoriteFieldsList.get(position);
        if (field != null) {
            FavoriteFieldItemFragment fragment = FavoriteFieldItemFragment.newInstance(field);
            fragment.setOnImageClickListener(new FavoriteFieldItemFragment.OnImageClickListener() {
                @Override
                public void onImageClick(Field field) {
                    Intent intent = new Intent(context, ChiTietSan.class);
                    intent.putExtra("object", field);
                    context.startActivity(intent);
                }
            });

            return fragment.onCreateView(LayoutInflater.from(context), parent, null);
        } else {
            return convertView;
        }




    }







}
