package com.mety.workey.ui.homeFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mety.workey.R;
import com.mety.workey.data.database.AppDatabase;
import com.mety.workey.data.entity.Task;
import com.mety.workey.databinding.HomeFragmentBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private HomeFragmentBinding dataBinding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        dataBinding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);
        dataBinding.setFragment(this);
        initRecycler();

        return dataBinding.getRoot();
    }


    private void initRecycler() {
        RecyclerView recyclerView = dataBinding.homeFragmentRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void onAddClick(View view) {
        RecyclerView recyclerView = dataBinding.homeFragmentRecyclerView;
        AppDatabase database = AppDatabase.getAppDatabase(getContext());
        database.taskDao().insert(new Task(dataBinding.editText.getText().toString(), "", 1));
        recyclerView.setAdapter(new HomeRecyclerAdapter(database.taskDao().getAll()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }

}
