package com.mety.workey.data.entity;


import com.mety.workey.BR;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Task extends BaseObservable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String description;
    private int priority;
    private boolean finished;
    private Date duration;
    private Date deadline;

    public Task(String name, String description, int priority, boolean finished, Date duration, Date deadline) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.finished = finished;
        this.duration = duration;
        this.deadline = deadline;
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

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    public void setPriority(int priority) {
        this.priority = priority;
        notifyPropertyChanged(BR.priority);
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
        notifyPropertyChanged(BR.finished);
    }

    @Bindable
    public Date getDuration() {
        return duration;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
        notifyPropertyChanged(BR.duration);
    }

    @Bindable
    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
        notifyPropertyChanged(BR.deadline);
    }

    @NonNull
    @Override
    public String toString() {
        return "Task {" + Integer.toString(id) + "; " + name + "; " + Integer.toString(priority) + "; " + finished + "} ";
    }
}