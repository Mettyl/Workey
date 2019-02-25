package com.mety.workey.data.database;

import android.content.Context;

import com.mety.workey.data.converters.Converters;
import com.mety.workey.data.dao.TaskDao;
import com.mety.workey.data.dao.TimeZoneDao;
import com.mety.workey.data.entity.Task;
import com.mety.workey.data.entity.TimeZone;

import java.util.Calendar;
import java.util.concurrent.Executors;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {Task.class, TimeZone.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static AppDatabase getAppDatabase(final Context context) {

        if (INSTANCE == null) {
            INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class)
                    .addCallback(new RoomDatabase.Callback() {
                        public void onCreate(SupportSQLiteDatabase db) {
                            Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    //populate database just once
                                    TimeZone[] timeZones = {new TimeZone(Calendar.MONDAY),
                                            new TimeZone(Calendar.TUESDAY),
                                            new TimeZone(Calendar.WEDNESDAY),
                                            new TimeZone(Calendar.THURSDAY),
                                            new TimeZone(Calendar.FRIDAY),
                                            new TimeZone(Calendar.SATURDAY),
                                            new TimeZone(Calendar.SUNDAY)};
                                    getAppDatabase(context).timeZoneDao().insert(timeZones);
                                }
                            });
                        }
                    }).build();
        }
        return INSTANCE;
    }


    public abstract TaskDao taskDao();

    public abstract TimeZoneDao timeZoneDao();
}
