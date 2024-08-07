package com.example.booksportfield.Fragment.DucNhanFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.booksportfield.Adapter.DucNhanAdapter.NotificationAdapter;
import com.example.booksportfield.Models.Notification;
import com.example.booksportfield.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotificationFragment extends Fragment {
    private ArrayList<Notification> messageArrayList;
    private NotificationAdapter messageAdapter;
    private DatabaseReference messagesRef;
    private String currentUserUsername;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        ListView listViewTinNhan = rootView.findViewById(R.id.listViewThongBao);

        messageArrayList = new ArrayList<>();
        messageAdapter = new NotificationAdapter(getActivity(), messageArrayList);
        messagesRef = FirebaseDatabase.getInstance().getReference("notifications");

        Bundle bundle = getArguments();
        if (bundle != null) {
            currentUserUsername = bundle.getString("username");
            Log.d("MessageFragment", "Current username: " + currentUserUsername); // Kiểm tra xem current username đã được đưa qua hay chưa
        }
        if (currentUserUsername != null) {
            Query query = messagesRef.orderByChild("user_id").equalTo(currentUserUsername);

            // Trong phương thức onDataChange của ValueEventListener
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    messageArrayList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Get the message content and sender ID directly
                        String messageContent = snapshot.child("notification_content").getValue(String.class);
                        //String senderId = snapshot.child("sender_id").getValue(String.class);

                        // Convert the message date from String to Date
                        String dateString = snapshot.child("notification_date").getValue(String.class);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Định dạng ngày trong Firebase
                        Date messageDate = null;
                        try {
                            messageDate = dateFormat.parse(dateString);
                        } catch (ParseException e) {
                            Log.e("MessageFragment", "Error parsing date: " + e.getMessage());
                        }

                        // Create a new Message object with the converted date
                        Notification message = new Notification(messageContent, messageDate, currentUserUsername);
                        messageArrayList.add(message);
                    }
                    messageAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("MessageFragment", "Error retrieving messages: " + databaseError.getMessage());
                }
            });


            listViewTinNhan.setAdapter(messageAdapter);
        } else {
            Log.e("MessageFragment", "Error: Current user's username is null");
        }

        return rootView;
    }
}
