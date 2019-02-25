package com.mety.workey.data.entity;

import com.mety.workey.BR;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class TimeZone extends BaseObservable {

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
        this.zoneStart = 0;
        this.zoneEnd = 24 * 60 * 60 * 1000;
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

    public int getZoneDuration() {

        if (zoneStart == 0 && zoneEnd == 0) {
            return 24 * 60 * 60 * 1000;
        } else if (zoneStart == 0) {
            return zoneEnd;
        } else if (zoneEnd == 0) {
            return 24 * 60 * 60 * 1000 - zoneStart;
        } else {
            return zoneEnd - zoneStart;
        }
    }

    public int getZoneTimeUntil(int deadline) {

        if (deadline == 0) {
            deadline = 24 * 60 * 60 * 1000;
        }

        if (zoneStart == 0 && zoneEnd == 0) {
            return deadline;
        } else if ((deadline > zoneStart && deadline < zoneEnd) || (deadline > zoneStart && deadline == 0)) {
            return deadline - zoneStart;
        } else if (deadline >= zoneEnd) {
            return zoneEnd - zoneStart;
        } else if (deadline < zoneStart) {
            return 0;
        } else {
            return 0;
        }
    }

    public int getZoneTimeFrom(int now) {
        if (zoneStart == 0 && zoneEnd == 0) {
            return 24 * 60 * 60 * 1000 - now;
        } else if (zoneStart == 0) {
            if (now >= zoneEnd) {
                return 0;
            } else {
                return zoneEnd - now;
            }
        } else if (zoneEnd == 0) {
            if (now < zoneStart) {
                return 24 * 60 * 60 * 1000 - zoneStart;
            } else {
                return 24 * 60 * 60 * 1000 - now;
            }
        } else {
            if (now <= zoneStart) {
                return zoneEnd - zoneStart;
            } else if (now <= zoneEnd) {
                return zoneEnd - now;
            } else {
                return 0;
            }
        }
    }


    public int getZoneTimeBetween(int now, int deadline) {

        if (deadline == 0) {
            deadline = 24 * 60 * 60 * 1000;
        }

        if (zoneStart == 0 && zoneEnd == 0) {
            //if zones not set
            return deadline - now;
        } else if (zoneStart == 0 && now < zoneEnd) {
            //if now in zone interval
            if (deadline <= zoneEnd) {
                return deadline - now;
            } else {
                return zoneEnd - now;
            }
        } else if (zoneEnd == 0) {
            if (now < zoneStart && deadline < zoneStart) {
                return 0;
            } else if (now < zoneStart && deadline > zoneStart) {
                return deadline - zoneStart;
            } else if (now > zoneStart && deadline > zoneStart) {
                return deadline - now;
            }
        } else {
            if (now < zoneStart && deadline < zoneStart) {
                return 0;
            } else if (now < zoneStart && deadline > zoneStart && deadline < zoneEnd) {
                return deadline - zoneStart;
            } else if (now < zoneStart && deadline > zoneEnd) {
                return zoneEnd - zoneStart;
            } else if (now > zoneStart && now < zoneEnd && deadline < zoneEnd) {
                return deadline - now;
            } else if (now > zoneStart && now < zoneEnd && deadline > zoneEnd) {
                return zoneEnd - now;
            } else if (now > zoneEnd) {
                return 0;
            }

        }
        return -1;
    }

    public void setZoneStart(int zoneStart) {
        this.zoneStart = zoneStart;
        notifyPropertyChanged(BR.zoneStart);
    }


    public void setZoneEnd(int zoneEnd) {
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
