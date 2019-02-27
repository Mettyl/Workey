package com.mety.workey.data.entity;


import com.mety.workey.BR;
import com.mety.workey.data.converters.Converters;
import com.mety.workey.ui.base.ListItem;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Task extends ListItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String description;
    private int priority;
    private boolean finished;
    private Date start;
    private int duration;
    private Date deadline;

    public Task(String name, String description, int priority, boolean finished, int duration, Date deadline) {
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
        this.duration = 10 * 60 * 1000;
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

    @Bindable
    public int getDuration() {
        return duration;
    }

    @Bindable
    public Date getDeadline() {
        return deadline;
    }

    @Bindable
    public Date getStart() {
        return start;
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

    public void setDuration(int duration) {
        this.duration = duration;
        notifyPropertyChanged(BR.duration);
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
        notifyPropertyChanged(BR.deadline);
    }

    public void setStart(Date start) {
        this.start = start;
        notifyPropertyChanged(BR.start);

    }

    @NonNull
    @Override
    public String toString() {
        return "Task {" + Integer.toString(id) + "; " + name + "; " + "Start: " + Converters.dateToString(start, Converters.DAY_MONTH_HOUR_MINUTE) + "; " + "Duration: "
                + Converters.timeToString(duration) + "; " + "Deadline: " + Converters.dateToString(deadline
                , Converters.DAY_MONTH_HOUR_MINUTE) + "; " + finished + "} ";
    }

    @Override
    public boolean isHeader() {
        return false;
    }
}