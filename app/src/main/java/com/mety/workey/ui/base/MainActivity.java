package com.mety.workey.ui.base;

import android.os.Bundle;

import com.mety.workey.R;
import com.mety.workey.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting up data binding
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //Setting up navigation component
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(binding.toolbar, navController);
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
    }


}
