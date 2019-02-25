package com.mety.workey.ui.timeZoneFragment;

import com.mety.workey.BR;
import com.mety.workey.R;
import com.mety.workey.data.entity.TimeZone;
import com.mety.workey.ui.base.MyBaseRecyclerAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class TimeZoneRecyclerAdapter extends MyBaseRecyclerAdapter<TimeZone> {

    private TimeZoneFragment fragment;

    public TimeZoneRecyclerAdapter(TimeZoneFragment fragment) {
        super(DIFF_CALLBACK);
        this.fragment = fragment;
    }

    private static final DiffUtil.ItemCallback<TimeZone> DIFF_CALLBACK = new DiffUtil.ItemCallback<TimeZone>() {

        @Override
        public boolean areItemsTheSame(@NonNull TimeZone oldItem, @NonNull TimeZone newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull TimeZone oldItem, @NonNull TimeZone newItem) {
            return true;
        }
    };

    @Override
    public void onBindViewHolder(@NonNull MyBaseRecyclerAdapter.MyViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.getBinding().setVariable(BR.timeZoneFragment, fragment);
    }

    @Override
    public int getLayoutId() {
        return R.layout.time_zone_fragment_row;
    }

    @Override
    public int getBRValue() {
        return BR.timeZone;
    }

    public interface TimeZoneListener {

        void onStartTimeClick(TimeZone zone);

        void onEndTimeClick(TimeZone zone);
    }
}
