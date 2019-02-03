package com.mety.workey.ui.homeFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mety.workey.R;
import com.mety.workey.data.entity.Task;
import com.mety.workey.databinding.HomeFragmentBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private HomeFragmentBinding dataBinding;

    private HomeViewModel viewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //Setting up view model
        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        //Setting up data binding
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);
        dataBinding.setViewmodel(viewModel);
        dataBinding.setLifecycleOwner(getViewLifecycleOwner());

        //Setting up recyclerView adapter with methods to differ tasks from each other
        final HomeRecyclerAdapter adapter = new HomeRecyclerAdapter();

        //initialization of recyclerView
        initRecycler(adapter);

        //Setting up observer for tasks live data
        viewModel.getAllTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                adapter.submitList(tasks);
            }
        });

        return dataBinding.getRoot();
    }


    private void initRecycler(RecyclerView.Adapter adapter) {
        RecyclerView recyclerView = dataBinding.homeFragmentRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }



}
