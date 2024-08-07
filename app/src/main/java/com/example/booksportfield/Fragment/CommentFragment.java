package com.example.booksportfield.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booksportfield.Adapter.Kiet.CmtAdapter;
import com.example.booksportfield.Adapter.ThanhBinhAdapter.UserSession;
import com.example.booksportfield.Models.Account;
import com.example.booksportfield.Models.Field;
import com.example.booksportfield.Models.Comment;
import com.example.booksportfield.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import per.wsj.library.AndRatingBar;


public class CommentFragment extends Fragment {

//    ArrayList<Comment> comments = new ArrayList<>();
    ArrayList<Account> accounts = new ArrayList<>();


    public CommentFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAccounts();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference data = database.getReference("comments");
        List<Comment> comments = new ArrayList<>();
        Bundle bundle = getArguments();
        Field field = (Field) bundle.getSerializable("object");

        CmtAdapter adapter = new CmtAdapter(requireContext(), comments);
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue :snapshot.getChildren()){
                        if(issue.getValue(Comment.class).getField_id().equals(field.getField_id()))
                            comments.add(issue.getValue(Comment.class));

                    }
                    for (Comment comment : comments) {
                        for (Account account : accounts) {
                            if (account.getUsername().equals(comment.getUser_id())) {
                                comment.setUser_name(account.getName());
                                comment.setUser_image(account.getUrlImage());
                            }
                        }
                    }


                    ImageButton btn = view.findViewById(R.id.btnSend);

                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView txtCmt = view.findViewById(R.id.txtCmt);
                            AndRatingBar rating = view.findViewById(R.id.ratingBarLichSu);
                            String comment = txtCmt.getText().toString();
                            if(comment.equals("") || comment == null)
                                Toast.makeText(requireContext(), "Vui lòng nhập bình luận", Toast.LENGTH_LONG).show();
                            else
                            {
                                Comment cmt = new Comment();
                                cmt.setField_id(field.getField_id());
                                cmt.setComment(txtCmt.getText()+"");
                                cmt.setRating(rating.getRating());
                                cmt.setUser_id(UserSession.getInstance().getUsername());
                                DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference("comments");
                                String key = bookingsRef.push().getKey(); // Tạo một khóa mới cho booking
                                if (key != null) {
                                    bookingsRef.push().setValue(cmt).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            comments.add(cmt);
                                            adapter.notifyDataSetChanged();
                                            txtCmt.setText("");
                                            Toast.makeText(getActivity(), "Đã lưu đánh giá thành công!", Toast.LENGTH_SHORT).show();

                                        }

                                    })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Xử lý khi thêm thất bại
                                                    Toast.makeText(requireContext(), "Đánh giá thất bại", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                                double totalRating = 0;
                                int numComments = comments.size();
                                for (Comment comment1 : comments) {
                                    totalRating += comment1.getRating();
                                }
                                double avgRating = totalRating / numComments;
                                avgRating = Math.round(avgRating * 10.0) / 10.0;

                                // Cập nhật giá trị rating trong bảng soccer_fields
                                DatabaseReference fieldRef = FirebaseDatabase.getInstance().getReference("soccer_fields").child(field.getField_id());
                                fieldRef.child("rating").setValue(avgRating).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        // Xử lý khi cập nhật thành công
                                        Log.d("TAG", "Rating updated successfully!");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Xử lý khi cập nhật thất bại
                                        Log.e("TAG", "Failed to update rating: " + e.getMessage());
                                    }
                                });
                            }
                        }
                    });
                    ListView listView = view.findViewById(R.id.recycleComment);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void initAccounts() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference data = database.getReference("accounts");
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Account acc = issue.getValue(Account.class);
                        acc.setUsername(issue.getKey());
                        accounts.add(acc);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}