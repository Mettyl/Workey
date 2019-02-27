package com.mety.workey.ui.blockingFragment;

import com.mety.workey.BR;
import com.mety.workey.R;
import com.mety.workey.ui.base.ListItem;
import com.mety.workey.ui.base.MyBaseRecyclerAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class BlockingFragmentRecyclerAdapter extends MyBaseRecyclerAdapter {


    public BlockingFragmentRecyclerAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<ListItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<ListItem>() {

        @Override
        public boolean areItemsTheSame(@NonNull ListItem oldItem, @NonNull ListItem newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ListItem oldItem, @NonNull ListItem newItem) {
            return false;
        }
    };


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

    }


    @Override
    public int getLayoutId() {
        return R.layout.blocking_fragment_row;
    }

    @Override
    public int getBRValue() {
        return BR.installedAppData;
    }


//    public void stopServiceButton(View view) {
//        stopService(new Intent(this, BlockingService.class));
//    }
//
//    public void startServiceButton(View view) {
//        Intent intent = new Intent(this, BlockingService.class);
//        intent.putStringArrayListExtra("info", ((AppsAdapter) recyclerView.getAdapter()).getCheckedPackages());
//        startService(intent);
//    }


}
