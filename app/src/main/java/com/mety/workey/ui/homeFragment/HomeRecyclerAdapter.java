package com.mety.workey.ui.homeFragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mety.workey.R;
import com.mety.workey.data.entity.Task;
import com.mety.workey.databinding.HomeFragmentRowBinding;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class HomeRecyclerAdapter extends ListAdapter<Task, HomeRecyclerAdapter.ViewHolder> {


    HomeRecyclerAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getName().equals(newItem.getName())
                    && oldItem.getDescription().equals(newItem.getDescription())
                    && oldItem.getPriority() == newItem.getPriority();
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new ViewHolder((HomeFragmentRowBinding) DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.home_fragment_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Task task = getItem(position);
        holder.binding.setTask(task);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private final HomeFragmentRowBinding binding;

        public ViewHolder(final HomeFragmentRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
