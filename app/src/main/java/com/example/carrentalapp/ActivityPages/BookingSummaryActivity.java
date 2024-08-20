package com.example.carrentalapp.ActivityPages;

import static android.app.PendingIntent.getActivity;
import static com.example.carrentalapp.utils.Tools.formatDateTime;
import static com.example.carrentalapp.utils.Tools.getDaysDifference;

import static java.nio.charset.StandardCharsets.UTF_8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carrentalapp.Adapter.VehicleCategoryAdapter;
import com.example.carrentalapp.FragmentPages.VehicleCategoryFragment;
import com.example.carrentalapp.Model.CarType;
import com.example.carrentalapp.Model.DataGridView;
import com.example.carrentalapp.Model.HttpResponse;
import com.example.carrentalapp.Model.Insurance;
import com.example.carrentalapp.Model.Rent;
import com.example.carrentalapp.Model.Vehicle;
import com.example.carrentalapp.R;
import com.example.carrentalapp.Session.Session;
import com.example.carrentalapp.utils.Constants;
import com.example.carrentalapp.utils.OkHttpHelper;
import com.example.carrentalapp.utils.ToastUtil;
import com.example.carrentalapp.utils.Tools;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BookingSummaryActivity extends AppCompatActivity {

    private Button back, book, payNow;

    //DRIVER DETAILS
    private TextView name, email;

    //BOOKING SUMMARY
    private TextView vehicleName, rate, totalDays, _pickup, _return, insurance, insuranceRate, totalCost;

    //VEHICLE IMAGE
    private ImageView vehicleImage;

    //DATABASE TABLE


    //BOOKING
    //INSURANCE
    private Insurance chosenInsurance;
    //VEHICLE
    private Vehicle vehicle;
    private Rent rent;
    private Insurance insuranceJ;

    private ProgressBar paidLoading;
    private long daysDifference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);
        initComponents();
        Wave wave = new Wave();
        paidLoading.setIndeterminateDrawable(wave);
        Intent intent = getIntent();
        rent = (Rent) intent.getSerializableExtra("RENT");
        vehicle = (Vehicle) intent.getSerializableExtra("VEHICLE");
        insuranceJ = (Insurance) intent.getSerializableExtra("INSURANCE");
        listenHandler();
        displayCustomerInformation();
        displaySummary();
    }

    private void initComponents() {
        back = findViewById(R.id.back);
        book = findViewById(R.id.book);
        payNow = findViewById(R.id.payNow);

        //DRIVER DETAILS
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);

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

        //VEHICLE IMAGE
        vehicleImage = findViewById(R.id.vehicleImage);


        //GET BOOKING OBJECT WHICH WAS PASSED FROM PREVIOUS PAGE


        paidLoading = findViewById(R.id.paidLoading);
        paidLoading.setVisibility(View.INVISIBLE);
    }

    private void listenHandler() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!book.isEnabled()) {
                    return;
                }
                Intent bookingCompletePage = new Intent(BookingSummaryActivity.this, BookingCompleteActivity.class);
                startActivity(bookingCompletePage);
            }
        });
        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paidLoading.setVisibility(View.VISIBLE);
                //网络请求
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // 使用 SimpleDateFormat 对象将 Date 对象格式化为字符串
                String returndate = sdf.format(rent.getReturndate());
                String begindate = sdf.format(rent.getBegindate());
                Map<String, String> formData = new HashMap<>();
                formData.put("rentid", rent.getRentid());
                formData.put("price", String.valueOf(daysDifference * vehicle.getRentprice()));
                formData.put("begindate", begindate);
                formData.put("returndate", returndate);
                formData.put("rentflag", "1");
                formData.put("identity", rent.getIdentity());
                String encodedCarNumber = null;
                try {
                    encodedCarNumber = URLEncoder.encode(rent.getCarnumber(), StandardCharsets.UTF_8.name());
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                formData.put("carnumber", encodedCarNumber);
                formData.put("opername", Session.read(getBaseContext(), "isLoggedIn", "admin"));
                // 发起网络请求
                String url = Constants.BASE_URL + "/rent/saveRent";

                OkHttpHelper.doPostAsync(url, formData, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        // 处理请求失败情况
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            // 将JSON转换为ArrayList<CarType>对象
                            Gson gson = new Gson();
                            final HttpResponse httpResponse = gson.fromJson(responseData, HttpResponse.class);
                            if (httpResponse.getCode() >= 0) {
                                // 更新UI，需要在UI线程中执行
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        paidLoading.setVisibility(View.INVISIBLE);
                                        payNow.setText("Paid");
                                        payNow.setEnabled(false);
                                        book.setEnabled(true);
                                        ToastUtil.showToastShort(getBaseContext(), httpResponse.getMsg());
                                    }
                                });
                            } else {
                                // 处理请求失败情况
                                ToastUtil.showToastShort(getBaseContext(), httpResponse.getMsg());
                            }
                        }
                    }
                });
                //request
            }
        });
    }

    private void displayCustomerInformation() {
        name.setText(Session.read(getBaseContext(), "isLoggedIn", "admin"));
        Date begindate = rent.getBegindate();
        Date returndate = rent.getReturndate();
        _pickup.setText(formatDateTime(begindate));
        _return.setText(formatDateTime(returndate));
        daysDifference = getDaysDifference(begindate, returndate);
        totalDays.setText(String.valueOf(daysDifference));
        if (daysDifference == 0) {
            daysDifference = 1;
        }
        totalCost.setText(String.valueOf(daysDifference * vehicle.getRentprice()));
    }



    private void displaySummary() {
        vehicleName.setText(vehicle.getDescription());
        rate.setText("$" + vehicle.getRentprice() + "/Day");
        insurance.setText(insuranceJ.getInsuranceID());
        insuranceRate.setText("$" + insuranceJ.getCost());
        String imagePath = "android.resource://" + getBaseContext().getPackageName() + "/" + R.drawable.suv;
        Picasso.get().load(imagePath).into(vehicleImage);
    }
}
