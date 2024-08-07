package com.example.booksportfield.Adapter.ThanhBinhAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.booksportfield.Models.Booking;
import com.example.booksportfield.R;

import java.text.DecimalFormat;
import java.util.List;

public class BookingAdapter extends BaseAdapter {
    private Context context;
    private List<Booking> bookings;
    private LayoutInflater inflater;

    public BookingAdapter(Context context, List<Booking> bookings) {
        this.context = context;
        this.bookings = bookings;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return bookings.size();
    }

    @Override
    public Object getItem(int position) {
        return bookings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Kiểm tra xem convertView có null không, nếu có thì inflate một view mới
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_booking, parent, false);
        }

        // Lấy đối tượng Booking tại vị trí position
        Booking booking = (Booking) getItem(position);

        // Ánh xạ các TextView với các thành phần trong item_booking.xml
        TextView tvBookingId = convertView.findViewById(R.id.tvBookingId);
        TextView tvBookingDate = convertView.findViewById(R.id.tvBookingDate);
        TextView tvBookingStatus = convertView.findViewById(R.id.tvBookingStatus);
        TextView tvPrice = convertView.findViewById(R.id.tvPrice);
        TextView tvRating = convertView.findViewById(R.id.tvRating);
        TextView tvTimeSlotId = convertView.findViewById(R.id.tvTimeSlotId);
        TextView tvFieldId = convertView.findViewById(R.id.tvFieldId);
        //TextView tvUserId = convertView.findViewById(R.id.tvUserId);


        DecimalFormat decimalFormat = new DecimalFormat("#,##0 VND");
        String formattedPrice = decimalFormat.format(booking.getPrice());

        // Gán giá trị cho các TextView
        tvBookingId.setText("Mã hoá đơn: " + booking.getBooking_id());
        tvBookingDate.setText("Ngày đặt: " + booking.getBooking_date());
        tvBookingStatus.setText("Trạng Thái: " + booking.getBooking_status());
        tvPrice.setText("Giá: " + formattedPrice);
        tvRating.setText("Đánh giá: " + booking.getRating());
        tvTimeSlotId.setText("Mã khung giờ: " + booking.getTime_slot_id());
        tvFieldId.setText("Mã sân: " + booking.getField_id());
        //tvUserId.setText("User ID: " + booking.getUser_id());

        // Trả về convertView đã được gán giá trị
        return convertView;
    }

    public void clear() {
        bookings.clear();
        notifyDataSetChanged();
    }

}
