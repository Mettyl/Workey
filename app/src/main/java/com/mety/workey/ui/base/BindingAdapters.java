package com.mety.workey.ui.base;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;

@BindingMethods({
        @BindingMethod(
                type = BottomNavigationView.class,
                attribute = "app:onNavigationItemSelected",
                method = "setOnNavigationItemSelectedListener"
        ),
})
public class BindingAdapters {


}
