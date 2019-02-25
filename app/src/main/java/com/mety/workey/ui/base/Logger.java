package com.mety.workey.ui.base;

import android.util.Log;

public class Logger {

    private static final String TAG = "debug";

    public static void i(boolean message) {
        i(Boolean.toString(message));
    }

    public static void i(int message) {
        i(Integer.toString(message));
    }

    public static void i(long message) {
        i(Long.toString(message));
    }

    public static void i(String message) {
        Log.i(TAG, message);
    }


}
