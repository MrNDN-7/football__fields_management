package com.example.booksportfield.Fragment.ThanhBinhFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booksportfield.Activity.DucNhan.LoginActivity;
import com.example.booksportfield.Activity.DucNhan.UserActivity;
import com.example.booksportfield.Activity.Kiet.LoaiSanActivity;
import com.example.booksportfield.Activity.ThanhBinh.MainActivity;
import com.example.booksportfield.Activity.ThanhBinh.SubmitUpdateRoleOfAdminActivity;
import com.example.booksportfield.Activity.ThanhBinh.seach_fields;
import com.example.booksportfield.Adapter.ThanhBinhAdapter.FieldPagerAdapter;
import com.example.booksportfield.Adapter.ThanhBinhAdapter.UserSession;
import com.example.booksportfield.Adapter.ThanhBinhAdapter.ViewPagerAdapter;
import com.example.booksportfield.Models.Field;
import com.example.booksportfield.Models.TimeSlot;
import com.example.booksportfield.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {


    public CircleImageView avtImgHeader;
    public ImageButton searchEdtHome, messImgHeader, field5, field7, field11, btnHome, btnLike, btnHistory, btnNoti, btnAccount, field1, icon_like1, field2, icon_like2, field3, icon_like3, field4, icon_like4, imgfield5, icon_like5;
    public EditText edtSearchHome;

    public TextView txtshowall, itemName_field1, itemDescribe_field1, itemName_field2, itemDescribe_field2, itemName_field3, itemDescribe_field3, itemName_field4, itemDescribe_field4, itemName_field5, itemDescribe_field5;

    public RatingBar rating_bar1, rating_bar2, rating_bar3, rating_bar4, rating_bar5;

    private MainActivity mainActivity;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager, viewpagerSanPhoBien;

    List<Field> fieldList ;

    private String username;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    private boolean isViewPagerAttached() {
        return isAdded() && viewpagerSanPhoBien != null;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        avtImgHeader = view.findViewById(R.id.avtImg_header);
        searchEdtHome = view.findViewById(R.id.searchbar_home);
        messImgHeader = view.findViewById(R.id.messImg_header);
        field5 = view.findViewById(R.id.field5);
        field7 = view.findViewById(R.id.field7);
        field11 = view.findViewById(R.id.field11);
        edtSearchHome = view.findViewById(R.id.edt_search_bar);
        viewpagerSanPhoBien = view.findViewById(R.id.viewpagerSanPhoBien);
        txtshowall = view.findViewById(R.id.txtshowall);




        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("soccer_fields");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("call data", "xử lý data" + dataSnapshot);

                fieldList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String nodeID = snapshot.getKey();
                    String ins = "soccer_fields/" + nodeID + "/timeslot";
                    Field field = snapshot.getValue(Field.class);
                    ArrayList<TimeSlot> ls = new ArrayList<>();
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(ins);
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot1 : snapshot.getChildren())
                            {
                                TimeSlot item = dataSnapshot1.getValue(TimeSlot.class);
                                if(item != null)
                                {
                                    ls.add(item);
                                }
                            }
                            field.setTimeslots(ls);

                            field.setNodeID(nodeID);
                            field.setField_id(nodeID);
                            fieldList.add(field);
                            Log.d("call data", "list data" + field);

                            if(fieldList != null && isViewPagerAttached())
                            {
                                //ViewPager viewPager = view.findViewById(R.id.viewpagerSanPhoBien);
                                if (viewpagerSanPhoBien != null) {
                                    List<Field> highRatedFields = new ArrayList<>();
                                    for (Field field : fieldList) {
                                        if (field.getRating() > 3) {
                                            highRatedFields.add(field);
                                        }
                                    }

                                    if (!highRatedFields.isEmpty()) {

                                        FieldPagerAdapter adapter = new FieldPagerAdapter(getChildFragmentManager(), highRatedFields, username);
                                        viewpagerSanPhoBien.setAdapter(adapter);
                                    }
                                }
                                else {
                                    //viewPager.setAdapter(null);
                                    Toast.makeText(getContext(), "Không có dữ liệu để hiển thị", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("call data", "Lỗi khi lấy dữ liệu từ Firebase: " + error.getMessage());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        homeView(rootView);
//
        username = UserSession.getInstance().getUsername();

        callSearchFieldLayout();
        callAccountLayout();
        if( username != null && !username.isEmpty() )
        {
            callMessageLayout(username);
        }
        callTypeField5();
        callTypeField7();
        callTypeField11();
        callShowAllField();

        fieldList = new ArrayList<>();







        return rootView;
    }

    private void homeView(View rootView) {
        avtImgHeader = rootView.findViewById(R.id.avtImg_header);
        searchEdtHome = rootView.findViewById(R.id.searchbar_home);
        messImgHeader = rootView.findViewById(R.id.messImg_header);
        field5 = rootView.findViewById(R.id.field5);
        field7 = rootView.findViewById(R.id.field7);
        field11 = rootView.findViewById(R.id.field11);
        edtSearchHome = rootView.findViewById(R.id.edt_search_bar);
        viewpagerSanPhoBien = rootView.findViewById(R.id.viewpagerSanPhoBien);
        txtshowall = rootView.findViewById(R.id.txtshowall);
    }

    private void callAccountLayout() {
        avtImgHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.avatar_menu, popupMenu.getMenu());

                // Định nghĩa sự kiện click cho các mục trong menu
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {


                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.accountLayout) {
                            AccountFragment accountFragment = new AccountFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("username", username);
                            accountFragment.setArguments(bundle);


                            // Bắt đầu một FragmentTransaction để thực hiện việc thay thế fragment
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                            // Thay thế fragment hiện tại bằng fragment mới (accountFragment)
                            fragmentTransaction.replace(R.id.flFragment, accountFragment);

                            // Đặt tên cho việc thực hiện transaction này (không bắt buộc)
                            fragmentTransaction.addToBackStack(null);

                            // Hoàn thành và thực hiện transaction
                            fragmentTransaction.commit();

                            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
                            bottomNavigationView.setSelectedItemId(R.id.bottomAccount);
                            return true;
                        } else if (item.getItemId() == R.id.action_logout) {
                            // Xử lý khi click vào menu item "Đăng xuất"
                            logoutUser();
                            return true;
                        }
                        return false;
                    }
                });

                // Hiển thị PopupMenu
                popupMenu.show();
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

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finishAffinity();
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

    private void callSearchFieldLayout() {
        edtSearchHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), seach_fields.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }

    private void callMessageLayout(String username) {
        messImgHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserActivity.class);
                intent.putExtra("loggedInUsername", username);
                startActivity(intent);
            }
        });
    }

    private void callTypeField5() {
        field5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoaiSanActivity.class);
                intent.putExtra("fieldType", "5");
                startActivity(intent);
            }

        });
    }

    private void callTypeField7() {
        field7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoaiSanActivity.class);
                intent.putExtra("fieldType", "7");

                startActivity(intent);
            }

        });
    }

    private void callTypeField11() {
        field11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoaiSanActivity.class);
                intent.putExtra("fieldType", "11");
                startActivity(intent);
            }

        });
    }

    private void callShowAllField() {
        txtshowall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoaiSanActivity.class);
                intent.putExtra("fieldType", "allField");
                startActivity(intent);
            }

        });
    }

    private void loadDataFieldinViewPage(View root) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("soccer_fields");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("call data", "xử lý data" + dataSnapshot);

                fieldList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String nodeID = snapshot.getKey();
                    Field field = snapshot.getValue(Field.class);
                    field.setNodeID(nodeID);
                    fieldList.add(field);
                    //Log.d("call data", "list data" + field);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("call data", "Lỗi khi lấy dữ liệu từ Firebase: " + error.getMessage());
            }
        });
    }

    private void setupViewPager(List<Field> fieldList, View root) {
        ViewPager viewPager = root.findViewById(R.id.viewpagerSanPhoBien);
        if (viewPager != null) {
            List<Field> highRatedFields = new ArrayList<>();
            for (Field field : fieldList) {
                if (field.getRating() > 4) {
                    highRatedFields.add(field);
                }
            }

            if (!highRatedFields.isEmpty()) {
                FieldPagerAdapter adapter = new FieldPagerAdapter(getChildFragmentManager(), highRatedFields, UserSession.getInstance().getUsername());
                viewPager.setAdapter(adapter);
            }

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    Log.d("ViewPager", "Trang được chọn: " + position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

            viewPager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Chuyển sang layout mới
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("openAccountFragment", true);
                    startActivity(intent);
                }
            });

            viewPager.setPageMargin(30);
        }
    }
}
