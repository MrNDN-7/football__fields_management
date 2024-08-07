package com.example.booksportfield.Adapter.TuanAnhAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksportfield.Models.Field;
import com.example.booksportfield.R;

import java.util.List;


public class FieldManagerAdapter extends RecyclerView.Adapter<FieldManagerAdapter.FieldManagerHolder> {
    private List<Field> mListItem;
    private IClickListener mIClickListener;

    public interface IClickListener{
        void onClickUpDateItem(Field item);
        void onClickSlotTime(Field item);
    }

    public FieldManagerAdapter(List<Field> mListItem, IClickListener listener)
    {
        this.mListItem = mListItem;
        this.mIClickListener = listener;
    }

    @NonNull
    @Override
    public FieldManagerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_field_rentalmanager, parent,false);
        return new FieldManagerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FieldManagerHolder holder, int position) {
        Field item = mListItem.get(position);
        if(item == null)
        {
            return;
        }
        holder.tvName.setText("Tên sân: " + item.getName());
        holder.tvDes.setText("Mô tả: " + item.getDescription());
        holder.tvTypeField.setText("Loại sân : " + item.getField_type());
        holder.tvPrice.setText("Giá thuê : " + String.valueOf(item.getPrice()));
        holder.tvRating.setText(String.valueOf(String.valueOf(item.getRating())));

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mIClickListener.onClickUpDateItem(item);
            }
        });

        holder.btnManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mIClickListener.onClickSlotTime(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListItem!=null)
        {
            return mListItem.size();
        }
        return 0;
    }
    public class FieldManagerHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
//        private TextView tvID;
        private TextView tvDes;
        private TextView tvTypeField;
        private TextView tvPrice;
        private TextView tvRating;
        private Button btnUpdate;
        private Button btnManage;

        public FieldManagerHolder(@NonNull View itemView) {
            super(itemView);
            this.tvName = itemView.findViewById(R.id.txtFieldName);
            this.tvDes = itemView.findViewById(R.id.txtDes);
            this.tvTypeField = itemView.findViewById(R.id.txtTypeField);
            this.tvPrice = itemView.findViewById(R.id.txtPrice);
            this.tvRating = itemView.findViewById(R.id.txtRate);

            this.btnUpdate = itemView.findViewById(R.id.btnUpdate);
            this.btnManage = itemView.findViewById(R.id.btnManage);

        }
    }
}
