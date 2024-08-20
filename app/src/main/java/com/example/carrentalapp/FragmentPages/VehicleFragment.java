package com.example.carrentalapp.FragmentPages;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.carrentalapp.ActivityPages.VehicleInfoActivity;
import com.example.carrentalapp.Adapter.VehicleAdapter;
import com.example.carrentalapp.Adapter.VehicleCategoryAdapter;
import com.example.carrentalapp.Model.CarType;
import com.example.carrentalapp.Model.DataGridView;
import com.example.carrentalapp.Model.Vehicle;
import com.example.carrentalapp.R;
import com.example.carrentalapp.utils.Constants;
import com.example.carrentalapp.utils.OkHttpHelper;
import com.example.carrentalapp.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class VehicleFragment extends Fragment implements VehicleAdapter.onVehicleListener {

    private String selectVehicleCategory;
    private RecyclerView recyclerView;
    private ArrayList<Vehicle> list = new ArrayList<>();
    private VehicleAdapter adapter;

    public VehicleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicle, container, false);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {

        selectVehicleCategory = getArguments().getString("CATEGORY");
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Bundle bundle = getArguments();
        if (bundle != null) {
            // 从Bundle中提取数据
            String category = bundle.getString("CATEGORY");
            if (category != null) {
                // 使用获取到的数据进行后续操作
                // 例如，您可以显示Toast来验证是否成功传递了数据
                adapter = new VehicleAdapter(getContext(), list,this);
                recyclerView.setAdapter(adapter);
                fetchVehicleData(category);
            }
        }
    }

    private void fetchVehicleData(String category) {
        // 构建请求参数
        Map<String, String> formData = new HashMap<>();
        formData.put("page", "1");
        formData.put("limit", "10");
//        formData.put("isrenting", String.valueOf(0));
        try {
            formData.put("cartype", URLEncoder.encode(category,StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        // 发起网络请求
        String url = Constants.BASE_URL + "/car/loadAllCar";
        OkHttpHelper.doPostAsync(url, formData, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // 处理请求失败情况
                ToastUtil.showToastShort(getActivity().getBaseContext(),"请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    // 将JSON转换为ArrayList<Vehicle>对象
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH-mm-ss").create();
                    Type type = new TypeToken<DataGridView<Vehicle>>() {}.getType();
                    DataGridView<Vehicle> dataGridView = gson.fromJson(responseData,type);
                    Object data =  dataGridView.getData();
                    String jsonString = gson.toJson(data);
                    type = new TypeToken<ArrayList<Vehicle>>(){}.getType();
                    final ArrayList<Vehicle> tlist = gson.fromJson(jsonString, type);
//                    ToastUtil.showToastShort(getActivity().getBaseContext(),tlist.toString());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            freshData(tlist);
                        }
                    });
                } else {
                    ToastUtil.showToastShort(getActivity().getBaseContext(), "网络请求失败");
                    Log.e("TAGDEBUG","2");
                    // 处理请求失败情况
                }
            }
        });
    }

    private void freshData(ArrayList<Vehicle> tlist) {
        list.clear();
        list.addAll(tlist);
        adapter.notifyDataSetChanged(); //刷新
    }

    @Override
    public void onClick(int position) {
        Intent vehicleInfoPage = new Intent(getActivity(), VehicleInfoActivity.class);
        vehicleInfoPage.putExtra("VEHICLE", list.get(position));
        startActivity(vehicleInfoPage);
    }


    //DEBUGING
    private void toast(String txt) {
        Toast toast = Toast.makeText(getContext(), txt, Toast.LENGTH_SHORT);
        toast.show();
    }
}
