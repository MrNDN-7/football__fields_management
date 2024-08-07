package com.example.booksportfield.Activity.TuanAnh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.booksportfield.Adapter.TuanAnhAdapter.TimeSlotAdapter;
import com.example.booksportfield.Models.Field;
import com.example.booksportfield.Models.TimeSlot;
import com.example.booksportfield.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Activity_TimeSlot extends AppCompatActivity {
    RecyclerView rcv_timeslot;
    private List<TimeSlot> mListTimeSlot;
    private TimeSlotAdapter mItemAdapter;
    private ImageButton btnBack;
    private Button btnSelectDay;
    private Button btnCreateTimeSlot;
    private TextView tvDateFilter;
    private static Field fieldCurrentAddTimeSlot;
    private String[] trangThaiValues = {"Chưa đặt", "Đặt"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_manage_field_timeslot);

        rcv_timeslot = findViewById(R.id.rcv_field_timeslot);
        btnSelectDay = findViewById(R.id.btnSelectDay);
        btnCreateTimeSlot = findViewById(R.id.btnCreateTimeSlot);
        btnBack = findViewById(R.id.btnBackTimeSlot);
        tvDateFilter = findViewById(R.id.tvDateFilter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcv_timeslot.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rcv_timeslot.addItemDecoration(dividerItemDecoration);

        fieldCurrentAddTimeSlot = (Field) getIntent().getSerializableExtra("itemObject");


        mListTimeSlot = new ArrayList<>();
        //Gọi dialog update
        mItemAdapter = new TimeSlotAdapter(mListTimeSlot, new TimeSlotAdapter.IClickListener() {
            @Override
            public void onClickUpDateItem(TimeSlot item) {
                OpenDialogUpdateTimeSlot(item);
            }
        }, fieldCurrentAddTimeSlot);

        //Gọi dialog create
        btnCreateTimeSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogCreateItem(null);
            }
        });

        //Chọn ngày lọc
        btnSelectDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(tvDateFilter,true);
            }
        });

        //Btn Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý khi nút Back được bấm
                onBackPressed();
            }
        });

        rcv_timeslot.setAdapter(mItemAdapter);
        interactWithItems(fieldCurrentAddTimeSlot);

    }

    private void OpenDialogUpdateTimeSlot(TimeSlot timeSlot)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_timeslot);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView titleDialog = dialog.findViewById(R.id.tvTitleDialogTimeSlot);
        titleDialog.setText("Cập nhập khung giờ");
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnUpdate = dialog.findViewById(R.id.btnAddSlotTime);

        EditText edtTimeStart = dialog.findViewById(R.id.edtSelectedTimeStart);
        EditText edtTimeEnd = dialog.findViewById(R.id.edtSelectedTimeEnd);
        EditText edtBookingDate = dialog.findViewById(R.id.edtBookingDate);
        Spinner spinnerStatus = dialog.findViewById(R.id.spinnerTrangThai);

        initSpinnerStatus(spinnerStatus);

        edtTimeStart.setText(timeSlot.getStart_time());
        edtTimeEnd.setText(timeSlot.getEnd_time());
        edtBookingDate.setText(timeSlot.getBooking_date());

        if(timeSlot.getStatus().equals("chuadat"))
        {
            spinnerStatus.setSelection(0);
        }
        else if(timeSlot.getStatus().equals("dat"))
        {
            spinnerStatus.setSelection(1);
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("soccer_fields");

                String bookingDate = edtBookingDate.getText().toString().trim();
                String timeStart = edtTimeStart.getText().toString().trim();
                String timeEnd = edtTimeEnd.getText().toString().trim();
                String status = spinnerStatus.getSelectedItem().toString().trim();

                if(timeSlot.getStatus().equals("dat"))
                {
                    Toast.makeText(Activity_TimeSlot.this, "Khung giờ đã được đặt không thể cập nhập", Toast.LENGTH_SHORT).show();
                    return;
                }

                timeSlot.setStart_time(timeStart);
                timeSlot.setEnd_time(timeEnd);
                timeSlot.setStatusUpdate(status);
                timeSlot.setBooking_date(bookingDate);

                // Kiểm tra nếu bookingDate nhỏ hơn ngày hiện tại
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                Date selectedDate;
                Date currentDate = new Date();
                try {
                    selectedDate = dateFormat.parse(bookingDate);
                    if (selectedDate.before(currentDate)) {
                        // Hiển thị thông báo lỗi hoặc xử lý phù hợp
                        Toast.makeText(Activity_TimeSlot.this, "Thời gian đã quá hạn đặt", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Kiểm tra nếu timeStart muộn hơn timeEnd
                if (!timeStart.isEmpty() && !timeEnd.isEmpty()) {
                    String[] startTime = timeStart.split(":");
                    String[] endTime = timeEnd.split(":");

                    int startHour = Integer.parseInt(startTime[0]);
                    int startMinute = Integer.parseInt(startTime[1]);

                    int endHour = Integer.parseInt(endTime[0]);
                    int endMinute = Integer.parseInt(endTime[1]);

                    if (startHour > endHour || (startHour == endHour && startMinute >= endMinute)) {
                        // Hiển thị thông báo lỗi hoặc xử lý phù hợp
                        Toast.makeText(Activity_TimeSlot.this, "Thời gian bắt đầu không được muộn hơn thời gian kết thúc", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                myRef.child(String.valueOf(fieldCurrentAddTimeSlot.getNodeID())).child("timeslot").child(String.valueOf(timeSlot.getId_timeSlot())).updateChildren(timeSlot.toMap(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(Activity_TimeSlot.this,"Update thanh cong", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
            }
        });

        edtBookingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(edtBookingDate, false);
            }
        });

        edtTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(edtTimeEnd);
            }
        });

        edtTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(edtTimeStart);
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    private void openDialogCreateItem(Field field) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_timeslot);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnAddSlotTime = dialog.findViewById(R.id.btnAddSlotTime);
        EditText edtTimeStart = dialog.findViewById(R.id.edtSelectedTimeStart);
        EditText edtTimeEnd = dialog.findViewById(R.id.edtSelectedTimeEnd);
        EditText edtBookingDate = dialog.findViewById(R.id.edtBookingDate);
        Spinner spinnerStatus = dialog.findViewById(R.id.spinnerTrangThai);
        initSpinnerStatus(spinnerStatus);

        edtBookingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(edtBookingDate, false);
            }
        });

        edtTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(edtTimeEnd);
            }
        });

        edtTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(edtTimeStart);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnAddSlotTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookingDate = edtBookingDate.getText().toString().trim();
                String timeStart = edtTimeStart.getText().toString().trim();
                String timeEnd = edtTimeEnd.getText().toString().trim();
                String status = spinnerStatus.getSelectedItem().toString().trim();

                // Validate các trường
                if (timeStart.isEmpty() ||
                        timeEnd.isEmpty() ||
                        bookingDate.isEmpty() ||
                        status.isEmpty()) {
                    // Nếu có ít nhất một trường là rỗng
                    Toast.makeText(Activity_TimeSlot.this, "Hãy chọn đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra nếu bookingDate nhỏ hơn ngày hiện tại
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                Date selectedDate;
                Date currentDate = new Date();
                try {
                    selectedDate = dateFormat.parse(bookingDate);
                    if (selectedDate.before(currentDate)) {
                        // Hiển thị thông báo lỗi hoặc xử lý phù hợp
                        Toast.makeText(Activity_TimeSlot.this, "Thời gian đã quá hạn đặt", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Kiểm tra nếu timeStart muộn hơn timeEnd
                if (!timeStart.isEmpty() && !timeEnd.isEmpty()) {
                    String[] startTime = timeStart.split(":");
                    String[] endTime = timeEnd.split(":");

                    int startHour = Integer.parseInt(startTime[0]);
                    int startMinute = Integer.parseInt(startTime[1]);

                    int endHour = Integer.parseInt(endTime[0]);
                    int endMinute = Integer.parseInt(endTime[1]);

                    if (startHour > endHour || (startHour == endHour && startMinute >= endMinute)) {
                        // Hiển thị thông báo lỗi hoặc xử lý phù hợp
                        Toast.makeText(Activity_TimeSlot.this, "Thời gian bắt đầu không được muộn hơn thời gian kết thúc", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                TimeSlot timeSlot = new TimeSlot(bookingDate, timeStart, timeEnd, status);
                onClickAddItem(timeSlot);
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    //Tương tác với dữ liệu
    void onClickAddItem(TimeSlot item) {
        // Lấy số lượng time slot từ cơ sở dữ liệu
        getTimeSlotCount(new TimeSlotCountListener() {
            @Override
            public void onTimeSlotCountLoaded(int count) {
                // Sử dụng số lượng time slot để tạo ID mới
                String keyItemRandom = String.valueOf(count); // Tạo ID mới bằng cách sử dụng số lượng time slot + 1

                // Thêm time slot mới vào cơ sở dữ liệu
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("soccer_fields").child(fieldCurrentAddTimeSlot.getNodeID()).child("timeslot");
                item.setId_timeSlot(keyItemRandom);
                myRef.child(keyItemRandom).setValue(item, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            // Xử lý khi thêm thành công
                            Toast.makeText(Activity_TimeSlot.this, "Success to add New Item", Toast.LENGTH_SHORT).show();
                        } else {
                            // Xử lý khi có lỗi xảy ra trong quá trình thêm
                            Toast.makeText(Activity_TimeSlot.this, "Failed to add New Item: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onTimeSlotCountError(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình lấy số lượng time slot
                Toast.makeText(Activity_TimeSlot.this, "Failed to get time slot count: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface TimeSlotCountListener {
        void onTimeSlotCountLoaded(int count);
        void onTimeSlotCountError(DatabaseError databaseError);
    }

    public static void getTimeSlotCount(TimeSlotCountListener listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference timeslotRef = database.getReference("soccer_fields").child(fieldCurrentAddTimeSlot.getNodeID()).child("timeslot");

        timeslotRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Lấy số lượng time slot từ dataSnapshot và chuyển đổi thành int
                int count = (int) dataSnapshot.getChildrenCount();
                listener.onTimeSlotCountLoaded(count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc số lượng time slot
                listener.onTimeSlotCountError(databaseError);
            }
        });
    }

    private void filterItemsByDate(String selectedDate) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("soccer_fields");

        Query query = myRef.child(fieldCurrentAddTimeSlot.getNodeID()).child("timeslot").orderByChild("booking_date").equalTo(selectedDate);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mListTimeSlot.clear();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    TimeSlot item = itemSnapshot.getValue(TimeSlot.class);
                    mListTimeSlot.add(item);
                }
                mItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Activity_TimeSlot.this, "Hoạt động bị hủy bỏ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void interactWithItems(Field field)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("soccer_fields");
        DatabaseReference idFieldRef = myRef.child(field.getNodeID());
        idFieldRef.child("timeslot").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TimeSlot item = snapshot.getValue(TimeSlot.class);
                if(item != null)
                {
                    item.setId_timeSlot(snapshot.getKey());
                    mListTimeSlot.add(item);
                    mItemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TimeSlot item = snapshot.getValue(TimeSlot.class);
                if(mListTimeSlot == null || mListTimeSlot.isEmpty())
                {
                    return;
                }

                for(int i=0;i< mListTimeSlot.size(); i++)
                {
                    if(item.getId_timeSlot().equals(mListTimeSlot.get(i).getId_timeSlot()))
                    {
                        mListTimeSlot.set(i,item);
                        mItemAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Tương tác với UI
    private void initSpinnerStatus(Spinner spinner)
    {
        // Thiết lập Adapter cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, trangThaiValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Xử lý sự kiện khi người dùng chọn một giá trị trong Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTrangThai = trangThaiValues[position];
                // Xử lý giá trị trạng thái đã chọn
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không cần xử lý
            }
        });
    }

    private void showDatePicker(TextView tvDate,boolean filter) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth +   "-" + (monthOfYear + 1) + "-" + year;
                        tvDate.setText(selectedDate);
                        if(filter)
                            filterItemsByDate(selectedDate);
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog(TextView tvTime) {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String timeString = String.format("%02d:%02d", hourOfDay, minute);
                tvTime.setText(timeString);
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }


}