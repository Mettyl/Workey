package com.mety.workey.ui.homeFragment;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;

import com.mety.workey.BR;
import com.mety.workey.R;
import com.mety.workey.data.entity.Task;
import com.mety.workey.databinding.HomeFragmentRowBinding;
import com.mety.workey.ui.base.MyBaseRecyclerAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class HomeRecyclerAdapter extends MyBaseRecyclerAdapter<Task> {

    private RecyclerItemListener recyclerItemListener;
    private Context context;
    private Task recentlyDeletedItem;


    HomeRecyclerAdapter(Context context, RecyclerItemListener recyclerItemListener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.recyclerItemListener = recyclerItemListener;
        recentlyDeletedItem = new Task();
    }

    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getName().equals(newItem.getName())
                    && oldItem.getDescription().equals(newItem.getDescription())
                    && oldItem.getPriority() == newItem.getPriority()
                    && oldItem.getFinished() == newItem.getFinished();
            //       && oldItem.getDuration().equals(newItem.getDuration())
            //      && oldItem.getDeadline().equals(newItem.getDeadline())
            //      && oldItem.getStart().equals(newItem.getStart());
        }
    };

    @Override
    public void onBindViewHolder(@NonNull MyBaseRecyclerAdapter.MyViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        final Task task = getItem(position);
        RadioButton radioButton = ((HomeFragmentRowBinding) holder.getBinding()).radioButton;
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.setFinished(!task.getFinished());
                recyclerItemListener.onCheckedChanged(task);
            }
        });
    }

    void deleteItem(int position) {
        recentlyDeletedItem = getItem(position);
        recyclerItemListener.onDeleteItem(recentlyDeletedItem);
    }


    @Override
    public int getLayoutId() {
        return R.layout.home_fragment_row;
    }

    @Override
    public int getBRValue() {
        return BR.task;
    }


    public interface RecyclerItemListener {

        void onCheckedChanged(Task task);

        void onDeleteItem(Task task);
    }

    Task getRecentlyDeletedItem() {
        return recentlyDeletedItem;
    }

    Context getContext() {
        return context;
    }
}
