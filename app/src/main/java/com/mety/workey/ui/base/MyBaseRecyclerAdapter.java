package com.mety.workey.ui.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public abstract class MyBaseRecyclerAdapter extends ListAdapter<ListItem, MyBaseRecyclerAdapter.MyViewHolder> {


    public static final int LAYOUT_HEADER = 0;
    public static final int LAYOUT_ITEM = 1;

    public MyBaseRecyclerAdapter(DiffUtil.ItemCallback<ListItem> diff_callback) {
        super(diff_callback);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding dataBinding;

        if (viewType == LAYOUT_HEADER) {
            dataBinding = DataBindingUtil.inflate(layoutInflater, getHeaderLayoutId(), parent, false);
        } else {
            dataBinding = DataBindingUtil.inflate(layoutInflater, getLayoutId(), parent, false);
        }

        return new MyViewHolder(dataBinding);
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).isHeader())
            return LAYOUT_HEADER;
        return LAYOUT_ITEM;
    }

    @Override
    public void onBindViewHolder(@NonNull MyBaseRecyclerAdapter.MyViewHolder holder, int position) {
        if (holder.getItemViewType() == LAYOUT_HEADER) {
            holder.bind(getHeaderBRValue(), getItem(position));
        } else {
            holder.bind(getBRValue(), getItem(position));
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ViewDataBinding binding;

        MyViewHolder(final ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(int br, Object obj) {
            binding.setVariable(br, obj);
            binding.executePendingBindings();
        }

        public void setOnClickListener(View.OnClickListener listener) {
            binding.getRoot().setOnClickListener(listener);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }

    /**
     * Specify the layout xml file.
     *
     * @return Id of file
     */
    public abstract int getLayoutId();

    public int getHeaderLayoutId() {
        return 0;
    }

    /**
     * Specify the name of data binding variable used.
     *
     * @return Id of generated variable
     */
    public abstract int getBRValue();

    public int getHeaderBRValue() {
        return 0;
    }
}
