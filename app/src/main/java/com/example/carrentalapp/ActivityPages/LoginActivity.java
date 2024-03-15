package com.example.carrentalapp.ActivityPages;

import static com.example.carrentalapp.utils.Constants.BASE_URL;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.room.Room;

import com.example.carrentalapp.Database.CustomerDao;
import com.example.carrentalapp.Database.InsuranceDao;
import com.example.carrentalapp.Database.Project_Database;
import com.example.carrentalapp.Database.VehicleCategoryDao;
import com.example.carrentalapp.Database.VehicleDao;
import com.example.carrentalapp.Model.Customer;
import com.example.carrentalapp.Model.Insurance;
import com.example.carrentalapp.Model.Vehicle;
import com.example.carrentalapp.Model.VehicleCategory;
import com.example.carrentalapp.R;
import com.example.carrentalapp.Session.Session;
import com.example.carrentalapp.utils.OkHttpHelper;
import com.example.carrentalapp.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {

    private TextView register;
    private TextView forgotPass;
    private Button login;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        //IF USER ALREADY LOGGED IN => REDIRECT TO HOME PAGE
        Boolean isLoggedIn = Boolean.valueOf(Session.read(LoginActivity.this, "isLoggedIn", "false"));
        if (isLoggedIn) {
            Intent homePage = new Intent(LoginActivity.this, UserViewActivity.class);
            startActivity(homePage);
        }
        initComponents();
        iFRegisterTo();
        clickListenHandler();

    }

    private void iFRegisterTo() {
        Intent intent = getIntent();
        String username1 = intent.getStringExtra("username");
        String password1 = intent.getStringExtra("password");
        if (username1 != null && password1 != null) {
            email.setText(username1);
            password.setText(password1);
        }
    }

    //This will initialize all the clickable components in Login page
    private void initComponents() {
        //Register Button
        register = findViewById(R.id.register);
        //Login Button
        login = findViewById(R.id.login);
        //Forgot Password Button
        forgotPass = findViewById(R.id.forgot_password);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }


    //This will handle all the click events on the login page
    private void clickListenHandler() {
        //Register Listener
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerPage = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(registerPage);
            }
        });

        //Login Listener
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取用户名和密码
                String username = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                // 发送网络API请求进行登录验证，这里假设使用异步方式
                // 这里应该调用您的网络请求方法，验证用户名和密码
                // 假设登录成功的情况
                String url = BASE_URL + "/user/login";
                Map<String, String> formData = new HashMap<>();
                formData.put("loginname", username);
                formData.put("pwd", pass);
                OkHttpHelper.doPostAsync(url, formData, new Callback() {
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
                                    ToastUtil.showToastShort(getBaseContext(), msg);
                                    // 注册成功，跳转到LoginActivity并传递用户名和密码
                                    // 保存用户名和密码到会话中
                                    Session.save(getApplicationContext(), "isLoggedIn", "true");
                                    // 跳转到主页面
                                    Intent intent = new Intent(LoginActivity.this, UserViewActivity.class);
                                    startActivity(intent);
                                    finish(); // 结束当前Activity

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
        });

        //Forgot Password Listener
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}
