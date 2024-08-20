package com.example.carrentalapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrentalapp.Model.Rent;
import com.example.carrentalapp.R;
import com.example.carrentalapp.utils.Tools;

import java.util.ArrayList;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingHolder>{

    private Context context;
    private ArrayList<Rent> rents;
    private onBookingListener onBookingListener;




    public BookingAdapter(Context context, ArrayList<Rent> list, BookingAdapter.onBookingListener onBookingListener) {
        this.context = context;
        this.rents = list;
        this.onBookingListener = onBookingListener;



    }

    @NonNull
    @Override
    public BookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.booking_card,null);
        return new BookingHolder(view,onBookingListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingHolder bookingHolder, int position) {
        Rent rent = rents.get(position);
        bookingHolder.vehicleName.setText(rent.getCarnumber());
        bookingHolder.bookingID.setText(rent.getIdentity());
        bookingHolder.customerName.setText(rent.getOpername());
        bookingHolder.pickupDate.setText(Tools.formatDateTime(rent.getBegindate()));
        bookingHolder.returnDate.setText(Tools.formatDateTime(rent.getReturndate()));
        bookingHolder.bookingStatus.setText("订单正在生效");
    }

    @Override
    public int getItemCount() { return rents.size(); }

    class BookingHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView vehicleName, bookingID, customerName,
                 pickupDate, returnDate, bookingStatus;
        onBookingListener onBookingListener;

        public BookingHolder(@NonNull View itemView, onBookingListener onBookingListener) {
            super(itemView);
            vehicleName = itemView.findViewById(R.id.vehicleName);
            bookingID = itemView.findViewById(R.id.bookingID);
            customerName = itemView.findViewById(R.id.customerName);
            pickupDate = itemView.findViewById(R.id.pickupDate);
            returnDate = itemView.findViewById(R.id.returnDate);
            bookingStatus = itemView.findViewById(R.id.bookingStatus);
            this.onBookingListener = onBookingListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onBookingListener.onClick(getAdapterPosition());
        }
    }

    public interface onBookingListener{
        void onClick(int position);
    }
}
