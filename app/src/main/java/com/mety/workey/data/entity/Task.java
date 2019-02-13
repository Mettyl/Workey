package com.mety.workey.data.entity;


import com.mety.workey.BR;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Task extends BaseObservable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "priority")
    private int priority;

    @ColumnInfo(name = "finished")
    private boolean finished;


    public Task(String name, String description, int priority, boolean finished) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.finished = finished;
    }

    @Ignore
    public Task() {
        this.name = "";
        this.description = "";
        this.priority = 0;
        this.finished = false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Bindable
    public String getName() {
        return name;
    }

    @Bindable
    public int getPriority() {
        return priority;
    }

    @Bindable
    public boolean getFinished() {
        return finished;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(int priority) {
        this.priority = priority;
        notifyPropertyChanged(BR.priority);
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
        notifyPropertyChanged(BR.finished);
    }

    @NonNull
    @Override
    public String toString() {
        return "Task {" + Integer.toString(id) + "; " + name + "; " + Integer.toString(priority) + "; " + finished + "} ";
    }
}