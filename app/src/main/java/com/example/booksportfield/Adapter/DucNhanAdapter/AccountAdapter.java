package com.example.booksportfield.Adapter.DucNhanAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.booksportfield.Models.Account;
import com.example.booksportfield.Activity.DucNhan.ChatActivity;
import com.example.booksportfield.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AccountAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private ArrayList<Account> accountArrayList;
    private ArrayList<Account> accountArrayListFiltered;
    private String loggedInUsername;

    public AccountAdapter(Context context, ArrayList<Account> accountArrayList, String loggedInUsername) {
        this.context = context;
        this.accountArrayList = accountArrayList;
        this.accountArrayListFiltered = accountArrayList;
        this.loggedInUsername = loggedInUsername;
    }

    @Override
    public int getCount() {
        return accountArrayListFiltered.size();
    }

    @Override
    public Object getItem(int position) {
        return accountArrayListFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolderAccount {
        ImageView imageAccount;
        TextView nameAccount;
        TextView roleAccount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderAccount viewHolderAccount;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_user_chat_item, parent, false);
            viewHolderAccount = new ViewHolderAccount();
            viewHolderAccount.imageAccount = convertView.findViewById(R.id.imageAccount);
            viewHolderAccount.nameAccount = convertView.findViewById(R.id.nameAccount);
            viewHolderAccount.roleAccount = convertView.findViewById(R.id.roleAccount);
            convertView.setTag(viewHolderAccount);
        } else {
            viewHolderAccount = (ViewHolderAccount) convertView.getTag();
        }

        Account account = this.accountArrayListFiltered.get(position);
        Picasso.get().load(account.getUrlImage()).into(viewHolderAccount.imageAccount);
        viewHolderAccount.nameAccount.setText(account.getName());
        viewHolderAccount.roleAccount.setText(account.getRole());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("name", account.getName());
                intent.putExtra("username", account.getUsername());
                intent.putExtra("imageUrl", account.getUrlImage());
                intent.putExtra("loggedInUsername", loggedInUsername);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                ArrayList<Account> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    // Nếu constraint rỗng, trả về toàn bộ danh sách tài khoản
                    filteredList.addAll(accountArrayList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Account account : accountArrayList) {
                        if (account.getName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(account);
                        }
                    }
                }
                filterResults.values = filteredList;
                filterResults.count = filteredList.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                accountArrayListFiltered.clear();
                accountArrayListFiltered.addAll((ArrayList<Account>) results.values);
                notifyDataSetChanged();
            }
        };
    }
}



//package com.example.booksportfield.NhanAdapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.example.booksportfield.NhanActivity.ChatActivity;
//import com.example.booksportfield.R;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//
//import com.example.booksportfield.Models.Account;
//
//
//public class AccountAdapter extends BaseAdapter {
//    private Context context;
//    private LayoutInflater layoutInflater;
//    private ArrayList<Account> accountArrayList;
//
//    private  String loggedInUsername;
//
//    public AccountAdapter(Context context, ArrayList<Account> accountArrayList, String loggedInUsername)
//    {
//        this.context=context;
//        this.accountArrayList=accountArrayList;
//        layoutInflater = LayoutInflater.from(context);
//        this.loggedInUsername=loggedInUsername;
//    }
//    @Override
//    public int getCount() {
//        return accountArrayList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return accountArrayList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    public class ViewHolderAccount {
//        ImageView imageAccount;
//        TextView nameAccount;
//        TextView roleAccount;
//    }
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolderAccount viewHolderAccount;
//        if (convertView == null) {
//            convertView = layoutInflater.inflate(R.layout.activity_user_chat_item, null);
//            viewHolderAccount = new ViewHolderAccount();
//            viewHolderAccount.imageAccount = convertView.findViewById(R.id.imageAccount);
//            viewHolderAccount.nameAccount = convertView.findViewById(R.id.nameAccount);
//            viewHolderAccount.roleAccount = convertView.findViewById(R.id.roleAccount);
//            convertView.setTag(viewHolderAccount);
//        } else {
//            viewHolderAccount = (ViewHolderAccount) convertView.getTag();
//        }
//
//        Account account = this.accountArrayList.get(position);
//        Picasso.get().load(account.getUrlImage()).into(viewHolderAccount.imageAccount);
//        viewHolderAccount.nameAccount.setText(account.getName());
//        viewHolderAccount.roleAccount.setText(account.getRole());
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Tạo Intent mới
//                Intent intent = new Intent(context, ChatActivity.class);
//                // Thêm thông tin vào Intent
//                intent.putExtra("name", account.getName());
//                intent.putExtra("username", account.getUsername());
//                intent.putExtra("imageUrl", account.getUrlImage());
//                intent.putExtra("loggedInUsername", loggedInUsername);
//
//                // Khởi chạy Activity mới
//                context.startActivity(intent);
//            }
//        });
//        return convertView;
//    }
//
//}
//
