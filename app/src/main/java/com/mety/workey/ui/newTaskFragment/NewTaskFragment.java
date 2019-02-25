package com.mety.workey.ui.newTaskFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mety.workey.R;
import com.mety.workey.data.converters.Converters;
import com.mety.workey.data.entity.Task;
import com.mety.workey.data.entity.TimeZone;
import com.mety.workey.databinding.NewTaskFragmentBinding;
import com.mety.workey.ui.base.Logger;
import com.mety.workey.ui.viewModels.TaskViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class NewTaskFragment extends Fragment {

    private NewTaskFragmentBinding dataBinding;
    private TaskViewModel viewModel;

    private Date chosenDate;

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

    //Runs new thread to count at which time should be the task completed
    public void findTimeForTask(Task task) {

        new FindTimeForTasks().execute(task);
    }

    private int daysBetween(Date start, Date end) {
        return (int) Math.round((start.getTime() - end.getTime()) / (double) 86400000);
    }

    private class FindTimeForTasks extends AsyncTask<Task, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Task... tasks) {
            Task task = tasks[0];

            return isEnoughTime(task);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            viewModel.setTimeForTask(aBoolean);
        }

        private boolean isEnoughTime(Task task) {
            if (viewModel.getTaskListSize() == 0) {

                int timeBetween = 0;

                Calendar currentTimeCal = Calendar.getInstance();
                Calendar iterationTimeCal = Calendar.getInstance();
                Calendar deadlineCal = Calendar.getInstance();
                deadlineCal.setTime(task.getDeadline());

                Logger.i(Converters.dateToString(iterationTimeCal.getTime(), Converters.DAY_MONTH_HOUR_MINUTE));
                Logger.i(Converters.dateToString(deadlineCal.getTime(), Converters.DAY_MONTH_HOUR_MINUTE));

                List<TimeZone> timeZones = viewModel.getRepository().getTimeZoneDao().getList();

                TimeZone lastTime;

                while (iterationTimeCal.getTimeInMillis() <= deadlineCal.getTimeInMillis()) {

                    //obtaining current time zone
                    TimeZone zone = timeZones.get(iterationTimeCal.get(Calendar.DAY_OF_WEEK) - 1);


                    int numberOfPos;
                    if (currentTimeCal.get(Calendar.DATE) == iterationTimeCal.get(Calendar.DATE) && currentTimeCal.get(Calendar.DATE) != deadlineCal.get(Calendar.DATE)) {

                        numberOfPos = zone.getZoneTimeFrom(
                                (currentTimeCal.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000)
                                        + (currentTimeCal.get(Calendar.MINUTE) * 60 * 1000))
                                / task.getDuration();
                    } else if (currentTimeCal.get(Calendar.DATE) == iterationTimeCal.get(Calendar.DATE) && currentTimeCal.get(Calendar.DATE) == deadlineCal.get(Calendar.DATE)) {

                        numberOfPos = zone.getZoneTimeBetween(
                                (currentTimeCal.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000)
                                        + (currentTimeCal.get(Calendar.MINUTE) * 60 * 1000),
                                (deadlineCal.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000)
                                        + (deadlineCal.get(Calendar.MINUTE) * 60 * 1000))
                                / task.getDuration();

                    } else if (iterationTimeCal.get(Calendar.DATE) == deadlineCal.get(Calendar.DATE)) {

                        numberOfPos = zone.getZoneTimeUntil(
                                (deadlineCal.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000)
                                        + (deadlineCal.get(Calendar.MINUTE) * 60 * 1000))
                                / task.getDuration();
                    } else {

                        numberOfPos = zone.getZoneDuration() / task.getDuration();
                    }

                    if (numberOfPos >= 1) {
                        timeBetween += task.getDuration() * numberOfPos;
                        Logger.i(+Converters.milliToHour(task.getDuration() * numberOfPos) + " Hours " + Converters.milliToMinute(task.getDuration() * numberOfPos) + " Minutes " + " " + Converters.getDayOfWeek(getContext(), iterationTimeCal.get(Calendar.DAY_OF_WEEK)));
                        return true;

                    }
                    iterationTimeCal.add(Calendar.DATE, 1);
                }
                Logger.i("All Hours " + Converters.milliToHour(timeBetween) + " Minutes " + Converters.milliToMinute(timeBetween));
            }
            return false;
        }
    }


    public boolean finishCreatingTask() {

        boolean correctlyFilled = true;
        Task task = viewModel.getCurrentlyCreatedTask();

        if (task.getName().isEmpty()) {
            dataBinding.newTaskNameTil.setError(getString(R.string.enter_task_name));
            correctlyFilled = false;
        }

        if (correctlyFilled) {
            viewModel.insert(task);
            return true;
        } else {
            return false;
        }
    }

    public void showDurationTimePicker(View view) {

        final Calendar durationCal = Calendar.getInstance();

        int duration = viewModel.getCurrentlyCreatedTask().getDuration();
        durationCal.set(Calendar.HOUR_OF_DAY, Converters.milliToHour(duration));
        durationCal.set(Calendar.MINUTE, Converters.milliToMinute(duration));


        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                int chosenTime = hourOfDay * 60 * 60 * 1000 + minute * 60 * 1000;
                if (chosenTime == 0) {
                    Toast.makeText(getContext(), getString(R.string.insert_duration_and_deadline), Toast.LENGTH_SHORT).show();
                } else {
                    viewModel.getCurrentlyCreatedTask().setDuration(chosenTime);
                }

            }
        }, durationCal.get(Calendar.HOUR_OF_DAY), durationCal.get(Calendar.MINUTE), true).show();
    }

    private void showDeadlineTimePicker(View view) {

        final Calendar calendar = Calendar.getInstance();
        Calendar chosenCal = Calendar.getInstance();
        chosenCal.setTime(chosenDate);

        Date deadline = viewModel.getCurrentlyCreatedTask().getDeadline();
        if (deadline != null) {
            calendar.setTime(deadline);
        }

        calendar.set(chosenCal.get(Calendar.YEAR), chosenCal.get(Calendar.MONTH), chosenCal.get(Calendar.DAY_OF_MONTH));

        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
                Date currentDate = new Date();
                if (currentDate.getTime() + viewModel.getCurrentlyCreatedTask().getDuration() >= calendar.getTimeInMillis()) {
                    Toast.makeText(getContext(), R.string.incorrect_date, Toast.LENGTH_SHORT).show();
                } else {
                    viewModel.getCurrentlyCreatedTask().setDeadline(calendar.getTime());

                    findTimeForTask(viewModel.getCurrentlyCreatedTask());
                }
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();

    }

    public void showDeadLineDatePicker(final View view) {

        final Calendar calendar = Calendar.getInstance();
        Date deadline = viewModel.getCurrentlyCreatedTask().getDeadline();
        if (deadline != null) {
            calendar.setTime(deadline);
        }

        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                calendar.set(year, month, dayOfMonth);
                chosenDate = new Date(calendar.getTimeInMillis());
                showDeadlineTimePicker(view);

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void resetAllViews() {
        viewModel.setCurrentlyCreatedTask(new Task());
        dataBinding.setTask(viewModel.getCurrentlyCreatedTask());
    }

    public TaskViewModel getViewModel() {
        return viewModel;
    }
}
