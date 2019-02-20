package com.mety.workey.ui.dialogFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.mety.workey.R;
import com.mety.workey.databinding.BottomNavigationDrawerFragmentBinding;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;


public class BottomNavigationDrawerFragment extends BottomSheetDialogFragment {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Setting up data binding
        final BottomNavigationDrawerFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.bottom_navigation_drawer_fragment, container, false);

        final NavController navController = NavHostFragment.findNavController(this);
        final NavigationView navigationView = binding.navigationView;

        NavigationUI.setupWithNavController(navigationView, navController);


        return binding.getRoot();
    }

}
