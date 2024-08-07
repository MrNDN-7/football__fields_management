package com.example.booksportfield.Adapter.ThanhBinhAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.booksportfield.Models.UpdateRole;
import com.example.booksportfield.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateRoleAdapter extends BaseAdapter {

    private List<UpdateRole> mData;
    private LayoutInflater mInflater;

    public UpdateRoleAdapter(Context context, List<UpdateRole> data) {
        mData = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0; // Handle potential null list
    }

    @Override
    public Object getItem(int position) {
        return mData != null && position >= 0 && position < mData.size() ? mData.get(position) : null; // Handle potential null list and out-of-bounds access
    }

    @Override
    public long getItemId(int position) {
        return position; // Consider using a unique identifier in UpdateRole if needed
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_item_submit_update_role, null);
            holder = new ViewHolder();
            holder.textViewName = convertView.findViewById(R.id.editUsername);
            holder.edtAddress = convertView.findViewById(R.id.edtAddress);
            holder.edtPhoneNumber = convertView.findViewById(R.id.edtPhoneNumber);
            holder.btnAccept = convertView.findViewById(R.id.btnAccept);

            holder.btnCancel = convertView.findViewById(R.id.btnCancel);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String username, addressField, phonenumber;
        username  = "";


        // Set data safely (assuming UpdateRole has appropriate getters)
        UpdateRole updateRole = mData != null ? mData.get(position) : null;
        if (updateRole != null) {
            username = updateRole.getUserId();
            addressField = updateRole.getAddressField();
            phonenumber = updateRole.getPhoneNumber();
            holder.textViewName.setText(username);  // Assuming getName() exists
            holder.edtAddress.setText(addressField);  // Assuming getAddress() exists
            holder.edtPhoneNumber.setText(phonenumber);  // Assuming getPhoneNumber() exists

        } else {
            // Handle null data case (optional: clear text or indicate error)
        }

        String finalUsername = username;
        if(!username.isEmpty())
        {
            holder.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("accounts/" + finalUsername);
                    Map<String , Object> updates = new HashMap<>();
                    updates.put("role", "ownerField");
                    updates.put("phoneNumber", updateRole.getPhoneNumber());
                    databaseReference.updateChildren(updates);

                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("updaterole/"+ finalUsername);
                    databaseReference1.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(mInflater.getContext(), "Đã cập nhật quyền tài khoản: " + finalUsername, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });

            // Xử lý sự kiện khi click nút Delete
            holder.btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("updaterole/"+ finalUsername);
                    databaseReference1.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(mInflater.getContext(), "Đã hủy cập nhật quyền tài khoản: " + finalUsername, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });
        }

        return convertView;
    }

    static class ViewHolder {
        EditText textViewName;
        EditText edtAddress;
        EditText edtPhoneNumber;

        Button btnAccept, btnCancel;
    }
}
