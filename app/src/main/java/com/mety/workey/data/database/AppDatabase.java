package com.mety.workey.data.database;

import android.content.Context;

import com.mety.workey.data.converters.Converters;
import com.mety.workey.data.dao.TaskDao;
import com.mety.workey.data.dao.TimeZoneDao;
import com.mety.workey.data.entity.Task;
import com.mety.workey.data.entity.TimeZone;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


@Database(entities = {Task.class, TimeZone.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static AppDatabase getAppDatabase(Context context) {

        if (INSTANCE == null) {
            INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class)
                    .build();
        }
        return INSTANCE;
    }

    public abstract TaskDao taskDao();

    public abstract TimeZoneDao timeZoneDao();
}
