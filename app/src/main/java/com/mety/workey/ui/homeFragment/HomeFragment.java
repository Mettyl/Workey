package com.mety.workey.ui.homeFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.mety.workey.R;
import com.mety.workey.data.entity.Task;
import com.mety.workey.databinding.HomeFragmentBinding;
import com.mety.workey.ui.base.ListItem;
import com.mety.workey.ui.base.MainActivity;
import com.mety.workey.ui.viewModels.TaskViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {


    private HomeFragmentBinding dataBinding;
    private TaskViewModel viewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //Setting up view model
        viewModel = ViewModelProviders.of(getActivity()).get(TaskViewModel.class);

        //Setting up data binding
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);
        dataBinding.setLifecycleOwner(getViewLifecycleOwner());
        dataBinding.setSize(viewModel.getTaskListSize());

        //Setting up recyclerView adapter with methods to differ tasks from each other
        final HomeRecyclerAdapter adapter = new HomeRecyclerAdapter(getContext(), new HomeRecyclerAdapter.RecyclerItemListener() {
            @Override
            public void onCheckedChanged(Task task) {
                viewModel.update(task);
            }

            @Override
            public void onDeleteItem(Task task) {
                viewModel.delete(task);
                showUndoSnackbar();
            }
        });


        //initialization of recyclerView
        initRecycler(adapter);

        //Setting up observer for tasks live data
        viewModel.getAllTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                dataBinding.setSize(tasks.size());
                if (tasks.size() != 0) {

                    List<ListItem> list = new ArrayList<>();

                    Task previousTask = tasks.get(0);

                    Calendar previousTaskCal = Calendar.getInstance();
                    Calendar taskCal = Calendar.getInstance();

                    for (Task task : tasks) {

                        if (previousTask != null) {
                            if (previousTask == task) {
                                list.add(new DateHeader(task.getStart()));
                                list.add(task);
                            } else {
                                previousTaskCal.setTime(previousTask.getStart());
                                taskCal.setTime(task.getStart());
                                if (previousTaskCal.get(Calendar.DATE) != taskCal.get(Calendar.DATE)) {
                                    list.add(new DateHeader(task.getStart()));
                                    list.add(task);
                                } else {
                                    list.add(task);
                                }
                            }
                        }
                    }
                    adapter.submitList(list);
                }
            }
        });

        return dataBinding.getRoot();
    }

    private void initRecycler(RecyclerView.Adapter adapter) {
        RecyclerView recyclerView = dataBinding.homeFragmentRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback((HomeRecyclerAdapter) adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }

    private void showUndoSnackbar() {
        Snackbar snackbar = Snackbar.make(((MainActivity) getActivity()).getCoordinatorLayout(), getString(R.string.task_removed), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.insert(((HomeRecyclerAdapter) dataBinding.homeFragmentRecyclerView.getAdapter()).getRecentlyDeletedItem());
                    }
                });

        snackbar.setAnchorView(R.id.floatingActionButton).show();
    }
}
