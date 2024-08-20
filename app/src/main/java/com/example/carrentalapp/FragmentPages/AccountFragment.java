package com.example.carrentalapp.FragmentPages;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.carrentalapp.ActivityPages.LoginActivity;
import com.example.carrentalapp.ActivityPages.RegistrationActivity;
import com.example.carrentalapp.Model.User;
import com.example.carrentalapp.Model.User;
import com.example.carrentalapp.Model.DataGridView;
import com.example.carrentalapp.Model.User;
import com.example.carrentalapp.R;
import com.example.carrentalapp.Session.Session;
import com.example.carrentalapp.utils.Constants;
import com.example.carrentalapp.utils.OkHttpHelper;
import com.example.carrentalapp.utils.ToastUtil;
import com.example.carrentalapp.utils.Tools;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private Button logout;
    private Button editButton;
    private EditText addressEditText;
    private EditText usernameEditText;
    private EditText passwordusernameEditText;
    private EditText phoneText;
    private User muser;
    private ProgressBar progressBar;
    private ConstraintLayout TextBlank;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initComponents(view);
        getUserData();
        listenHandler();
        return view;
    }

    private void getUserData() {
        usernameEditText.setText("");
        passwordusernameEditText.setText("");
        phoneText.setText("");
        addressEditText.setText("");
//        http://localhost:8080/carRental_war/user/loadAllUser?loginname=mx&page=1&limit=10
        String username = Session.read(getContext(), "isLoggedIn", "admin");
        Map<String, String> formData = new HashMap<>();
        formData.put("loginname", username);
        formData.put("page", "1");
        formData.put("limit", "10");
        OkHttpHelper.doPostAsync(Constants.BASE_URL + "/user/loadAllUser", formData, new Callback() {
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
                    Gson gson = new Gson();
                    DataGridView<LinkedTreeMap> dataGridView = gson.fromJson(responseBody, DataGridView.class);
                    Object data = dataGridView.getData();
                    String jsonString = gson.toJson(data);
                    Type type = new TypeToken<ArrayList<User>>() {
                    }.getType();
                    final ArrayList<User> user = gson.fromJson(jsonString, type);
                    if (user != null && user.size() != 0) {
                        muser = user.get(0);
                        // 判断code是否为0，即注册是否成功
                        if (dataGridView.getCode() == 0) {
                            ToastUtil.showToastShort(getContext(), dataGridView.toString());
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    usernameEditText.setText(user.get(0).getLoginname());
                                    passwordusernameEditText.setText("*******");
                                    phoneText.setText(user.get(0).getPhone());
                                    addressEditText.setText(user.get(0).getAddress());
                                    TextBlank.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });
//                            startActivity(intent);
                        } else {
                            // 失败，显示错误信息给用户
                            ToastUtil.showToastShort(getContext(), "用户状态异常 ， 请重新登录");
                        }
                    }
                } else {
                    // 请求失败，显示错误信息给用户
                }
                // 关闭response
                response.close();
            }
        });

    }

    private void listenHandler() {

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.close(getContext());
                Intent loginPage = new Intent(getActivity(), LoginActivity.class);
                loginPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginPage);
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trigger flashing animation for the editing field
                if (editButton.getText().equals("EDIT")) {
                    editButton.setText("提交");
                    // Add border to the editing field
                    addBorderToEditingField();
                } else {
                    boolean validString = Tools.isValidString(addressEditText.getText().toString());
                    boolean validString2 = Tools.isValidString(phoneText.getText().toString());
                    boolean validString3 = Tools.isValidString(passwordusernameEditText.getText().toString());
                    if (validString && validString2 && validString3) {
                        fetchUpdateUser();
                    } else {
                        ToastUtil.showToastShort(getContext(), "请重新填写 文本不可为空值");
                    }
                    editButton.setText("EDIT");
                    removeBorderToEditingField();
                }
            }
        });

    }

    private void removeBorderToEditingField() {
        passwordusernameEditText.setBackgroundResource(0);
        phoneText.setBackgroundResource(0);
        addressEditText.setBackgroundResource(0);
        passwordusernameEditText.setEnabled(false);
        phoneText.setEnabled(false);
        addressEditText.setEnabled(false);
    }

    private void fetchUpdateUser() {
        //        http://localhost:8080/carRental_war/user/loadAllUser?loginname=mx&page=1&limit=10
        String username = Session.read(getContext(), "isLoggedIn", "admin");
        Map<String, String> formData = new HashMap<>();
        formData.put("loginname", username);
        formData.put("address", addressEditText.getText().toString());
        formData.put("phone", phoneText.getText().toString());
        formData.put("pwd", passwordusernameEditText.getText().toString());
        OkHttpHelper.doPostAsync(Constants.BASE_URL + "/user/updateUser", formData, new Callback() {
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
                        if (code>=0){
                            ToastUtil.showToastShort(getContext(),"更新成功"+msg);
                        }else{
                            ToastUtil.showToastShort(getContext(),"更新错误");
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    // 关闭response
                    response.close();
                }
            }
        });
    }

    private void initComponents(View view) {
        logout = view.findViewById(R.id.logout2);
        addressEditText = view.findViewById(R.id.addressEditText);
        usernameEditText = view.findViewById(R.id.usernameEditText);
        passwordusernameEditText = view.findViewById(R.id.passwordusernameEditText);
        phoneText = view.findViewById(R.id.phoneText);
        editButton = view.findViewById(R.id.edit_user);
        progressBar = view.findViewById(R.id.progressBar);
        TextBlank = view.findViewById(R.id.Text);
        TextBlank.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void addBorderToEditingField() {
        // Add a border to the editing field
        passwordusernameEditText.setBackgroundResource(R.drawable.editing_field_border);
        phoneText.setBackgroundResource(R.drawable.editing_field_border);
        addressEditText.setBackgroundResource(R.drawable.editing_field_border);
        passwordusernameEditText.setEnabled(true);
        addressEditText.setEnabled(true);
        phoneText.setEnabled(true);
    }
}

