package com.mety.workey.ui.base;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public abstract class MyBaseRecyclerAdapter<Entity> extends ListAdapter<Entity, MyBaseRecyclerAdapter.MyViewHolder> {


    public MyBaseRecyclerAdapter(DiffUtil.ItemCallback<Entity> diff_callback) {
        super(diff_callback);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding dataBinding = DataBindingUtil.inflate(layoutInflater, getLayoutId(), parent, false);

        return new MyViewHolder(dataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBaseRecyclerAdapter.MyViewHolder holder, int position) {

        holder.bind(getBRValue(), getItem(position));
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

    /**
     * Specify the name of data binding variable used.
     *
     * @return Id of generated variable
     */
    public abstract int getBRValue();
}
