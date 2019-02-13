package com.mety.workey.ui.newTaskFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mety.workey.R;
import com.mety.workey.data.entity.Task;
import com.mety.workey.databinding.NewTaskFragmentBinding;
import com.mety.workey.ui.viewModels.TaskViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

public class NewTaskFragment extends Fragment {

    private NewTaskFragmentBinding dataBinding;
    private TaskViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Setting up view model
        viewModel = ViewModelProviders.of(getActivity()).get(TaskViewModel.class);

        //Setting up data binding
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.new_task_fragment, container, false);
        dataBinding.setViewmodel(viewModel);
        dataBinding.setFragment(this);
        dataBinding.setLifecycleOwner(getViewLifecycleOwner());

        return dataBinding.getRoot();
    }

    public void onAddTaskButtonClick(View view, Task task) {
        viewModel.insert(task);
        Navigation.findNavController(view).navigateUp();
    }
}
