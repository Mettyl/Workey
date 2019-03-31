package com.mety.workey.ui.homeFragment;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;

import com.mety.workey.BR;
import com.mety.workey.R;
import com.mety.workey.data.entity.Task;
import com.mety.workey.databinding.HomeFragmentRowBinding;
import com.mety.workey.ui.base.ListItem;
import com.mety.workey.ui.base.MyBaseRecyclerAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class HomeRecyclerAdapter extends MyBaseRecyclerAdapter {

    private RecyclerItemListener recyclerItemListener;
    private Context context;
    private Task recentlyDeletedItem;


    HomeRecyclerAdapter(Context context, RecyclerItemListener recyclerItemListener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.recyclerItemListener = recyclerItemListener;
        recentlyDeletedItem = new Task();
    }

    private static final DiffUtil.ItemCallback<ListItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<ListItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull ListItem oldItem, @NonNull ListItem newItem) {

            if (oldItem instanceof Task && newItem instanceof Task) {

                return ((Task) oldItem).getId() == ((Task) newItem).getId();

            } else if (oldItem instanceof DateHeader && newItem instanceof DateHeader) {

                return ((DateHeader) oldItem).getHeaderName().equals(((DateHeader) newItem).getHeaderName());
            }
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ListItem oldItem, @NonNull ListItem newItem) {
            if (oldItem instanceof Task && newItem instanceof Task) {

                return ((Task) oldItem).getName().equals(((Task) newItem).getName())
                        && ((Task) oldItem).getDescription().equals(((Task) newItem).getDescription())
                        && ((Task) oldItem).getPriority() == ((Task) newItem).getPriority()
                        && ((Task) oldItem).getFinished() == ((Task) newItem).getFinished()
                        && ((Task) oldItem).getDuration() == ((Task) newItem).getDuration()
                        && ((Task) oldItem).getDeadline().equals(((Task) newItem).getDeadline())
                        && ((Task) oldItem).getStart().equals(((Task) newItem).getStart());

            } else if (oldItem instanceof DateHeader && newItem instanceof DateHeader) {

                return ((DateHeader) oldItem).getHeaderName().equals(((DateHeader) newItem).getHeaderName());
            }
            return false;
        }
    };

    @Override
    public void onBindViewHolder(@NonNull MyBaseRecyclerAdapter.MyViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        //just for items (not headers)
        if (holder.getItemViewType() == LAYOUT_ITEM) {

            final Task task = (Task) getItem(position);
            RadioButton radioButton = ((HomeFragmentRowBinding) holder.getBinding()).radioButton;
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    task.setFinished(!task.getFinished());
                    recyclerItemListener.onCheckedChanged(task);
                }
            });

            holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerItemListener.onItemClick(task);
                }
            });

        }
    }

    void deleteItem(int position) {
        recentlyDeletedItem = (Task) getItem(position);
        recyclerItemListener.onDeleteItem(recentlyDeletedItem);
    }

    void checkItem(int position) {
        Task task = (Task) getItem(position);
        task.setFinished(true);
        recyclerItemListener.onCheckedChanged(task);
    }


    @Override
    public int getLayoutId() {
        return R.layout.home_fragment_row;
    }

    @Override
    public int getBRValue() {
        return BR.task;
    }

    @Override
    public int getHeaderLayoutId() {
        return R.layout.home_fragment_header_row;
    }

    @Override
    public int getHeaderBRValue() {
        return BR.dateHeader;
    }

    public interface RecyclerItemListener {

        void onCheckedChanged(Task task);

        void onDeleteItem(Task task);

        void onItemClick(Task task);

    }

    Task getRecentlyDeletedItem() {
        return recentlyDeletedItem;
    }

    Context getContext() {
        return context;
    }
}
