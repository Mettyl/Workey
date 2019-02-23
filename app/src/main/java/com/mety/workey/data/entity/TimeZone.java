package com.mety.workey.data.entity;

import com.mety.workey.BR;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TimeZone extends BaseObservable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int day;
    private Date zoneStart;
    private Date zoneEnd;

    public TimeZone(int day, Date zoneStart, Date zoneEnd) {
        this.day = day;
        this.zoneStart = zoneStart;
        this.zoneEnd = zoneEnd;
    }

    @Bindable
    public Date getZoneStart() {
        return zoneStart;
    }

    @Bindable
    public Date getZoneEnd() {
        return zoneEnd;
    }

    @Bindable
    public int getDay() {
        return day;
    }

    public void setZoneStart(Date zoneStart) {
        this.zoneStart = zoneStart;
        notifyPropertyChanged(BR.zoneStart);
    }


    public void setZoneEnd(Date zoneEnd) {
        this.zoneEnd = zoneEnd;
        notifyPropertyChanged(BR.zoneEnd);
    }

    public void setDay(int day) {
        this.day = day;
        notifyPropertyChanged(BR.day);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @NonNull
    @Override
    public String toString() {
        return "Day number: " + day;
    }
}
