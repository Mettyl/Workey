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
import android.widget.Toast;

import com.mety.workey.R;
import com.mety.workey.data.entity.Task;
import com.mety.workey.databinding.NewTaskFragmentBinding;
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

            //Setting up data binding
            dataBinding = DataBindingUtil.inflate(inflater, R.layout.new_task_fragment, container, false);
            dataBinding.setTask(viewModel.getCurrentlyCreatedTask());
            dataBinding.setFragment(this);
            dataBinding.setLifecycleOwner(getViewLifecycleOwner());

            //Ensures that previously created task is cleared
            if (savedInstanceState == null) {
                resetAllViews();
            }
        }

        return dataBinding.getRoot();
    }


    public boolean finishCreatingTask() {

        boolean correctlyFilled = true;
        Task task = viewModel.getCurrentlyCreatedTask();

        if (task.getName().isEmpty()) {
            dataBinding.newTaskNameTil.setError(getString(R.string.enter_task_name));
            correctlyFilled = false;
        }
        Date currentDate = new Date();
        if (task.getDeadline() != null && currentDate.getTime() >= task.getDeadline().getTime()) {
            correctlyFilled = false;
            Toast.makeText(getContext(), R.string.incorrect_date, Toast.LENGTH_SHORT).show();
        }
        if (correctlyFilled) {
            viewModel.insert(viewModel.getCurrentlyCreatedTask());
            return true;
        } else {
            return false;
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

                durationCal.set(0, 0, 0, hourOfDay, minute);
                viewModel.getCurrentlyCreatedTask().setDuration(durationCal.getTime());

            }
        }, durationCal.get(Calendar.HOUR_OF_DAY), durationCal.get(Calendar.MINUTE), true).show();
    }

    private void showDeadlineTimePicker(View view) {
        final Calendar calendar = Calendar.getInstance();
        Date duration = viewModel.getCurrentlyCreatedTask().getDeadline();
        if (duration != null) {
            calendar.setTime(duration);
        }
        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
                viewModel.getCurrentlyCreatedTask().setDeadline(calendar.getTime());
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();

    }

    public void showDatePicker(final View view) {

        final Calendar calendar = Calendar.getInstance();
        Date duration = viewModel.getCurrentlyCreatedTask().getDeadline();
        if (duration != null) {
            calendar.setTime(duration);
        }

        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                calendar.set(year, month, dayOfMonth);
                viewModel.getCurrentlyCreatedTask().setDeadline(calendar.getTime());
                showDeadlineTimePicker(view);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void resetAllViews() {
        viewModel.setCurrentlyCreatedTask(new Task());
        dataBinding.setTask(viewModel.getCurrentlyCreatedTask());
    }

}
