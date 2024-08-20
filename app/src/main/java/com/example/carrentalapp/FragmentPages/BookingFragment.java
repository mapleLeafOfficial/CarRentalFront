package com.example.carrentalapp.FragmentPages;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.carrentalapp.ActivityPages.ViewBookingActivity;
import com.example.carrentalapp.Adapter.BookingAdapter;
import com.example.carrentalapp.Model.Booking;
import com.example.carrentalapp.Model.CarType;
import com.example.carrentalapp.Model.DataGridView;
import com.example.carrentalapp.Model.Rent;
import com.example.carrentalapp.Model.Rent;
import com.example.carrentalapp.R;
import com.example.carrentalapp.Session.Session;
import com.example.carrentalapp.utils.Constants;
import com.example.carrentalapp.utils.OkHttpHelper;
import com.example.carrentalapp.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
/**
 * A simple {@link Fragment} subclass.
 */
public class BookingFragment extends Fragment implements BookingAdapter.onBookingListener{

    private RecyclerView recyclerView;
    private ArrayList<Rent> rents = new ArrayList<>();
    private BookingAdapter bookingAdapter;


    public BookingFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bookingAdapter = new BookingAdapter(getContext(),rents,this);
        recyclerView.setAdapter(bookingAdapter);
        fetchBookingData();
    }
    private void fetchBookingData() {
        String username = Session.read(getContext(), "isLoggedIn", "admin");
        Map<String, String> formData = new HashMap<>();
        formData.put("opername", username);
        formData.put("page", "1");
        formData.put("limit", "10");
        OkHttpHelper.doPostAsync(Constants.BASE_URL + "/rent/loadAllRent", formData, new Callback() {
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
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    DataGridView<LinkedTreeMap> dataGridView = gson.fromJson(responseBody, DataGridView.class);
                    Object data = dataGridView.getData();
                    String jsonString = gson.toJson(data);
                    Type type = new TypeToken<ArrayList<Rent>>() {
                    }.getType();
                    final ArrayList<Rent> rents1 = gson.fromJson(jsonString, type);
                    if (dataGridView.getCode()>=0){
                        ToastUtil.showToastShort(getContext(),"刷新数据成功"+dataGridView.getMsg());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateData(rents1);
                            }
                        });
                    }else{
                        ToastUtil.showToastShort(getContext(),"更新错误");
                    }
                    // 关闭response
                    response.close();
                }
            }
        });
    }
    private void updateData(List<Rent> tlist) {
        rents.clear();
        rents.addAll(tlist);
        bookingAdapter.notifyDataSetChanged(); //刷新
    }
    @Override
    public void onClick(int position) {
        Rent rent = rents.get(position);
        Intent viewBooking = new Intent(getContext(), ViewBookingActivity.class);
        viewBooking.putExtra("RENT",rent);
        startActivity(viewBooking);
    }
}
