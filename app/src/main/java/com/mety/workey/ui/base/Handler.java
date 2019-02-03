package com.mety.workey.ui.base;

import android.content.Context;
import android.view.MenuItem;
import android.widget.Toast;

import com.mety.workey.R;

import androidx.annotation.NonNull;

public class Handler {

    private Context context;

    public Handler(Context context) {
        this.context = context;
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.navigation_home:
                Toast.makeText(context, R.string.title_home, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.navigation_agenda:
                Toast.makeText(context, R.string.title_dashboard, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.navigation_notifications:
                Toast.makeText(context, R.string.title_notifications, Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }
}
