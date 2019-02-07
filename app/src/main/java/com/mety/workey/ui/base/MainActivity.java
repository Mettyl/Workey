package com.mety.workey.ui.base;

import android.os.Bundle;

import com.mety.workey.R;
import com.mety.workey.databinding.ActivityMainBinding;
import com.mety.workey.ui.homeFragment.HomeFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting up data binding
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setHandler(new Handler(getApplicationContext()));


        //Starting app with home fragment
        if (savedInstanceState == null) {
            HomeFragment homeFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment, homeFragment.getTag()).commit();
        }
    }

}
