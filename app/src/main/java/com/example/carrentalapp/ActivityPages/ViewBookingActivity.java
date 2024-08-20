package com.example.carrentalapp.ActivityPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.carrentalapp.Model.DataGridView;
import com.example.carrentalapp.Model.Insurance;
import com.example.carrentalapp.Model.Rent;
import com.example.carrentalapp.R;
import com.example.carrentalapp.Session.Session;
import com.example.carrentalapp.utils.Constants;
import com.example.carrentalapp.utils.OkHttpHelper;
import com.example.carrentalapp.utils.ToastUtil;
import com.example.carrentalapp.utils.Tools;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ViewBookingActivity extends AppCompatActivity {

    private Button back, returnCar;

    //DRIVER DETAILS
    private TextView name, email, phoneNumber;

    //BOOKING SUMMARY
    private TextView bookingID, vehicleName, rate, totalDays, _pickup, _return, insurance, insuranceRate, totalCost;

    //BOOKING
    private Rent rent;
    //INSURANCE
    private Insurance chosenInsurance = new Insurance("基础");
    //VEHICLE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booking);
        loadData();
        initComponents();
        listenHandler();
        displayCustomerInformation();
        displaySummary();
        displayTotalCost();
    }

    private void loadData() {
        Intent intent = getIntent();
        rent = (Rent) intent.getSerializableExtra("RENT");
    }

    private void initComponents() {
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
        bookingID = findViewById(R.id.bookingID);
        returnCar = findViewById(R.id.returnCar);
    }

    private void listenHandler() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        returnCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchDeleteOrder();
            }
        });
    }

    private void fetchDeleteOrder() {
        //http://localhost:8080/carRental_war/rent/deleteRent?rentid=CZ_2024_0316_61550_0856500
        Map<String, String> formData = new HashMap<>();
        formData.put("rentid", rent.getRentid());
        OkHttpHelper.doPostAsync(Constants.BASE_URL + "/rent/deleteRent", formData, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                // 处理请求失败的情况，例如显示错误信息给用户
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // 处理请求成功的情况
                if (response.isSuccessful()) {
                    // 获取返回的JSON字符串
                    String responseBody = response.body().string();
                    try {
                        // 解析返回的JSON字符串
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        int code = jsonResponse.getInt("code");
                        String msg = jsonResponse.getString("msg");
                        // 判断code是否为0，即注册是否成功
                        if (code >= 0) {
                            ToastUtil.showToastShort(getBaseContext(), "换车成功");
                            // 跳转到主页面
                            Intent intent = new Intent(ViewBookingActivity.this, UserViewActivity.class);
                            startActivity(intent);
                            finish(); // 结束当前Activity

                        } else {
                            // 注册失败，显示错误信息给用户
                            ToastUtil.showToastShort(getBaseContext(), "网络错误");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    // 请求失败，显示错误信息给用户
                    ToastUtil.showToastShort(getBaseContext(), "系统错误");
                }
                // 关闭response
                response.close();
            }
        });
    }

    private void displayCustomerInformation() {
        String username = Session.read(getBaseContext(), "isLoggedIn", "admin");
        name.setText(username);
        //DISPLAY DRIVER INFO
        bookingID.setText("BookingID: " + rent.getIdentity());
    }

    private void displaySummary() {

//        vehicleName.setText(vehicle.fullTitle());
//        rate.setText("$"+vehicle.getPrice()+"/Day");
        totalDays.setText(String.valueOf((int) Tools.getDaysDifference(new Date(), rent.getReturndate())));
        _pickup.setText(Tools.formatDateTime(rent.getBegindate()));
        _return.setText(Tools.formatDateTime(rent.getReturndate()));
        insurance.setText(chosenInsurance.getCoverageType());
        insuranceRate.setText("$" + chosenInsurance.getCost());
    }

    private void displayTotalCost() {
//        double cost = calculateTotalCost();
//        totalCost.setText("$"+cost);
    }


    private long getDayDifference(Calendar start, Calendar end) {
        return ChronoUnit.DAYS.between(start.toInstant(), end.toInstant()) + 2;
    }

//    private double calculateTotalCost(){
//        long _days = getDayDifference(booking.getPickupDate(),booking.getReturnDate());
////        double _vehicleRate = vehicle.getPrice();
//        double _insuranceRate = chosenInsurance.getCost();
//
//        return (_days*_vehicleRate) + _insuranceRate;
//    }


    public void onBackPressed() {
        super.onBackPressed();
        Intent homepage = new Intent(getApplicationContext(), UserViewActivity.class);
        homepage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Will clear out your activity history stack till now
        startActivity(homepage);
    }
}
