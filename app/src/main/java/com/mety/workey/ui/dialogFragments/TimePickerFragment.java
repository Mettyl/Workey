package com.mety.workey.ui.dialogFragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {


    public TimePickerFragment() {

    }

    @NonNull
    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                true);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
    }
}