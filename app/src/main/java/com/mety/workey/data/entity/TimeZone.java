package com.mety.workey.data.entity;

import com.mety.workey.BR;
import com.mety.workey.ui.base.ListItem;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class TimeZone extends ListItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int day;
    private int zoneStart;
    private int zoneEnd;

    public TimeZone(int day, int zoneStart, int zoneEnd) {
        this.day = day;
        this.zoneStart = zoneStart;
        this.zoneEnd = zoneEnd;
    }

    @Ignore
    public TimeZone(int day) {
        this.day = day;
//        this.zoneStart = 0;
//        this.zoneEnd = 24 * 60 * 60 * 1000;

        this.zoneStart = 15 * 60 * 60 * 1000;
        this.zoneEnd = 18 * 60 * 60 * 1000;
    }

    @Bindable
    public int getZoneStart() {
        return zoneStart;
    }

    @Bindable
    public int getZoneEnd() {
        return zoneEnd;
    }

    @Bindable
    public int getDay() {
        return day;
    }

    public void setZoneStart(int zoneStart) {
        this.zoneStart = zoneStart;
        notifyPropertyChanged(BR.zoneStart);
    }


    public void setZoneEnd(int zoneEnd) {
        this.zoneEnd = zoneEnd == 0 ? 24 * 60 * 60 * 1000 : zoneEnd;
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


    public int getZoneDuration(int duration) {
        if (zoneEnd - zoneStart - duration >= 0) {
            return zoneStart;
        }
        return -1;
    }

    public int getZoneTimeUntil(int deadline, int duration) {

        if (deadline <= zoneStart) {
            return -1;
        } else if (deadline <= zoneEnd) {
            if (deadline - zoneStart - duration >= 0) {
                return zoneStart;
            }
        } else {
            if (zoneEnd - zoneStart - duration >= 0) {
                return zoneStart;
            }
        }
        return -1;
    }

    public int getZoneTimeFrom(int now, int duration) {

        if (now <= zoneStart) {
            if (zoneEnd - zoneStart - duration >= 0) {
                return zoneStart;
            }
        } else if (now <= zoneEnd) {
            if (zoneEnd - now - duration >= 0) {
                return now;
            }
        }
        return -1;
    }

    public int getZoneTimeBetween(int now, int deadline, int duration) {

        if (now <= zoneStart && deadline <= zoneStart) {
            return -1;
        } else if (now <= zoneStart && deadline <= zoneEnd) {
            if (deadline - zoneStart - duration >= 0) {
                return zoneStart;
            }
        } else if (now <= zoneStart) {
            if (zoneEnd - zoneStart - duration >= 0) {
                return zoneStart;
            }
        } else if (now <= zoneEnd && deadline <= zoneEnd) {
            if (deadline - now - duration >= 0) {
                return now;
            }
        } else if (now <= zoneEnd) {
            if (zoneEnd - now - duration >= 0) {
                return now;
            }
        } else {
            return -1;
        }
        return -1;
    }


    @NonNull
    @Override
    public String toString() {
        return "Day number: " + day;
    }

    @Override
    public boolean isHeader() {
        return false;
    }
}
