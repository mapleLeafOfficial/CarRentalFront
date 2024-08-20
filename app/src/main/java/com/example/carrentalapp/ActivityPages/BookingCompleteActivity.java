package com.example.carrentalapp.ActivityPages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.carrentalapp.Model.Booking;
import com.example.carrentalapp.Model.Customer;
import com.example.carrentalapp.Model.Insurance;
import com.example.carrentalapp.Model.Vehicle;
import com.example.carrentalapp.R;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class BookingCompleteActivity extends AppCompatActivity {

    private Button back;

    //DRIVER DETAILS
    private TextView name, email, phoneNumber;

    //BOOKING SUMMARY
    private TextView bookingID, vehicleName, rate, totalDays, _pickup, _return, insurance, insuranceRate, totalCost;

    //DATABASE TABLE
    //BOOKING
    private Booking booking;
    //INSURANCE
    private Insurance chosenInsurance;
    //VEHICLE
    private Vehicle vehicle;
    ConstraintLayout bookingGo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_complete);
        initComponents();
        listenHandler();
//        displayCustomerInformation();
//        displaySummary();
//        displayTotalCost();
    }
    private void initComponents() {
        bookingGo = findViewById(R.id.bookingGo);
        back = findViewById(R.id.back);
        //DRIVER DETAILS
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        //BOOKING SUMMARY
        vehicleName = findViewById(R.id.vehicleName);
        rate = findViewById(R.id.rate);
        totalDays = findViewById(R.id.totalDays);
        _pickup = findViewById(R.id.pickup);
        _return = findViewById(R.id.dropoff);
        //INSURANCE TYPE
        insurance = findViewById(R.id.insurance);
        insuranceRate = findViewById(R.id.insuranceRate);
        //TOTAL COST
        totalCost = findViewById(R.id.totalCost);
        //DATABASE TABLE
        //GET BOOKING OBJECT WHICH WAS PASSED FROM PREVIOUS PAGE
        booking = (Booking) getIntent().getSerializableExtra("BOOKING");



        bookingID = findViewById(R.id.bookingID);
    }
    private void listenHandler() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homePage = new Intent(BookingCompleteActivity.this, UserViewActivity.class);
                homePage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homePage);
//                finish();

            }
        });
        bookingGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homePage = new Intent(BookingCompleteActivity.this, UserViewActivity.class);
                homePage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homePage);
            }
        });
    }
    private void displayCustomerInformation() {


        bookingID.setText("BookingID: " + booking.getBookingID());
    }
    private void displaySummary(){
//
//        vehicleName.setText(vehicle.fullTitle());
//        rate.setText("$"+vehicle.getPrice()+"/Day");
        totalDays.setText(getDayDifference(booking.getPickupDate(),booking.getReturnDate())+" Days");
        _pickup.setText(booking.getPickupTime());
        _return.setText(booking.getReturnTime());

        insurance.setText(chosenInsurance.getCoverageType());
        insuranceRate.setText("$"+chosenInsurance.getCost());
    }
    private void displayTotalCost(){
//        double cost = calculateTotalCost();
//        totalCost.setText("$"+cost);
    }
    private long getDayDifference(Calendar start, Calendar end){
        return ChronoUnit.DAYS.between(start.toInstant(), end.toInstant())+2;
    }

//    private double calculateTotalCost(){
//        long _days = getDayDifference(booking.getPickupDate(),booking.getReturnDate());
//        double _vehicleRate = vehicle.getPrice();
//        double _insuranceRate = chosenInsurance.getCost();
//
//        return (_days*_vehicleRate) + _insuranceRate;
//    }
    public void onBackPressed(){
        super.onBackPressed();
        Intent homepage = new Intent(getApplicationContext(), UserViewActivity.class);
        homepage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Will clear out your activity history stack till now
        startActivity(homepage);
    }
}
