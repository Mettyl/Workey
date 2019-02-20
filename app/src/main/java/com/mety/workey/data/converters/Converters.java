package com.mety.workey.data.converters;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.room.TypeConverter;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }


    public static String timeToString(Date date) {

        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.HOUR_OF_DAY) + "h " + calendar.get(Calendar.MINUTE) + "m";
        } else {
            return null;
        }
    }

    public static String dateToString(Date date) {

        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            return dateFormat.format(date);
        } else {
            return null;
        }
    }
}
