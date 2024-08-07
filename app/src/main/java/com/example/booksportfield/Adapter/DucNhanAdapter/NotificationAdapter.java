package com.example.booksportfield.Adapter.DucNhanAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.booksportfield.Models.Notification;
import com.example.booksportfield.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NotificationAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Notification> messageArrayList;

    public NotificationAdapter(Context context, ArrayList<Notification> messageArrayList) {
        this.context = context;
        this.messageArrayList = messageArrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return messageArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        //TextView sender_ID;
        TextView message_CONTENT;
        TextView date_SEND;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.activity_notification_item, null);
            holder = new ViewHolder();
            //holder.sender_ID = convertView.findViewById(R.id.sender_ID);
            holder.message_CONTENT = convertView.findViewById(R.id.message_CONTENT);
            holder.date_SEND = convertView.findViewById(R.id.date_SEND);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Notification message = this.messageArrayList.get(position);
        //holder.sender_ID.setText("Người gửi: " +message.getSender_id());
        holder.message_CONTENT.setText("Nội Dung: " + message.getMessage_content());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = sdf.format(message.getMessage_date());
        holder.date_SEND.setText("Thời gian: "+ dateString);

        // Return the completed view to render on screen
        return convertView;
    }
}
