package com.example.carrentalapp.ActivityPages;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.carrentalapp.Model.Insurance;
import com.example.carrentalapp.Model.Rent;
import com.example.carrentalapp.Model.Vehicle;
import com.example.carrentalapp.R;
import com.example.carrentalapp.Session.Session;
import com.example.carrentalapp.utils.Tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class BookingCarActivity extends AppCompatActivity {

    //PICKUP AND RETURN DATE
    private TextView pickupDate, returnDate;

    //PICKUP AND RETURN TIME
    private TextView pickupTime, returnTime;

    //PICKUP DATE/TIME
    private Calendar _pickup;

    //RETURN DATE/TIME
    private Calendar _return;

    //DRIVER DETAILS
    private EditText firstName, phoneNumber;
    private RadioGroup customerTitle;

    private Vehicle vehicle;
    private String insuranceid;


    //BY DEFAULT TITLE SELECTION
    String mrMs = "mr";

    //DATE FORMAT -> FOR DISPLAY PURPOSE
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM, d yyyy", Locale.CANADA);
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.CANADA);

    //DATE/TIME STORING
    //GOING BACK BUTTON and CONTINUE BOOKING BUTTON
    private Button back, continueBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_car);
        Intent intent = getIntent();
        vehicle = (Vehicle) intent.getSerializableExtra("VEHICLE");
        insuranceid = intent.getStringExtra("INSURANCE");
        initComponents();
        listenHandler();
    }

    private void initComponents() {
        //BACK BUTTON
        back = findViewById(R.id.back);
        //CONTINUE BOOKING
        continueBooking = findViewById(R.id.continueBooking);
        //CAR RENTAL DATE AND TIME
        pickupDate = findViewById(R.id.pickupDate);
        pickupTime = findViewById(R.id.pickupTime);
        returnDate = findViewById(R.id.returnDate);
        returnTime = findViewById(R.id.returnTime);
        //DRIVER DETAILS
        customerTitle = findViewById(R.id.mrMsTitle);
        firstName = findViewById(R.id.firstName);
        phoneNumber = findViewById(R.id.phoneNumber);
        //PICKUP AND RETURN DATE OBJECT
        _pickup = Calendar.getInstance();
        _return = Calendar.getInstance();
        //SET THE DATE AND TIME TO CURRENT
        pickupDate.setText(dateFormat.format(_pickup.getTime()));
        pickupTime.setText(timeFormat.format(_pickup.getTime()));
        returnDate.setText(dateFormat.format(_return.getTime()));
        returnTime.setText(timeFormat.format(_return.getTime()));
        firstName.setText(Session.read(getBaseContext(), "isLoggedIn", "admin"));
    }

    //LISTEN HANDLER
    private void listenHandler() {
        //GOING BACK BUTTON
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //PICKUP DATE AND TIME LISTENER
        pickupDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar(_pickup,pickupDate);
            }
        });
        pickupTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(_pickup, pickupTime);
            }
        });

        //RETURN DATE AND TIME LISTENER
        returnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar(_return,returnDate);
            }
        });
        returnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openTimePicker(_return, returnTime);
            }
        });

        continueBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //收集所有数据：
                String carnumber = vehicle.getCarnumber();
                String opername = Session.read(getBaseContext(), "isLoggedIn", "admin");
                Date _returnT = _return.getTime();
                Date _pickupT = _pickup.getTime();
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // 使用 SimpleDateFormat 对象将 Date 对象格式化为字符串
                String returndate = sdf.format(_returnT);
                String begindate = sdf.format(_pickupT);
                String identity = Tools.generateOrderNumber(18);
                Double price = vehicle.getRentprice();
                //价格
                String rentid = Tools.generateRentID();
                if (insuranceid.equals("基础")){
                    price += 15;
                } else if (insuranceid.equals("高级")) {
                    price += 25;
                }
                Date currentDate = new Date();
                Rent rent = new Rent(rentid,price,_pickupT,_returnT,1,identity,carnumber,opername,currentDate);
                Intent bookingSummaryPage = new Intent(BookingCarActivity.this, BookingSummaryActivity.class);
                bookingSummaryPage.putExtra("RENT",rent);
                bookingSummaryPage.putExtra("INSURANCE",new Insurance(insuranceid));
                bookingSummaryPage.putExtra("VEHICLE",vehicle);
                startActivity(bookingSummaryPage);
            }
        });
    }




    //OPEN CALENDAR DIALOG

    private void openCalendar(final Calendar rentalDate, final TextView rentalDateText) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);

        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                rentalDate.set(year,month,dayOfMonth);
                rentalDate.set(Calendar.HOUR_OF_DAY, 0);
                rentalDate.set(Calendar.MINUTE, 0);
                rentalDate.set(Calendar.SECOND, 0);
                rentalDateText.setText(dateFormat.format(rentalDate.getTime()));
            }
        });
        datePickerDialog.show();
    }
    //OPEN TIMEPICKER DIALOG
    private Date openTimePicker(final Calendar rentalTime, final TextView rentalTimeText){
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                rentalTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
                rentalTime.set(Calendar.MINUTE,minute);

                rentalTimeText.setText(timeFormat.format(rentalTime.getTime()));
            }
        },hour,min,false);

        timePickerDialog.show();

        return calendar.getTime();
    }
}
