package com.example.carrentalapp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Tools {

    public static int pickRandomNumber(int[] numbers) {
        if (numbers==null){
            numbers = new int[]{-47032, -13936668, -4068, -3092272, -9539986};
        }
        // 使用Random类生成随机数
        Random random = new Random();
        // 生成一个0到数组长度-1之间的随机索引
        int randomIndex = random.nextInt(numbers.length);
        // 返回对应随机索引的数字
        return numbers[randomIndex];
    }
    public static String generateOrderNumber(int length) {
        StringBuilder orderNumber = new StringBuilder();

        // 生成随机数字串
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // 生成0到9的随机数字
            orderNumber.append(digit);
        }

        return orderNumber.toString();
    }
    public static String generateRentID() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MMdd");
        String currentDate = dateFormat.format(new Date());

        Random random = new Random();
        int randomNumber1 = random.nextInt(100000);
        int randomNumber2 = random.nextInt(1000000);

        return "CZ_" + currentDate + "_" + String.format("%05d", randomNumber1) + "_" + String.format("%07d", randomNumber2);
    }
    public static long getDaysDifference(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        long startMillis = startCal.getTimeInMillis();
        long endMillis = endCal.getTimeInMillis();
        // 一天的毫秒数
        long millisPerDay = 1000 * 60 * 60 * 24;
        // 计算相隔天数并返回
        return (endMillis - startMillis) / millisPerDay;
    }

    public static boolean isValidString(String password) {
        // 检查密码是否为空
        if (password.isEmpty()) {
            return false;
        }

        // 检查密码是否为"*******"
        if (password.equals("*******")) {
            return false;
        }

        // 检查密码长度是否大于3位
        if (password.length() <= 3) {
            return false;
        }

        // 如果上述条件都不满足，则密码有效
        return true;
    }
    public static String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm '星期'E'，'MM'月'dd'日'", Locale.CHINA);
        // 设置为晚上11:00
        date.setHours(23);
        date.setMinutes(0);
        return sdf.format(date);
    }
}
