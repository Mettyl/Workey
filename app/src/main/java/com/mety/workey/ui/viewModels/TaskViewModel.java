package com.mety.workey.ui.viewModels;

import android.app.Application;

import com.mety.workey.data.database.Repository;
import com.mety.workey.data.entity.Task;
import com.mety.workey.ui.base.Logger;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class TaskViewModel extends ObservableViewModel {

    private Repository repository;
    private LiveData<List<Task>> tasks;


    private Task currentlyCreatedTask;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        tasks = repository.getAllTasks();
        currentlyCreatedTask = new Task();
    }

    // Main database access

    public void insert(Task task) {
        repository.insert(task);
    }

    public void update(Task task) {
        repository.update(task);
    }

    public void delete(Task task) {
        repository.delete(task);
    }

    public void deleteAllTasks() {
        repository.deleteAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return tasks;
    }

    public int getTaskListSize() {
        return tasks.getValue() == null ? 0 : tasks.getValue().size();
    }

    //
    public Task getCurrentlyCreatedTask() {
        return currentlyCreatedTask;
    }

    public void setCurrentlyCreatedTask(Task currentlyCreatedTask) {
        this.currentlyCreatedTask = currentlyCreatedTask;
    }

    public void logTasks() {
        StringBuilder builder = new StringBuilder();
        if (getAllTasks().getValue() != null) {
            for (Task task : getAllTasks().getValue()) {
                builder.append(task.toString());
            }
            Logger.i(builder.toString());
        } else {
            Logger.i("LiveData are empty");
        }
    }

    @Override
    protected void onCleared() {
        Logger.i("TaskViewModel: cleared");
        super.onCleared();
    }
}
