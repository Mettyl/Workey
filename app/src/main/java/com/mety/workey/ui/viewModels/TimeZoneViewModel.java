package com.mety.workey.ui.viewModels;

import android.app.Application;

import com.mety.workey.data.database.Repository;
import com.mety.workey.data.entity.TimeZone;
import com.mety.workey.ui.base.Logger;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TimeZoneViewModel extends AndroidViewModel {

    private Repository repository;
    private LiveData<List<TimeZone>> timeZones;

    public TimeZoneViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        timeZones = repository.getAllTimeZones();
    }

    // Main database access

    public void insert(TimeZone... timeZone) {
        repository.insert(timeZone);
    }

    public void update(TimeZone timeZone) {
        repository.update(timeZone);
    }

    public void delete(TimeZone timeZone) {
        repository.delete(timeZone);
    }

    public void deleteAllTimeZones() {
        repository.deleteAllTimeZones();
    }

    public LiveData<List<TimeZone>> getAllTimeZones() {
        return timeZones;
    }

    public int getTimeZoneListSize() {
        return timeZones.getValue() == null ? 0 : timeZones.getValue().size();
    }

    public void logTimeZones() {
        StringBuilder builder = new StringBuilder();
        if (getAllTimeZones().getValue() != null) {
            for (TimeZone timeZone : getAllTimeZones().getValue()) {
                builder.append(timeZone.toString());
            }
            Logger.i(builder.toString());
        } else {
            Logger.i("LiveData are empty");
        }
    }


}
