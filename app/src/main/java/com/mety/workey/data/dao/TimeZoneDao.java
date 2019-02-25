package com.mety.workey.data.dao;

import com.mety.workey.data.entity.TimeZone;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TimeZoneDao {

    @Query("SELECT * FROM timezone")
    LiveData<List<TimeZone>> getAll();

    @Query("SELECT * FROM timezone ORDER BY day ASC")
    List<TimeZone> getList();

    @Insert
    void insert(TimeZone... timeZones);

    @Update
    void update(TimeZone timeZone);

    @Delete
    void delete(TimeZone timeZone);

    @Query("DELETE FROM timezone")
    void deleteAllTimeZones();


}
