package com.example.carrentalapp.ActivityPages;

import static com.example.carrentalapp.utils.Constants.BASE_URL;

import android.app.DatePickerDialog;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.room.Room;

import com.example.carrentalapp.Database.CustomerDao;
import com.example.carrentalapp.Database.Project_Database;
import com.example.carrentalapp.Model.*;
import com.example.carrentalapp.R;
import com.example.carrentalapp.utils.OkHttpHelper;
import com.example.carrentalapp.utils.ToastUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class RegistrationActivity extends AppCompatActivity {

    private TextView login;
    private EditText firstNameEditText, middleNameEditText, passwordEditText, confirmPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initComponents();

        clickListenHandler();

    }

    //Initialize all the components in Register Page
    private void initComponents() {
        //Register Button
        //Login Button
        login = findViewById(R.id.login);
        //Expiry Button
        firstNameEditText = findViewById(R.id.firstName);
        middleNameEditText = findViewById(R.id.middleName);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirmPassword);

    }

    //This method handles all the click events
    private void clickListenHandler() {

        //Login Listener
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerPage = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(registerPage);
            }
        });


        // 获取注册按钮
        Button registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调用函数处理注册逻辑
                handleRegistration();
            }
        });
    }

    // 处理注册逻辑的函数
    private void handleRegistration() {
        // 提取用户输入的信息
        String firstName = firstNameEditText.getText().toString().trim();
        String middleName = middleNameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        // 检查密码是否匹配
        if (password.equals(confirmPassword)) {
            registerUser(firstName, middleName, password);
        } else {
            // 密码不匹配，显示错误信息给用户
            // 可以通过Toast或者Snackbar显示错误信息
        }
    }

    // 注册用户的方法
    private void registerUser(final String firstName, String middleName, final String password) {
        Map<String, String> formData = new HashMap<>();
        formData.put("loginname", firstName);
        formData.put("realname", middleName);
        formData.put("pwd", password);
            // 异步发送POST请求
            String url = BASE_URL+"/user/addUser";
            OkHttpHelper.doPostAsync(url,formData , new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    // 处理请求失败的情况，例如显示错误信息给用户
                }
                @Override
                public void onResponse(Call call, final Response response) throws IOException {
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
                            if (code == 0) {
                                ToastUtil.showToastShort(getBaseContext(), "注册成功");
                                // 注册成功，跳转到LoginActivity并传递用户名和密码
                                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                intent.putExtra("username", firstName); // 传递用户名
                                intent.putExtra("password", password); // 传递密码
                                startActivity(intent);
                            } else {
                                // 注册失败，显示错误信息给用户
                                ToastUtil.showToastShort(getBaseContext(), msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // 请求失败，显示错误信息给用户
                        ToastUtil.showToastShort(getBaseContext(), "注册失败");
                    }
                    // 关闭response
                    response.close();
                }
            });
    }
}





