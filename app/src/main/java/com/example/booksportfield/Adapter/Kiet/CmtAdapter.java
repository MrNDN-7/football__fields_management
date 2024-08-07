package com.example.booksportfield.Adapter.Kiet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.booksportfield.Models.Comment;
import com.example.booksportfield.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CmtAdapter extends ArrayAdapter<Comment> {

    private Context context;
    private List<Comment> commentList;


    public CmtAdapter(@NonNull Context context, @NonNull List<Comment> objects) {
        super(context,0,  objects);
        this.commentList = objects;
        this.context=context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.rating_item, parent, false);
        } else {
            listItem = convertView;
        }
        TextView txtCmt = listItem.findViewById(R.id.txtComment);
        if(commentList != null) {
            String cmt = commentList.get(position).getComment();
            txtCmt.setText(cmt);

            TextView rating = listItem.findViewById(R.id.txtRate);
            rating.setText(commentList.get(position).getRating()+"");
            TextView Name = listItem.findViewById(R.id.txtName_user);
            Name.setText(commentList.get(position).getUser_name());
            CircleImageView avt = listItem.findViewById(R.id.imgAvt);
            Picasso.get().load(commentList.get(position).getUser_image()).into(avt);
        }
        return listItem;
    }

}
