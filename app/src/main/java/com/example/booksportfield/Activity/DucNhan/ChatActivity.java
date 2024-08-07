    package com.example.booksportfield.Activity.DucNhan;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.EditText;
    import android.widget.ImageButton;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.ActionBar;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.cardview.widget.CardView;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.booksportfield.Adapter.DucNhanAdapter.ChatAdapter;
    import com.example.booksportfield.Models.Chat;
    import com.example.booksportfield.R;
    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;
    import com.squareup.picasso.Picasso;

    import java.util.ArrayList;
    import java.util.Date;

    import de.hdodenhof.circleimageview.CircleImageView;
    public class ChatActivity extends AppCompatActivity {
        String reciverimg, reciverUserName,reciverName,SenderUID,SenderUserName;


        //Layout
        CircleImageView profile;
        TextView reciverNName;
        FirebaseDatabase database;

        CardView sendbtn;
        EditText textmsg;

        ImageButton imageButtonThoatChat;

        //image
        public  static String senderImg;
        public  static String reciverIImg;


        //room chat
        String senderRoom,reciverRoom;

        //View
        RecyclerView msglistView;
        ArrayList<Chat> chatArrayList;
        ChatAdapter chatAdapter;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_chat);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }


            //ánh xạ
            sendbtn = findViewById(R.id.sendbtnn);
            textmsg = findViewById(R.id.textmsg);
            reciverNName = findViewById(R.id.recivername);
            profile = findViewById(R.id.profileimgg);
            msglistView = findViewById(R.id.msglistView);
            imageButtonThoatChat=findViewById(R.id.imageButtonThoatChat);

            //database
            database = FirebaseDatabase.getInstance();

            //lấy dữ liệu truyền qua intent
            reciverName = getIntent().getStringExtra("name");
            reciverimg = getIntent().getStringExtra("imageUrl");
            reciverUserName = getIntent().getStringExtra("username");
            SenderUserName = getIntent().getStringExtra("loggedInUsername");
            //set dữ liệu cho đối tượng
            Picasso.get().load(reciverimg).into(profile);
            reciverNName.setText("" + reciverName);


            chatArrayList = new ArrayList<>();


            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setStackFromEnd(true);
            msglistView.setLayoutManager(linearLayoutManager);
            chatAdapter= new ChatAdapter(ChatActivity.this,chatArrayList,SenderUserName);
            msglistView.setAdapter(chatAdapter);




            SenderUID =  SenderUserName;

            senderRoom = SenderUID + reciverUserName;
            reciverRoom = reciverUserName + SenderUID;



            DatabaseReference  reference = database.getReference().child("accounts").child(SenderUID);
            DatabaseReference  chatreference = database.getReference().child("chats").child(senderRoom).child("messages");


            chatreference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    chatArrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Chat message = dataSnapshot.getValue(Chat.class);
                        chatArrayList.add(message);
                    }
                    chatAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    senderImg= snapshot.child("urlImage").getValue().toString();
                    reciverIImg=reciverimg;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            sendbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String message = textmsg.getText().toString();
                    if (message.isEmpty()){
                        Toast.makeText(ChatActivity.this, "Enter The Message First", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    textmsg.setText("");
                    Date date = new Date();
                    Chat messagess = new Chat(message,SenderUID,date.getTime());

                    database=FirebaseDatabase.getInstance();
                    database.getReference().child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    database.getReference().child("chats")
                                            .child(reciverRoom)
                                            .child("messages")
                                            .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });
                                }
                            });
                }
            });
            ThoatChat();

        }
        public void ThoatChat() {
            imageButtonThoatChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChatActivity.this, UserActivity.class);
                    // Truyền dữ liệu sang UserActivity bằng các biến đã khai báo
                    intent.putExtra("name", reciverName);
                    intent.putExtra("imageUrl", reciverimg);
                    intent.putExtra("username", reciverUserName);
                    intent.putExtra("loggedInUsername", SenderUserName);
                    // Bắt đầu UserActivity và đóng ChatActivity
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
