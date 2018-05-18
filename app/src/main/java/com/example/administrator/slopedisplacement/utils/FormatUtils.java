package com.example.administrator.slopedisplacement.utils;

import android.text.TextUtils;
import android.util.Log;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 格式化工具类
 */

public class FormatUtils {
    public static String round(double value) {
        return new BigDecimal(value).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static String format2(double value) {
        return String.format("%.2f", value);
    }

    public static String format3(double value) {
        return String.format("%.3f", value);
    }

    private final static String FORMAT_TIME = "yyyy-MM-dd";
    private final static String FORMAT_TIME_MONTH = "yyyy-MM";
    private final static String FORMAT_TIME_YEAR = "yyyy";

    /**
     * 将Date类型格式化成String yyyy-MM-dd
     *
     * @param date 时间
     * @return
     */
    public static String dateToString(Date date) {
        if (date == null) {
            return "";
        } else {
            return new SimpleDateFormat(FORMAT_TIME).format(date);
        }
    }

    /**
     * 将Date类型格式化成String yyyy-MM-dd
     *
     * @param date 时间
     * @return
     */
    public static String dateToString(Date date, boolean month, boolean day) {
        if (date == null) {
            return "";
        } else {
            if (month == true && day == true) {
                return new SimpleDateFormat(FORMAT_TIME).format(date);
            } else if (month == true && day == false) {
                return new SimpleDateFormat(FORMAT_TIME_MONTH).format(date);
            }
            return new SimpleDateFormat(FORMAT_TIME_YEAR).format(date);

        }
    }

    /**
     * 将Calendar类型格式化成String yyyy-MM-dd
     *
     * @param date 时间
     * @return
     */
    public static String calendarToString(Calendar date) {
        if (date == null) {
            return "";
        } else {
            return new SimpleDateFormat(FORMAT_TIME).format(date.getTime());
        }
    }

    public static Date stringToDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_TIME);
        Date timeDate = null;
        try {
            timeDate = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeDate;
    }

    public static Calendar stringToCalendar(String time) {
        if (TextUtils.isEmpty(time)) {
            Log.e("FormatUtils", "String转Calender失败，String为空");
            return Calendar.getInstance();
        }
        Date date = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(FORMAT_TIME);
            date = df.parse(time);
        } catch (ParseException e) {
            Log.e("FormatUtils", "String转Calender失败，String格式不是FORMAT_TIME");
            return Calendar.getInstance();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 将Date类型格式化成Calendar
     *
     * @param date 时间
     * @return
     */
    public static Calendar dataToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}
