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

    private List<Task> tasksToPostpone;
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

    private class FindTimeForTasks extends AsyncTask<Task, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Task... tasks) {
            Task task = tasks[0];

            if (viewModel.getTaskListSize() == 0) {

                Date start = findStartForTask(task, new Date(), task.getDeadline());

                if (start != null) {
                    Logger.i(Converters.dateToString(start, Converters.DAY_MONTH_HOUR_MINUTE));
                    viewModel.getCurrentlyCreatedTask().setStart(start);
                    return true;
                } else {
                    return false;
                }

            } else {
                Task laterDeadlineTask = viewModel.getRepository().getTaskDao().findTaskWithLaterDeadline(task.getDeadline());

                if (laterDeadlineTask == null) {
                    //there is no task with later deadline, so add in a row

                    Task lastTask = viewModel.getRepository().getTaskDao().getLastTask();
                    Date start = findStartForTask(task, new Date(lastTask.getStart().getTime() + lastTask.getDuration()), task.getDeadline());
                    if (start != null) {
                        viewModel.getCurrentlyCreatedTask().setStart(start);
                        return true;
                    } else {
                        return false;
                    }
                } else {

                    //finding empty space for task before it starts replacing lower priority tasks
                    Calendar iterationTimeCal = Calendar.getInstance();

                    while (iterationTimeCal.getTimeInMillis() <= laterDeadlineTask.getStart().getTime()) {
                        //finding last task in day
                        iterationTimeCal.set(Calendar.HOUR_OF_DAY, 0);
                        iterationTimeCal.set(Calendar.MINUTE, 0);
                        iterationTimeCal.set(Calendar.SECOND, 0);
                        Task lastTaskInDay = viewModel.getRepository().getTaskDao().findLastTaskInBetweenDates(
                                iterationTimeCal.getTime(), new Date(iterationTimeCal.getTimeInMillis() + 24 * 60 * 60 * 1000));

                        if (lastTaskInDay != null) {
                            iterationTimeCal.setTime(new Date(lastTaskInDay.getStart().getTime() + lastTaskInDay.getDuration()));
                        } else {
                            iterationTimeCal.setTime(new Date());
                        }
                        Date start = findStartForTask(task, iterationTimeCal.getTime(), iterationTimeCal.getTime());
                        if (start != null && start.getTime() < laterDeadlineTask.getStart().getTime()) {
                            viewModel.getCurrentlyCreatedTask().setStart(start);
                            return true;
                        }

                        iterationTimeCal.add(Calendar.DATE, 1);
                    }

                    //no empty space before, but there is task with later deadline, try to replace them and postpone all the others
                    Date start = findStartForTask(task, laterDeadlineTask.getStart(), task.getDeadline());
                    if (start != null) {
                        task.setStart(start);
                        Task beForeTask = task;
                        List<Task> tasksList = viewModel.getRepository().getTaskDao().findTaskWithLaterStart(start);
                        for (Task t : tasksList) {
                            Date st = findStartForTask(t, new Date(beForeTask.getStart().getTime() + beForeTask.getDuration()), t.getDeadline());
                            if (st != null) {
                                t.setStart(st);
                                beForeTask = t;
                            } else {
                                return false;
                            }
                        }
                        viewModel.getCurrentlyCreatedTask().setStart(task.getStart());
                        tasksToPostpone = tasksList;
                        return true;
                    } else {
                        return false;
                    }
                }
            }

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            viewModel.setTimeForTask(aBoolean);
        }

        private Date findStartForTask(Task task, Date fromTime, Date toTime) {

            Calendar fromTimeCal = Calendar.getInstance();
            Calendar iterationTimeCal = Calendar.getInstance();
            Calendar toTimeCal = Calendar.getInstance();
            Calendar deadLineCal = Calendar.getInstance();

            fromTimeCal.setTime(fromTime);
            iterationTimeCal.setTime(fromTime);
            toTimeCal.setTime(toTime);
            deadLineCal.setTime(task.getDeadline());

            iterationTimeCal.set(Calendar.HOUR_OF_DAY, 0);
            iterationTimeCal.set(Calendar.MINUTE, 0);
            iterationTimeCal.set(Calendar.SECOND, 0);

            //accessing all timezones in database
            List<TimeZone> timeZones = viewModel.getRepository().getTimeZoneDao().getList();


            while (iterationTimeCal.getTimeInMillis() <= toTimeCal.getTimeInMillis()) {

                //obtaining current time zone
                TimeZone zone = timeZones.get(iterationTimeCal.get(Calendar.DAY_OF_WEEK) - 1);

                if (fromTimeCal.get(Calendar.DATE) == iterationTimeCal.get(Calendar.DATE) && fromTimeCal.get(Calendar.DATE) != deadLineCal.get(Calendar.DATE)) {
                    //from current time

                    int result = zone.getZoneTimeFrom(
                            (fromTimeCal.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000)
                                    + (fromTimeCal.get(Calendar.MINUTE) * 60 * 1000), task.getDuration());

                    if (result >= 0) {
                        iterationTimeCal.set(Calendar.HOUR_OF_DAY, Converters.milliToHour(result));
                        iterationTimeCal.set(Calendar.MINUTE, Converters.milliToMinute(result));
                        return iterationTimeCal.getTime();
                    }


                } else if (fromTimeCal.get(Calendar.DATE) == iterationTimeCal.get(Calendar.DATE) && fromTimeCal.get(Calendar.DATE) == deadLineCal.get(Calendar.DATE)) {
                    //from current time with same date of deadline

                    int result = zone.getZoneTimeBetween(
                            (fromTimeCal.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000)
                                    + (fromTimeCal.get(Calendar.MINUTE) * 60 * 1000),
                            (deadLineCal.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000)
                                    + (deadLineCal.get(Calendar.MINUTE) * 60 * 1000)
                            , task.getDuration());

                    if (result >= 0) {
                        iterationTimeCal.set(Calendar.HOUR_OF_DAY, Converters.milliToHour(result));
                        iterationTimeCal.set(Calendar.MINUTE, Converters.milliToMinute(result));
                        return iterationTimeCal.getTime();
                    }

                } else if (iterationTimeCal.get(Calendar.DATE) == deadLineCal.get(Calendar.DATE)) {
                    //day of deadline

                    int result = zone.getZoneTimeUntil(
                            (deadLineCal.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000)
                                    + (deadLineCal.get(Calendar.MINUTE) * 60 * 1000)
                            , task.getDuration());
                    if (result >= 0) {

                        iterationTimeCal.set(Calendar.HOUR_OF_DAY, Converters.milliToHour(result));
                        iterationTimeCal.set(Calendar.MINUTE, Converters.milliToMinute(result));
                        return iterationTimeCal.getTime();
                    }
                } else {
                    //other days
                    int result = zone.getZoneDuration(task.getDuration());
                    if (result >= 0) {
                        iterationTimeCal.set(Calendar.HOUR_OF_DAY, Converters.milliToHour(result));
                        iterationTimeCal.set(Calendar.MINUTE, Converters.milliToMinute(result));
                        return iterationTimeCal.getTime();
                    }
                }

                iterationTimeCal.add(Calendar.DATE, 1);
            }
            return null;
        }

    }


    public boolean finishCreatingTask() {

        boolean correctlyFilled = true;
        Task task = viewModel.getCurrentlyCreatedTask();

        if (task.getName().isEmpty()) {
            dataBinding.newTaskNameTil.setErrorEnabled(true);
            dataBinding.newTaskNameTil.setError(getString(R.string.enter_task_name));
            correctlyFilled = false;
        } else {
            dataBinding.newTaskNameTil.setError(null);
            dataBinding.newTaskNameTil.setErrorEnabled(false);
        }

        if (viewModel.getCurrentlyCreatedTask().getDeadline() == null) {
            Toast.makeText(getContext(), R.string.incorrect_date, Toast.LENGTH_SHORT).show();
            correctlyFilled = false;
        }

        if (!viewModel.isTimeForTask()) {
            correctlyFilled = false;
        }

        if (correctlyFilled) {
            if (tasksToPostpone != null) {
                for (Task t : tasksToPostpone) {
                    viewModel.update(t);
                }
            }
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
                    Toast.makeText(getContext(), getString(R.string.time_null), Toast.LENGTH_SHORT).show();
                } else {
                    viewModel.getCurrentlyCreatedTask().setDuration(chosenTime);
                    if (viewModel.getCurrentlyCreatedTask().getDeadline() != null) {
                        findTimeForTask(viewModel.getCurrentlyCreatedTask());
                    }
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
        viewModel.setTimeForTask(true);
        dataBinding.setTask(viewModel.getCurrentlyCreatedTask());
    }

    public TaskViewModel getViewModel() {
        return viewModel;
    }
}
