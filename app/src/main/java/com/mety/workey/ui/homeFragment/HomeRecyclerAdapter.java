package com.mety.workey.ui.homeFragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mety.workey.R;
import com.mety.workey.data.entity.Task;
import com.mety.workey.databinding.HomeFragmentRowBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {

    private List<Task> taskList;

    public HomeRecyclerAdapter(List<Task> taskList) {

        this.taskList = taskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new ViewHolder((HomeFragmentRowBinding) DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.home_fragment_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Task task = taskList.get(position);
        holder.binding.setTask(task);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final HomeFragmentRowBinding binding;

        public ViewHolder(final HomeFragmentRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
