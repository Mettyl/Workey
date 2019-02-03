package com.mety.workey.data.database;

import android.content.Context;

import com.mety.workey.data.dao.TaskDao;
import com.mety.workey.data.entity.Task;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Task.class}, version = 1)
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
}
