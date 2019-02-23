package com.mety.workey.ui.timeZoneFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mety.workey.R;
import com.mety.workey.data.entity.TimeZone;
import com.mety.workey.databinding.TimeZoneFragmentBinding;
import com.mety.workey.ui.viewModels.TimeZoneViewModel;

import java.util.Calendar;
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


    public TimeZoneFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final TimeZoneViewModel viewModel = ViewModelProviders.of(getActivity()).get(TimeZoneViewModel.class);

        binding = DataBindingUtil.inflate(inflater, R.layout.time_zone_fragment, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        final TimeZoneRecyclerAdapter adapter = new TimeZoneRecyclerAdapter();

        //populate database just once
        if (viewModel.getTimeZoneListSize() == 0) {
            TimeZone[] timeZones = {new TimeZone(Calendar.MONDAY, null, null),
                    new TimeZone(Calendar.TUESDAY, null, null),
                    new TimeZone(Calendar.WEDNESDAY, null, null),
                    new TimeZone(Calendar.THURSDAY, null, null),
                    new TimeZone(Calendar.FRIDAY, null, null),
                    new TimeZone(Calendar.SATURDAY, null, null),
                    new TimeZone(Calendar.SUNDAY, null, null)};
            viewModel.insert(timeZones);
        }

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

}
