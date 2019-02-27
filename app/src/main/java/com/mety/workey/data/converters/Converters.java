package com.mety.workey.data.converters;

import android.content.Context;

import com.mety.workey.R;
import com.mety.workey.data.entity.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.room.TypeConverter;

public class Converters {

    public static String DAY_MONTH_HOUR_MINUTE = "dd. MM. HH:mm";
    public static String DAY_MONTH_YEAR_HOUR_MINUTE = "dd.MM.yyyy HH:mm";
    public static String HOUR_MINUTE = "HH:mm";
    public static String DAY_MONTH = "dd. MM.";

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
            if (calendar.get(Calendar.HOUR_OF_DAY) == 0) {
                return calendar.get(Calendar.MINUTE) + " min";
            }
            return calendar.get(Calendar.HOUR_OF_DAY) + " h " + calendar.get(Calendar.MINUTE) + " min";
        } else {
            return null;
        }
    }

    public static String timeToString(int milliseconds) {

        if (milliseconds == 0 || milliseconds == 24 * 60 * 60 * 1000) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Converters.milliToHour(milliseconds));
        calendar.set(Calendar.MINUTE, Converters.milliToMinute(milliseconds));

        SimpleDateFormat dateFormat = new SimpleDateFormat(HOUR_MINUTE);
        return dateFormat.format(calendar.getTime());

    }

    public static int milliToHour(int milliseconds) {
        return milliseconds / (60 * 60 * 1000);
    }

    public static int milliToMinute(int milliseconds) {
        return (milliseconds / (60 * 1000) % 60);
    }


    public static String dateToString(Date date, String format) {

        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(date);
        } else {
            return null;
        }
    }

    public static String startPlusDuration(Task task, String format) {

        if (task != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(new Date(task.getStart().getTime() + task.getDuration()));
        } else {
            return null;
        }
    }

    public static String dateToWeekDay(Date date, Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return getDayOfWeek(context, calendar.get(Calendar.DAY_OF_WEEK));
    }

    public static String getDayOfWeek(Context context, int value) {
        String day = "";
        switch (value) {
            case 1:
                day = context.getString(R.string.sunday);
                break;
            case 2:
                day = context.getString(R.string.monday);
                break;
            case 3:
                day = context.getString(R.string.tuesday);
                break;
            case 4:
                day = context.getString(R.string.wednesday);
                break;
            case 5:
                day = context.getString(R.string.thursday);
                break;
            case 6:
                day = context.getString(R.string.friday);
                break;
            case 7:
                day = context.getString(R.string.saturday);
                break;
        }
        return day;
    }
}
