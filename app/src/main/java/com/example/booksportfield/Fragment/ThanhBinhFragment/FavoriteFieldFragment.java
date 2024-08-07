package com.example.booksportfield.Fragment.ThanhBinhFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.booksportfield.Activity.ThanhBinh.ChangeAvtActivity;
import com.example.booksportfield.Adapter.ThanhBinhAdapter.FavoriteFieldAdapter;
import com.example.booksportfield.Models.Field;
import com.example.booksportfield.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteFieldFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteFieldFragment extends Fragment {

    private ListView lvFavoriteFields;
    private ArrayList<Field> favoriteFieldsList;
    private FavoriteFieldAdapter adapter;
    private String user;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavoriteFieldFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteFieldFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteFieldFragment newInstance(String param1, String param2) {
        FavoriteFieldFragment fragment = new FavoriteFieldFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_field, container, false);
        if (getArguments() != null) {
            user = getArguments().getString("username");
        }
        loadFField(view, user);

        //RoundedImageView img = view.findViewById(R.id.favorite_imgfield);

        return view;
    }





    private void loadFField(View root, String user) {
        List<String> listFField = new ArrayList<>();
        List<Field> listField = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("favorites/" + user);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listFField.clear();
                for (DataSnapshot sp : snapshot.getChildren()) {
                    if (sp.getValue(Boolean.class)) {
                        String fieldID = sp.getKey();
                        listFField.add(fieldID);
                    }
                }
                loadFieldData(listFField, root);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu có
            }
        });
    }

    private void loadFieldData(List<String> listFField, View root) {
        List<Field> listField = new ArrayList<>();

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("soccer_fields");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listField.clear();
                for (DataSnapshot sp : snapshot.getChildren()) {
                    for (String key : listFField) {
                        if (key.equals(sp.getKey())) {
                            Field field = sp.getValue(Field.class);
                            field.setNodeID(sp.getKey());
                            field.setField_id(sp.getKey());
                            listField.add(field);
                        }
                    }
                }
                setAdapter(listField, root);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu có
            }
        });



    }

    private void setAdapter(List<Field> list, View root)
    {
        lvFavoriteFields = root.findViewById(R.id.lvFavoriteFields);
        FavoriteFieldAdapter adapter = new FavoriteFieldAdapter(getContext(), list);



        lvFavoriteFields.setAdapter(adapter);

//        lvFavoriteFields.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (view.getId() == R.id.itemField) {
//                    // Xử lý khi click vào hình ảnh
//                    Toast.makeText(getActivity(), "click img", Toast.LENGTH_SHORT).show();
//                    String selectedItem = (String) parent.getItemAtPosition(position);
//                    Toast.makeText(getContext(), "Bạn đã chọn: " + selectedItem, Toast.LENGTH_SHORT).show();
//
//                    handleItemClick(position);
//                } else {
//                    // Xử lý khi click vào phần khác của item
//                    Toast.makeText(getActivity(), "click item", Toast.LENGTH_SHORT).show();
//                    handleItemClick(position);
//                }
//            }
//        });
    }

    private void handleItemClick(int position) {
        Intent intent = new Intent(getActivity(), ChangeAvtActivity.class);
        startActivity(intent);
    }


}