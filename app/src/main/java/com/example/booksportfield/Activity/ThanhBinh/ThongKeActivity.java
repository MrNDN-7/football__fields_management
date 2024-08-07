package com.example.booksportfield.Activity.ThanhBinh;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.booksportfield.Adapter.ThanhBinhAdapter.BookingAdapter;
import com.example.booksportfield.Adapter.ThanhBinhAdapter.UserSession;
import com.example.booksportfield.Models.Booking;
import com.example.booksportfield.Models.Field;
import com.example.booksportfield.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ThongKeActivity extends AppCompatActivity {
    private Spinner timeSpinner;
    private Button selectDateButton, selectWeekButton, selectMonthButton, selectYearButton;
    private String selectDate, selectWeek, selectMonth, selectYear;
    private ListView lstThongKe;

    private TextView total;
    private List<Booking> lstAllBooking = new ArrayList<>();
    private List<Field> lstFiledOfUser = new ArrayList<>();
    private List<String> lstFieldID = new ArrayList<>();

    private BookingAdapter bookingAdapter;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private static final SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thong_ke);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnBack), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BookingFillter();


        timeSpinner = findViewById(R.id.spinnerThongKe);
        selectDateButton = findViewById(R.id.select_date_button);
        selectWeekButton = findViewById(R.id.select_week_button);
        selectMonthButton = findViewById(R.id.select_month_button);
        selectYearButton = findViewById(R.id.select_year_button);
        lstThongKe = findViewById(R.id.lstThongKe);
        total = findViewById(R.id.total);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_thong_ke, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapter);

        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = parent.getItemAtPosition(position).toString();
                handleSelection(selection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        selectDateButton.setOnClickListener(v -> showDatePickerDialog());
        selectWeekButton.setOnClickListener(v -> showWeekPickerDialog());
        selectMonthButton.setOnClickListener(v -> showMonthPickerDialog());
        selectYearButton.setOnClickListener(v -> showYearPickerDialog());

    }

    private void handleSelection(String selection) {
        selectDateButton.setVisibility(View.GONE);
        selectWeekButton.setVisibility(View.GONE);
        selectMonthButton.setVisibility(View.GONE);
        selectYearButton.setVisibility(View.GONE);

        switch (selection) {
            case "Ngày":
                selectDateButton.setVisibility(View.VISIBLE);
                break;
            case "Tuần":
                selectWeekButton.setVisibility(View.VISIBLE);
                break;
            case "Tháng":
                selectMonthButton.setVisibility(View.VISIBLE);
                break;
            case "Năm":
                selectYearButton.setVisibility(View.VISIBLE);
                break;
        }
    }



    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Tạo Calendar mới để thiết lập ngày đã chọn
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(year1, monthOfYear, dayOfMonth);

                    // Định dạng lại ngày theo định dạng "03-05-2024"
                    selectDate = dateFormat.format(selectedCalendar.getTime());
                    selectDateButton.setText(selectDate);
                    thongKeTheoNgay();
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void showWeekPickerDialog() {
        final CharSequence[] weeks = generateWeeks(); // Tạo mảng các tuần

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn tuần");
        builder.setItems(weeks, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectWeek = weeks[which].toString();
                selectWeekButton.setText(selectWeek);
                thongKeTheoTuan();
            }
        });
        builder.show();
    }

    private CharSequence[] generateWeeks() {
        // Tạo mảng các tuần từ 1 đến 52 hoặc tùy theo nhu cầu của bạn
        CharSequence[] weeks = new CharSequence[52];
        for (int i = 0; i < 52; i++) {
            weeks[i] = "Tuần " + (i + 1);
        }
        return weeks;
    }


    private void showMonthPickerDialog() {
        final CharSequence[] months = {
                "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
                "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn tháng");
        builder.setItems(months, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectMonth = months[which].toString();
                selectMonthButton.setText(selectMonth);
                thongKeTheoThang();
            }
        });
        builder.show();
    }

    private void showYearPickerDialog() {
        int startYear = 2020;
        int endYear = 2030;
        final CharSequence[] years = new CharSequence[endYear - startYear + 1];
        for (int i = startYear; i <= endYear; i++) {
            years[i - startYear] = String.valueOf(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn năm");
        builder.setItems(years, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectYear = years[which].toString();
                selectYearButton.setText(selectYear);
                thongKeTheoNam();
            }
        });
        builder.show();
    }

    public void thongKeTheoNgay() {
        List<Booking> filteredBookings = filterBookingsByDate(lstAllBooking, selectDate);

        TongThuNhap(filteredBookings);
        if (bookingAdapter != null) {
            bookingAdapter.clear();
        }
        bookingAdapter = new BookingAdapter(this, filteredBookings);

        lstThongKe.setAdapter(bookingAdapter);

    }


    public static List<Booking> filterBookingsByDate(List<Booking> bookings, String date) {
        return bookings.stream()
                .filter(booking -> date.equals(booking.getBooking_date()))
                .collect(Collectors.toList());
    }

    public void thongKeTheoTuan() {

    }

    public void thongKeTheoThang() {
        int month = convertMonthStringToInteger(selectMonth);
        List<Booking> filteredBookings = filterBookingsByMonth(lstAllBooking, month, 2024);
        TongThuNhap(filteredBookings);
        if (bookingAdapter != null) {
            bookingAdapter.clear();
        }
        bookingAdapter = new BookingAdapter(this, filteredBookings);

        lstThongKe.setAdapter(bookingAdapter);
    }

    public static List<Booking> filterBookingsByMonth(List<Booking> bookings, int month, int year) {
        // Khởi tạo Calendar để đặt tháng và năm cần tìm
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.set(Calendar.MONTH, month - 1); // Calendar.MONTH bắt đầu từ 0
        targetCalendar.set(Calendar.YEAR, year);

        // Lọc danh sách bookings dựa trên tháng và năm
        return bookings.stream()
                .filter(booking -> {
                    try {
                        // Chuyển đổi chuỗi ngày của booking thành đối tượng Date
                        Date bookingDate = dateFormat1.parse(booking.getBooking_date());
                        // Tạo Calendar từ ngày của booking
                        Calendar bookingCalendar = Calendar.getInstance();
                        bookingCalendar.setTime(bookingDate);
                        // So sánh tháng và năm của booking với tháng và năm cần tìm
                        return bookingCalendar.get(Calendar.MONTH) == targetCalendar.get(Calendar.MONTH)
                                && bookingCalendar.get(Calendar.YEAR) == targetCalendar.get(Calendar.YEAR);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    private static int convertMonthStringToInteger(String monthString) {
        // Chia chuỗi thành các từ riêng biệt
        String[] parts = monthString.split(" ");
        // Lấy phần số từ chuỗi
        String numberString = parts[1];
        // Chuyển đổi phần số thành số nguyên
        int month = Integer.parseInt(numberString);
        return month;
    }
    public void thongKeTheoNam() {
        int number = Integer.parseInt(selectYear);
        List<Booking> filteredBookings = filterBookingsByYear(lstAllBooking, number);
        TongThuNhap(filteredBookings);
        if (bookingAdapter != null) {
            bookingAdapter.clear();
        }
        bookingAdapter = new BookingAdapter(this, filteredBookings);

        lstThongKe.setAdapter(bookingAdapter);
    }
    public static List<Booking> filterBookingsByYear(List<Booking> bookings, int year) {
        // Khởi tạo Calendar để đặt năm cần tìm
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.set(Calendar.YEAR, year);

        // Lọc danh sách bookings dựa trên năm
        return bookings.stream()
                .filter(booking -> {
                    try {
                        // Chuyển đổi chuỗi ngày của booking thành đối tượng Date
                        Date bookingDate = dateFormat1.parse(booking.getBooking_date());
                        // Tạo Calendar từ ngày của booking
                        Calendar bookingCalendar = Calendar.getInstance();
                        bookingCalendar.setTime(bookingDate);
                        // So sánh năm của booking với năm cần tìm
                        return bookingCalendar.get(Calendar.YEAR) == targetCalendar.get(Calendar.YEAR);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }
    public void FieldFillter() {
        lstFieldID.clear();
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("soccer_fields");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dp : snapshot.getChildren()) {
                    String fieldOwner = dp.child("field_owner").getValue(String.class);
                    // So sánh giá trị "field_owner" với username của người đăng nhập
                    if (fieldOwner != null && fieldOwner.equals(UserSession.getInstance().getUsername())) {
                        // Nếu trùng khớp, thêm khóa của sân vào danh sách
                        lstFieldID.add(dp.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void BookingFillter() {
        FieldFillter();
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("bookings");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstAllBooking.clear(); // Xóa danh sách hoá đơn cũ
                for (DataSnapshot dp : snapshot.getChildren()) {
                    // Lấy giá trị của trường "field_id" từ DataSnapshot
                    String fieldId = dp.child("field_id").getValue(String.class);
                    // Kiểm tra xem fieldId có trong danh sách lstFieldID không
                    if (lstFieldID.contains(fieldId)) {
                        // Nếu có, thêm hoá đơn vào danh sách lstAllBooking
                        Booking bk = dp.getValue(Booking.class);
                        lstAllBooking.add(bk);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void TongThuNhap(List<Booking> bookings) {
        double total1 = 0.0;
        for (Booking bk : bookings) {
            if (bk.getBooking_status().equals("dat")) {
                total1 += bk.getPrice();
            }

        }
        DecimalFormat decimalFormat = new DecimalFormat("#,##0 VND");
        String formattedPrice = decimalFormat.format(total1);
        total.setText("Tổng thu nhập: " + formattedPrice);
    }

}
