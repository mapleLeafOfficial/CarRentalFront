package com.example.carrentalapp.ActivityPages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carrentalapp.Model.Insurance;
import com.example.carrentalapp.Model.Vehicle;
import com.example.carrentalapp.R;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class VehicleInfoActivity extends AppCompatActivity {

    //VEHICLE OBJECT
    private Vehicle vehicle;
    //VEHICLE TITLE
    private TextView vehicleTitle;
    //VEHICLE IMAGE OBJECT
    private ImageView vehicleImage;
    //VEHICLE PRICE
    private TextView vehiclePrice;

    //VEHICLE AVAILABILITY FIELD
    private ConstraintLayout available;
    private ConstraintLayout notAvailable;

    //GOING BACK BUTTON
    private Button back;
    private Button book;

    //VEHICLE INFO FIELD
    private TextView year, manufacturer, model, mileage, seats, type;

    //INSURANCE OPTION
    private RadioGroup insuranceOption;

    private String chosenInsurance = "";

    //INSURANCE DATABASE TABLE


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_info);
        initComponents();
        listenHandler();
        displayVehicleInfo();
    }

    private void initComponents() {

        //INITIALIZING COMPONENTS
        vehicle = (Vehicle) getIntent().getSerializableExtra("VEHICLE");
        back = findViewById(R.id.back);
        vehicleTitle = findViewById(R.id.vehicleTitle);
        vehicleImage = findViewById(R.id.vehicleImage);
        available = findViewById(R.id.available);
        notAvailable = findViewById(R.id.notAvailable);
        //VEHICLE INFO FIELD
        year = findViewById(R.id.year);
        manufacturer = findViewById(R.id.manufacturer);
        model = findViewById(R.id.model);
        mileage = findViewById(R.id.mileage);
        seats = findViewById(R.id.seats);
        type = findViewById(R.id.type);
        //VEHICLE PRICE
        vehiclePrice = findViewById(R.id.vehiclePrice);
        //INSURANCE OPTION
        insuranceOption = findViewById(R.id.insuranceOption);
        //BOOK BUTTON
        book = findViewById(R.id.book_this_car);
        //INSURANCE DATABASE TABLE
    }

    private void listenHandler() {

        //BACK ARROW BUTTON LISTENER
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //BOOKING BUTTON -> THIS WILL REDIRECT TO BOOKING PAGE
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent informationPage = new Intent(VehicleInfoActivity.this, BookingCarActivity.class);
                informationPage.putExtra("INSURANCE",chosenInsurance);
                informationPage.putExtra("VEHICLE",vehicle);
//                informationPage.putExtra("VEHICLEID",vehicle.getVehicleID()+"");
                startActivity(informationPage);
            }
        });


        insuranceOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton option = findViewById(checkedId);
                chosenInsurance = option.getText().toString().toLowerCase();
            }
        });


    }


    private void displayVehicleInfo() {
        vehicleTitle.setText(vehicle.getDescription());
//        LOADING THE VEHICLE IMAGE
        String imagePath =  "android.resource://" + getBaseContext().getPackageName() + "/" + R.drawable.suv;
        Picasso.get().load(imagePath).into(vehicleImage);
//        IF VEHICLE AVAILABLE => DISPLAY AVAILABLE TEXT
//        IF VEHICLE NOT AVAILABLE => DISPLAY NOT AVAILABLE TEXT
        if(vehicle.getIsrenting()==0){
            available.setVisibility(ConstraintLayout.VISIBLE);
            notAvailable.setVisibility(ConstraintLayout.INVISIBLE);
            book.setEnabled(true);
            book.setBackground(ContextCompat.getDrawable(VehicleInfoActivity.this,R.drawable.round_button));
            book.setText("Book This Car");
        }else{
            available.setVisibility(ConstraintLayout.INVISIBLE);
            notAvailable.setVisibility(ConstraintLayout.VISIBLE);
            book.setEnabled(false);
            book.setBackground(ContextCompat.getDrawable(VehicleInfoActivity.this,R.drawable.disable_button));
            book.setText("Vehicle Not Available");
        }

        //SET VEHICLE SPECS
        Date createtime = vehicle.getCreatetime();
        year.setText(createtime.getYear()+"");
        model.setText(vehicle.getDescription());
        mileage.setText(vehicle.getDeposit()+"");
        type.setText(vehicle.getCartype());
        vehiclePrice.setText("$" + vehicle.getRentprice()+"/Day");
    }
}
