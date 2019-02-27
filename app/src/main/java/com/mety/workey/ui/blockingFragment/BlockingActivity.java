package com.mety.workey.ui.blockingFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mety.workey.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class BlockingActivity extends AppCompatActivity {

    public BlockingActivity() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);
    }

    /**
     * Closes activity on button click.
     *
     * @param view
     */
    public void closeButton(View view) {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(startMain);
        finish();
    }
}
