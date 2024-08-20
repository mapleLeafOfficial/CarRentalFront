package com.example.carrentalapp.Session;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.Arrays;

public class Session {

    public static void close(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("session",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void save(Context context, String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("session",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }
    public static void saveIntArray(Context context, String key, int[] values) {
        // 将整型数组转换为以逗号分隔的字符串
        String intArrayAsString = TextUtils.join(",", Arrays.asList(values));

        // 保存字符串到 SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, intArrayAsString);
        editor.apply();
    }
    public static int[] getIntArray(Context context, String key) {
        // 从 SharedPreferences 中获取保存的字符串
        SharedPreferences sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        String intArrayAsString = sharedPreferences.getString(key, "");

        // 将保存的字符串解析为整型数组
        if (!TextUtils.isEmpty(intArrayAsString)) {
            String[] intArrayAsStrings = intArrayAsString.split(",");
            int[] intArray = new int[intArrayAsStrings.length];
            for (int i = 0; i < intArrayAsStrings.length; i++) {
                intArray[i] = Integer.parseInt(intArrayAsStrings[i]);
            }
            return intArray;
        } else {
            // 如果没有保存的数据，则返回空数组或 null，取决于您的需求
            return new int[0];
        }
    }
    public static String read(Context context, String key, String defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences("session",Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,defaultValue);
    }
}
