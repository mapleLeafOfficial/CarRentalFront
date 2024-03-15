package com.example.carrentalapp.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.eclipsesource.json.Json;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OkHttpHelper {
    private static final OkHttpClient client = new OkHttpClient();
    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static final MediaType JSON = MediaType.parse("application/json;charset=UTF-8");

    public static void doPostAsync(String url, Map<String, String> formData, Callback callback) {
        logURL(url, formData.toString());

        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : formData.entrySet()) {
            formBuilder.add(entry.getKey(), entry.getValue());
        }
        RequestBody body = formBuilder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public interface OkHttpCallback {
        void onSuccess(String response);

        void onFailure(IOException e);
    }
    // 同步发送POST请求
    public static String doPostSync(String url, String json) throws IOException {
        logURL( url,  json);
        RequestBody body = RequestBody.create(JSON, json);
        FormBody formBody = new FormBody.Builder()
                .add("name", "android基础")
                .add("price", "50")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    // 异步发送POST请求
//    public static void doPostAsync(String url, String json, Callback callback) {
//        logURL( url,  json);
//        RequestBody body = RequestBody.create(JSON, json);
//        Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .build();
//        client.newCall(request).enqueue(callback);
//    }
    //get请求
    public static void get(String url, final OkHttpCallback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, final Response response) {
                if (!response.isSuccessful()) {
                    onFailure(call, new IOException("Unexpected code " + response));
                    return;
                }

                try {
                    final String responseData = response.body().string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(responseData);
                        }
                    });
                } catch (IOException e) {
                    onFailure(call, e);
                }
            }

            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(e);
                    }
                });
            }
        });
    }
    // 记录URL的日志
    private static void logURL(String url, String json) {
        String logMessage = "URL: " + url;
        if (json != null && !json.isEmpty()) {
            logMessage += "\nJSON: " + json;
        }
        Log.d("OkHttpHelper", logMessage);
    }
}