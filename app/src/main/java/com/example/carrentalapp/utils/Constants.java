package com.example.carrentalapp.utils;

public class Constants {

    // 网络请求相关
//    public static final String BASE_URL = "http://localhost:8080/carRental_war";
//    public static final String BASE_URL = "http://10.0.2.2:8080/carRental_war";
    public static final String BASE_URL = "http://192.168.174.25:8080/carRental_war";
    public static final int TIMEOUT_IN_SECONDS = 30;

    // SharedPreferences键值
    public static final String PREF_NAME = "MyAppPrefs";
    public static final String PREF_KEY_USERNAME = "username";
    public static final String PREF_KEY_TOKEN = "token";

    // Intent传递参数的键值
    public static final String INTENT_EXTRA_USER_ID = "user_id";
    public static final String INTENT_EXTRA_USER_NAME = "user_name";

    // 日志标签
    public static final String LOG_TAG = "MyApp";

    // 其他常量
    public static final int MAX_RETRY_COUNT = 3;
    public static final int PAGE_SIZE = 20;
    public static final int DEFAULT_ANIMATION_DURATION = 300;
}
