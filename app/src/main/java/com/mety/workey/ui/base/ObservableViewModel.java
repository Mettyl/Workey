package com.mety.workey.ui.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.AndroidViewModel;


public class ObservableViewModel extends AndroidViewModel implements Observable {

    private PropertyChangeRegistry callbacks = new PropertyChangeRegistry();

    public ObservableViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void addOnPropertyChangedCallback(
            Observable.OnPropertyChangedCallback callback) {
        callbacks.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(
            Observable.OnPropertyChangedCallback callback) {
        callbacks.remove(callback);
    }

    public void notifyChange() {
        callbacks.notifyCallbacks(this, 0, null);
    }

    public void notifyPropertyChanged(int fieldId) {
        callbacks.notifyCallbacks(this, fieldId, null);
    }
}