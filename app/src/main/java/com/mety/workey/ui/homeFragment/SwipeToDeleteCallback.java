package com.mety.workey.ui.homeFragment;

import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.mety.workey.R;
import com.mety.workey.ui.base.Logger;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private HomeRecyclerAdapter adapter;
    private Drawable rightSwipeIcon;
    private Drawable leftSwipeIcon;
    private ColorDrawable rightSwipeBackground;
    private ColorDrawable leftSwipeBackground;

    public SwipeToDeleteCallback(HomeRecyclerAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;

        rightSwipeIcon = ContextCompat.getDrawable(adapter.getContext(),
                R.drawable.ic_done_white_24dp);
        rightSwipeBackground = new ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.secondaryColor));

        leftSwipeIcon = ContextCompat.getDrawable(adapter.getContext(),
                R.drawable.ic_delete_white_24dp);
        leftSwipeBackground = new ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.primaryColor));

    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        Logger.i(direction);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        int iconMargin = (itemView.getHeight() - rightSwipeIcon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - rightSwipeIcon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + rightSwipeIcon.getIntrinsicHeight();

        if (dX > 0) { //Swiping to the right

            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + rightSwipeIcon.getIntrinsicWidth();
            rightSwipeIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            rightSwipeBackground.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());

            rightSwipeBackground.draw(c);
            rightSwipeIcon.draw(c);

        } else if (dX < 0) { //Swiping to the left

            int iconLeft = itemView.getRight() - iconMargin - leftSwipeIcon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            leftSwipeIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            leftSwipeBackground.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());

            leftSwipeBackground.draw(c);
            leftSwipeIcon.draw(c);

        } else { //View is unSwiped
            rightSwipeBackground.setBounds(0, 0, 0, 0);
            leftSwipeBackground.setBounds(0, 0, 0, 0);
        }

    }
}
