package com.searchengine.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String getTodayDate() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = simpleDateFormat.format(date);
        return todayDate;
    }
}
