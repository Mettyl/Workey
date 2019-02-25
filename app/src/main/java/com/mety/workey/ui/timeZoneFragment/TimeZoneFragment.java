package com.mety.workey.ui.timeZoneFragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.google.android.material.snackbar.Snackbar;
import com.mety.workey.R;
import com.mety.workey.data.converters.Converters;
import com.mety.workey.data.entity.TimeZone;
import com.mety.workey.databinding.TimeZoneFragmentBinding;
import com.mety.workey.ui.base.Logger;
import com.mety.workey.ui.base.MainActivity;
import com.mety.workey.ui.viewModels.TimeZoneViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class TimeZoneFragment extends Fragment {

    TimeZoneFragmentBinding binding;
    TimeZoneViewModel viewModel;


    public TimeZoneFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = ViewModelProviders.of(getActivity()).get(TimeZoneViewModel.class);

        binding = DataBindingUtil.inflate(inflater, R.layout.time_zone_fragment, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        final TimeZoneRecyclerAdapter adapter = new TimeZoneRecyclerAdapter(this);


        //Setting up observer for timeZones live data
        viewModel.getAllTimeZones().observe(getViewLifecycleOwner(), new Observer<List<TimeZone>>() {
            @Override
            public void onChanged(List<TimeZone> timeZones) {
                adapter.submitList(timeZones);
            }
        });


        //initialization of recyclerView
        initRecycler(adapter);


        return binding.getRoot();
    }

    private void initRecycler(RecyclerView.Adapter adapter) {
        RecyclerView recyclerView = binding.timeZoneRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerView.setAdapter(adapter);
    }

    public void showFromTimePicker(View view, final TimeZone timeZone) {

        Calendar calendar = Calendar.getInstance();

        if (timeZone.getZoneStart() != 0) {
            calendar.set(Calendar.HOUR_OF_DAY, Converters.milliToHour(timeZone.getZoneStart()));
            calendar.set(Calendar.MINUTE, Converters.milliToMinute(timeZone.getZoneStart()));
        }

        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                int chosenTime = hourOfDay * 60 * 60 * 1000 + minute * 60 * 1000;

                if (timeZone.getZoneEnd() != 0 && chosenTime >= timeZone.getZoneEnd()) {
                    showErrorSnackbar();
                } else {
                    timeZone.setZoneStart(chosenTime);
                    viewModel.update(timeZone);
                }
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    public void showToTimePicker(View view, final TimeZone timeZone) {

        Calendar calendar = Calendar.getInstance();

        if (timeZone.getZoneEnd() != 0) {
            calendar.set(Calendar.HOUR_OF_DAY, Converters.milliToHour(timeZone.getZoneEnd()));
            calendar.set(Calendar.MINUTE, Converters.milliToMinute(timeZone.getZoneEnd()));
        }
        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                int chosenTime = hourOfDay * 60 * 60 * 1000 + minute * 60 * 1000;

                if (timeZone.getZoneStart() != 0 && timeZone.getZoneStart() >= chosenTime) {
                    showErrorSnackbar();
                } else {
                    timeZone.setZoneEnd(chosenTime);
                    viewModel.update(timeZone);
                }
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }


    public int compareTimes(Date d1, Date d2) {
        int t1;
        int t2;

        t1 = (int) (d1.getTime() % (24 * 60 * 60 * 1000L));
        t2 = (int) (d2.getTime() % (24 * 60 * 60 * 1000L));
        Logger.i(t1 + " - " + t2);
        return (t1 - t2);
    }

    private void showErrorSnackbar() {
        Snackbar snackbar = Snackbar.make(((MainActivity) getActivity()).getCoordinatorLayout(), getString(R.string.wrong_hour), Snackbar.LENGTH_LONG);
        snackbar.setAnchorView(R.id.bottom_app_bar).show();
    }

}
