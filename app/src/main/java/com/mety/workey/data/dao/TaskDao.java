package com.mety.workey.data.dao;


import com.mety.workey.data.entity.Task;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM task WHERE finished = 0 ORDER BY start ASC")
    LiveData<List<Task>> getAll();

    @Query("SELECT * FROM task WHERE finished = 0 ORDER BY start DESC LIMIT 1")
    Task getLastTask();

    @Query("SELECT * FROM task WHERE finished = 0 AND deadline >= :deadline ORDER BY ABS( deadline - :deadline ) LIMIT 1")
    Task findTaskWithLaterDeadline(Date deadline);

    @Query("SELECT * FROM task WHERE finished = 0 AND start >= :start ORDER BY ABS( start - :start ) ASC")
    List<Task> findTaskWithLaterStart(Date start);

    @Query("SELECT * FROM task WHERE finished = 0 AND start BETWEEN :date1 AND :date2 ORDER BY start DESC LIMIT 1")
    Task findLastTaskInBetweenDates(Date date1, Date date2);

    @Insert
    void insert(Task... tasks);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM task")
    void deleteAllTasks();

}
