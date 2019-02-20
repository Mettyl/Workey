package com.mety.workey.ui.newTaskFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.mety.workey.R;
import com.mety.workey.data.entity.Task;
import com.mety.workey.databinding.NewTaskFragmentBinding;
import com.mety.workey.ui.base.Logger;
import com.mety.workey.ui.viewModels.TaskViewModel;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class NewTaskFragment extends Fragment {

    private NewTaskFragmentBinding dataBinding;
    private TaskViewModel viewModel;

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getActivity() != null) {
            //Setting up view model
            viewModel = ViewModelProviders.of(getActivity()).get(TaskViewModel.class);

            if (savedInstanceState == null) {
                resetAllViews();
            }

            //Setting up data binding
            dataBinding = DataBindingUtil.inflate(inflater, R.layout.new_task_fragment, container, false);
            dataBinding.setTask(viewModel.getCurrentlyCreatedTask());
            dataBinding.setFragment(this);
            dataBinding.setLifecycleOwner(getViewLifecycleOwner());
        }


        return dataBinding.getRoot();
    }


    public boolean finishCreatingTask() {
        if (viewModel.getCurrentlyCreatedTask().getName().isEmpty()) {
            dataBinding.newTaskNameTil.setError("Enter task name!");
            return false;
        } else {
            viewModel.insert(viewModel.getCurrentlyCreatedTask());
            return true;
        }
    }

    public void showDurationTimePicker(View view) {

        final Calendar durationCal = Calendar.getInstance();

        Date duration = viewModel.getCurrentlyCreatedTask().getDuration();
        if (duration == null) {
            durationCal.set(0, 0, 0, 0, 10);
        } else {
            durationCal.setTime(duration);
        }

        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                dataBinding.newTaskSelectDurationTv.setTextColor(getResources().getColor(R.color.secondaryColor));
                dataBinding.newTaskDurationIv.setColorFilter(getResources().getColor(R.color.secondaryColor));

                durationCal.set(0, 0, 0, hourOfDay, minute);
                viewModel.getCurrentlyCreatedTask().setDuration(durationCal.getTime());

            }
        }, durationCal.get(Calendar.HOUR_OF_DAY), durationCal.get(Calendar.MINUTE), true).show();
    }

    public void showDeadlineTimePicker(View view) {
        final Calendar calendar = Calendar.getInstance();
        Date duration = viewModel.getCurrentlyCreatedTask().getDeadline();
        if (duration != null) {
            calendar.setTime(duration);
        }
        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                dataBinding.newTaskSelectDeadlineTv.setTextColor(getResources().getColor(R.color.secondaryColor));
                dataBinding.newTaskDeadlineIv.setColorFilter(getResources().getColor(R.color.secondaryColor));

                calendar.setTime(viewModel.getCurrentlyCreatedTask().getDeadline());
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
                viewModel.getCurrentlyCreatedTask().setDeadline(calendar.getTime());
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();

    }

    public void showDatePicker(final View view) {

        Calendar calendar = Calendar.getInstance();
        Date duration = viewModel.getCurrentlyCreatedTask().getDeadline();
        if (duration != null) {
            calendar.setTime(duration);
        }

        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                viewModel.getCurrentlyCreatedTask().setDeadline(calendar.getTime());
                showDeadlineTimePicker(view);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.i("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.i("onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Logger.i("onDetach");

    }

    public void resetAllViews() {
        Task task = viewModel.getCurrentlyCreatedTask();
        task.setName("");
        task.setDescription("");
        task.setDeadline(null);
        task.setDuration(null);
        task.setPriority(0);
        task.setFinished(false);
    }

}
