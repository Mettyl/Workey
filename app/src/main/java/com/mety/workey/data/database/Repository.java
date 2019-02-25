package com.mety.workey.data.database;

import android.content.Context;
import android.os.AsyncTask;

import com.mety.workey.data.dao.TaskDao;
import com.mety.workey.data.dao.TimeZoneDao;
import com.mety.workey.data.entity.Task;
import com.mety.workey.data.entity.TimeZone;

import java.util.List;

import androidx.lifecycle.LiveData;

public class Repository {

    private LiveData<List<Task>> tasks;
    private LiveData<List<TimeZone>> timeZones;
    private TaskDao taskDao;
    private TimeZoneDao timeZoneDao;

    public Repository(Context context) {
        AppDatabase database = AppDatabase.getAppDatabase(context);
        taskDao = database.taskDao();
        timeZoneDao = database.timeZoneDao();
        tasks = taskDao.getAll();
        timeZones = timeZoneDao.getAll();


    }

    ///////////// TASK \\\\\\\\\\\\\\

    public void insert(Task task) {
        new InsertTaskAsyncTask(taskDao).execute(task);
    }

    public void update(Task task) {
        new UpdateTaskAsyncTask(taskDao).execute(task);
    }

    public void delete(Task task) {
        new DeleteTaskAsyncTask(taskDao).execute(task);
    }

    public void deleteAllTasks() {
        new DeleteAllTasksAsyncTask(taskDao).execute();
    }

    public LiveData<List<Task>> getAllTasks() {
        return tasks;
    }

    private static class InsertTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private InsertTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insert(tasks[0]);
            return null;
        }
    }

    private static class UpdateTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private UpdateTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.update(tasks[0]);
            return null;
        }
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private DeleteTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.delete(tasks[0]);
            return null;
        }
    }

    private static class DeleteAllTasksAsyncTask extends AsyncTask<Void, Void, Void> {
        private TaskDao taskDao;

        private DeleteAllTasksAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.deleteAllTasks();
            return null;
        }
    }

    ///////////// TIMEZONE \\\\\\\\\\\\\\

    public void insert(TimeZone[] timeZone) {
        new InsertTimeZoneAsyncTask(timeZoneDao).execute(timeZone);
    }

    public void update(TimeZone timeZone) {
        new UpdateTimeZoneAsyncTask(timeZoneDao).execute(timeZone);
    }

    public void delete(TimeZone timeZone) {
        new DeleteTimeZoneAsyncTask(timeZoneDao).execute(timeZone);
    }

    public void deleteAllTimeZones() {
        new DeleteAllTimeZonesAsyncTask(timeZoneDao).execute();
    }

    public LiveData<List<TimeZone>> getAllTimeZones() {
        return timeZones;
    }


    private static class InsertTimeZoneAsyncTask extends AsyncTask<TimeZone, Void, Void> {
        private TimeZoneDao timeZoneDao;

        private InsertTimeZoneAsyncTask(TimeZoneDao timeZoneDao) {
            this.timeZoneDao = timeZoneDao;
        }

        @Override
        protected Void doInBackground(TimeZone... timeZones) {
            timeZoneDao.insert(timeZones);
            return null;
        }
    }


    private static class UpdateTimeZoneAsyncTask extends AsyncTask<TimeZone, Void, Void> {
        private TimeZoneDao timeZoneDao;

        private UpdateTimeZoneAsyncTask(TimeZoneDao timeZoneDao) {
            this.timeZoneDao = timeZoneDao;
        }

        @Override
        protected Void doInBackground(TimeZone... timeZones) {
            timeZoneDao.update(timeZones[0]);
            return null;
        }
    }

    private static class DeleteTimeZoneAsyncTask extends AsyncTask<TimeZone, Void, Void> {
        private TimeZoneDao timeZoneDao;

        private DeleteTimeZoneAsyncTask(TimeZoneDao timeZoneDao) {
            this.timeZoneDao = timeZoneDao;
        }

        @Override
        protected Void doInBackground(TimeZone... timeZones) {
            timeZoneDao.delete(timeZones[0]);
            return null;
        }
    }

    private static class DeleteAllTimeZonesAsyncTask extends AsyncTask<Void, Void, Void> {
        private TimeZoneDao timeZoneDao;

        private DeleteAllTimeZonesAsyncTask(TimeZoneDao timeZoneDao) {
            this.timeZoneDao = timeZoneDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            timeZoneDao.deleteAllTimeZones();
            return null;
        }
    }

    public TaskDao getTaskDao() {
        return taskDao;
    }

    public TimeZoneDao getTimeZoneDao() {
        return timeZoneDao;
    }
}
