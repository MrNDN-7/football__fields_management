package com.example.booksportfield.Fragment.ThanhBinhFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.booksportfield.Activity.DucNhan.LoginActivity;
import com.example.booksportfield.Activity.DucNhan.UserActivity;
import com.example.booksportfield.Activity.Kiet.LichSu;
import com.example.booksportfield.Activity.ThanhBinh.ChangeAvtActivity;
import com.example.booksportfield.Activity.ThanhBinh.ChangeInforActivity;
import com.example.booksportfield.Activity.ThanhBinh.ChangePassActivity;
import com.example.booksportfield.Activity.ThanhBinh.Edit_field_infor;
import com.example.booksportfield.Activity.ThanhBinh.FormUpdateRoleActivity;
import com.example.booksportfield.Activity.ThanhBinh.ThongKeActivity;
import com.example.booksportfield.Activity.TuanAnh.Activity_Add_Field;
import com.example.booksportfield.Activity.TuanAnh.Activity_FieldManager;
import com.example.booksportfield.Adapter.ThanhBinhAdapter.UserSession;
import com.example.booksportfield.Models.Account;
import com.example.booksportfield.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    private ImageButton messImg;
    private TextView historyOrderField, accRole, accListBill, accUpdateRole, username_account, add_field, list_field, manager_field, accUsername, accInformation, accChangePass, accChangeAvt, accAddField, accListField, accManagerField, accPaymentMode, accPaymentHistory, accLogout;

    private ImageView accIconChat;
    private CircleImageView accAvtImage;
    private LinearLayout ownerField;
private String username;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        accountView(rootView);
        String user = "";
//        if (getArguments() != null) {
//            user = getArguments().getString("username");
//            // Sử dụng giá trị username ở đây
//        }
        user = UserSession.getInstance().getUsername();
        loadDataAccount(user);

        callPaymentHistory();
        callPaymentMode();
        callInfor();
        callChangeAvt();
        callChangePass();
        //callManaField();
        callListField();
        callAddField();
        callEditProfile();
        callMessage(user);
        callLogout();
        callUpdateRole();
        callHistoryOrderfield();

        return rootView;
    }

    public void accountView(View root) {
        accIconChat = root.findViewById(R.id.accIconChat);
        accAvtImage = root.findViewById(R.id.accAvtImage);
        accUsername = root.findViewById(R.id.accUsername);
        accInformation = root.findViewById(R.id.accInformation);
        accChangePass = root.findViewById(R.id.accChangePass);
        accChangeAvt = root.findViewById(R.id.accChangeAvt);
        accAddField = root.findViewById(R.id.accAddField);
        accListField = root.findViewById(R.id.accListField);
        //accManagerField = root.findViewById(R.id.accManagerField);
        accPaymentMode = root.findViewById(R.id.accPaymentMode);
        accPaymentHistory = root.findViewById(R.id.accPaymentHistory);
        accLogout = root.findViewById(R.id.accLogout);
        ownerField = root.findViewById(R.id.ownerField);
        accUpdateRole = root.findViewById(R.id.accUpdateRole);
        //accListBill = root.findViewById(R.id.accListBill);
        accRole = root.findViewById(R.id.accRole);
        historyOrderField = root.findViewById(R.id.historyOrderField);

    }

    public void callMessage(String username) {
        accIconChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserActivity.class);
                intent.putExtra("loggedInUsername", username);
                startActivity(intent);
            }
        });
    }

    public void callEditProfile() {
        accUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangeInforActivity.class);
                intent.putExtra("loadDataAccount", true);

                startActivity(intent);
            }
        });
        accInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangeInforActivity.class);
                intent.putExtra("loadDataAccount", true);
                startActivity(intent);

            }
        });

    }

    public void callAddField() {
        accAddField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Activity_Add_Field.class);
                startActivity(intent);
            }
        });
    }

    public void callListField() {
        accListField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Activity_FieldManager.class);
                startActivity(intent);
            }
        });
    }

    //    public void callManaField() {
//        accManagerField.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), Edit_field_infor.class);
//                startActivity(intent);
//            }
//        });
//    }
    public void callChangePass() {
        accChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePassActivity.class);
                intent.putExtra("loadDataAccount", true);
                startActivity(intent);
            }
        });
    }

    public void callChangeAvt() {
        accChangeAvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangeAvtActivity.class);
                intent.putExtra("loadDataAccount", true);
                startActivity(intent);
            }
        });
        accAvtImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangeAvtActivity.class);
                startActivity(intent);
            }
        });
    }

    public void callInfor() {

    }

    public void callPaymentMode() {
        accInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Edit_field_infor.class);
                startActivity(intent);
            }
        });
    }

    public void callPaymentHistory() {
        accPaymentHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LichSu.class);
                startActivity(intent);
            }
        });
    }

    public void callLogout() {
        accLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }
    private void logoutUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Đăng xuất");
        builder.setMessage("Bạn có muốn đăng xuất khỏi ứng dụng?");
        builder.setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xóa thông tin đăng nhập hoặc thực hiện các tác vụ đăng xuất khác
                // ...

                // Đóng tất cả các Activity và trở về màn hình đăng nhập hoặc màn hình chính
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finishAffinity() ;
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Không làm gì cả, đóng hộp thoại
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void callUpdateRole() {
        accUpdateRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FormUpdateRoleActivity.class);
                startActivity(intent);
            }
        });
    }

    Account accInfor = new Account();

    private void loadDataAccount(String user) {
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("accounts/" + user);
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    accInfor.setUsername(user);
                    accInfor.setName(snapshot.child("name").getValue(String.class));
                    accInfor.setPhoneNumber(snapshot.child("phoneNumber").getValue(String.class));
                    accInfor.setEmail(snapshot.child("email").getValue(String.class));
                    accInfor.setDateOfBirth(snapshot.child("dateOfBirth").getValue(String.class));
                    accInfor.setGender(snapshot.child("gender").getValue(String.class));
                    accInfor.setRole(snapshot.child("role").getValue(String.class));
                    accInfor.setUrlImage(snapshot.child("urlImage").getValue(String.class));
                    accInfor.setPassword(snapshot.child("password").getValue(String.class));

                    setTextAccount();
                    setLayoutOwner(accInfor.getRole());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void setTextAccount() {
        accUsername.setText(accInfor.getUsername());
        String urlImage = accInfor.getUrlImage();
        Picasso.get().load(urlImage).into(accAvtImage);
    }

    private void setLayoutOwner(String role)
    {
        if(role.equals("ownerField"))
        {
            ownerField.setVisibility(View.VISIBLE);
            accUpdateRole.setVisibility(View.GONE);
            historyOrderField.setVisibility(View.VISIBLE);
            accRole.setText("Chủ sân");
        }
        else {
            ownerField.setVisibility(View.GONE);
            accUpdateRole.setVisibility(View.VISIBLE);
            historyOrderField.setVisibility(View.GONE);
            accRole.setText("Người dùng");
        }
    }

    public void callHistoryOrderfield()
    {
        historyOrderField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ThongKeActivity.class);
                startActivity(intent);
            }
        });
    }
}