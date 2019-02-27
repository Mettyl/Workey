package com.mety.workey.ui.blockingFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.mety.workey.BR;
import com.mety.workey.R;
import com.mety.workey.databinding.BlockingFragmentRowBinding;
import com.mety.workey.ui.base.Logger;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class BlockingFragmentRecyclerAdapter extends RecyclerView.Adapter<BlockingFragmentRecyclerAdapter.MyViewHolder> {

    private Context context;
    private List<InstalledAppData> list;

    public BlockingFragmentRecyclerAdapter(Context context, List<InstalledAppData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BlockingFragmentRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        ViewDataBinding dataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.blocking_fragment_row, parent, false);

        return new BlockingFragmentRecyclerAdapter.MyViewHolder(dataBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull BlockingFragmentRecyclerAdapter.MyViewHolder holder, final int position) {

        Switch sw = ((BlockingFragmentRowBinding) holder.getBinding()).appRecyclerSwitch;
        sw.setOnCheckedChangeListener(null);
        sw.setChecked(list.get(position).isBlocked());
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                list.get(position).setBlocked(isChecked);
                for (InstalledAppData a : list) {
                    Logger.i(a.getName() + a.isBlocked());
                }
            }
        });
        holder.bind(BR.installedAppData, list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    ArrayList<String> getCheckedPackages() {
        ArrayList<String> strings = new ArrayList<>();
        for (InstalledAppData appData : list) {
            if (appData.isBlocked()) {
                strings.add(appData.getPackageName());
            }
        }
        return strings;
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

    public void setList(List<InstalledAppData> list) {
        this.list = list;
    }
}
