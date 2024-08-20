package com.example.carrentalapp.FragmentPages;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.carrentalapp.Adapter.VehicleCategoryAdapter;

import com.example.carrentalapp.Model.CarType;
import com.example.carrentalapp.Model.DataGridView;
import com.example.carrentalapp.R;
import com.example.carrentalapp.utils.Constants;
import com.example.carrentalapp.utils.OkHttpHelper;
import com.example.carrentalapp.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class VehicleCategoryFragment extends Fragment implements VehicleCategoryAdapter.onCategoryListener {



    private RecyclerView recyclerView;
    private VehicleCategoryAdapter adapter;
    private ArrayList<CarType> list = new ArrayList<>();
    public VehicleCategoryFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicle_category, container, false);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VehicleCategoryAdapter(getContext(), list,this);
        recyclerView.setAdapter(adapter);
        fetchCarTypes();
    }


    // 获取到数据进行更新
    private void updateData(List<CarType> tlist) {
        list.clear();
        list.addAll(tlist);
        adapter.notifyDataSetChanged(); //刷新
    }
    private void fetchCarTypes() {
        // 构建请求参数
        Map<String, String> formData = new HashMap<>();
        formData.put("page", "1");
        formData.put("limit", "4");

        // 发起网络请求
        String url = Constants.BASE_URL +"/car/getAllCartype";
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
                    Type type = new TypeToken<DataGridView<CarType>>() {}.getType();
                    DataGridView<CarType> dataGridView = gson.fromJson(responseData,type);
                    Object data =  dataGridView.getData();
                    String jsonString = gson.toJson(data);
                    type = new TypeToken<ArrayList<CarType>>(){}.getType();
                    final ArrayList<CarType> tlist = gson.fromJson(jsonString, type);
                    // 更新UI，需要在UI线程中执行
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            updateData(tlist);
                            list= tlist;
                            adapter = new VehicleCategoryAdapter(getContext(), list,VehicleCategoryFragment.this);
                            recyclerView.setAdapter(adapter);
                        }
                    });
                } else {
                    // 处理请求失败情况
                }
            }
        });
    }
    //DEBUGING
    private void toast(String txt) {
        Toast toast = Toast.makeText(getContext(), txt, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onCategoryClick(int position) {
        toast(list.get(position).getCartype());
        String selectedCategory = list.get(position).getCartype();
        Bundle bundle=new Bundle();
        bundle.putString("CATEGORY", selectedCategory);
        Fragment viewVehicle = new VehicleFragment();
        viewVehicle.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, viewVehicle);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onSelectClick(int position) {
        toast(list.get(position).getCartype() + " Select");
    }

}
