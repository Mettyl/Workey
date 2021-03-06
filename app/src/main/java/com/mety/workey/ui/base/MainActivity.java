package com.mety.workey.ui.base;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mety.workey.R;
import com.mety.workey.databinding.ActivityMainBinding;
import com.mety.workey.ui.blockingFragment.BlockingFragment;
import com.mety.workey.ui.dialogFragments.BottomNavigationDrawerFragment;
import com.mety.workey.ui.newTaskFragment.NewTaskFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    BottomAppBar bottomAppBar;
    FloatingActionButton fab;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting up data binding
        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        bottomAppBar = binding.bottomAppBar;
        fab = binding.floatingActionButton;
        coordinatorLayout = binding.container;


        //Setting up navigation component with toolbar
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(binding.toolbar, navController);


        //Change fab position and menu according to current destination
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (fab.isOrWillBeHidden()) {
                    fab.show();
                }
                fab.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                    @Override
                    public void onHidden(FloatingActionButton fab) {
                        super.onHidden(fab);
                        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) binding.toolbar.getLayoutParams();

                        switch (navController.getCurrentDestination() == null ? -1 : navController.getCurrentDestination().getId()) {
                            case R.id.nav_graph_home_fragment:

                                //scrolling toolbar
                                params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
                                centerFab();
                                bottomAppBar.replaceMenu(R.menu.bottom_bar_menu);
                                fab.show();
                                break;
                            case R.id.nav_graph_new_task:
                                //static toolbar
                                binding.appBarLayout.setExpanded(true, false);
                                params.setScrollFlags(0);
                                //show bottom app bar
                                new BottomAppBar.Behavior().slideUp(bottomAppBar);

                                endFab(R.drawable.ic_done_black_24dp);
                                bottomAppBar.replaceMenu(R.menu.bottom_bar_new_task_menu);
                                fab.show();
                                break;
                            case R.id.nav_graph_time_zone_fragment:
                                binding.appBarLayout.setExpanded(true, false);
                                params.setScrollFlags(0);
                                new BottomAppBar.Behavior().slideUp(bottomAppBar);
                                bottomAppBar.replaceMenu(R.menu.bottom_bar_menu);
                                break;
                            case R.id.nav_graph_blocking_fragment:
                                binding.appBarLayout.setExpanded(true, false);
                                params.setScrollFlags(0);
                                new BottomAppBar.Behavior().slideUp(bottomAppBar);
                                bottomAppBar.replaceMenu(R.menu.bottom_bar_blocking_menu);
                                break;
                        }

                    }
                });
            }
        });


        //Handle fab clicks
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (navController.getCurrentDestination() == null ? -1 : navController.getCurrentDestination().getId()) {
                    case R.id.nav_graph_home_fragment:
                        navController.navigate(R.id.action_homeFragment_to_newTaskFragment);
                        break;
                    case R.id.nav_graph_new_task:
                        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment).getChildFragmentManager().getPrimaryNavigationFragment();
                        if (fragment instanceof NewTaskFragment) {
                            NewTaskFragment newTaskFragment = (NewTaskFragment) fragment;
                            if (newTaskFragment.finishCreatingTask())
                                navController.navigateUp();
                        }
                        break;
                }

            }
        });
        //Opens up navigation drawer
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BottomNavigationDrawerFragment().show(getSupportFragmentManager(), "nav");
            }
        });

        //Handle menu item clicks
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment).getChildFragmentManager().getPrimaryNavigationFragment();
                switch (item.getItemId()) {
                    case R.id.menu_search:
                        Toast.makeText(getApplicationContext(), "Todo", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_help:
                        Toast.makeText(getApplicationContext(), "Todo", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_settings:
                        Toast.makeText(getApplicationContext(), "Todo", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.new_task_menu_help:
                        Toast.makeText(getApplicationContext(), "Todo", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_stop_service:
                        if (fragment instanceof BlockingFragment) {
                            BlockingFragment blockingFragment = (BlockingFragment) fragment;
                            blockingFragment.stopService();
                        }
                        return true;
                    case R.id.menu_power_manager:
                        if (fragment instanceof BlockingFragment) {
                            BlockingFragment blockingFragment = (BlockingFragment) fragment;
                            blockingFragment.openPowerManager();
                        }
                        return true;
                    case R.id.menu_start_service:
                        if (fragment instanceof BlockingFragment) {
                            BlockingFragment blockingFragment = (BlockingFragment) fragment;
                            blockingFragment.startService();
                        }
                        return true;
                    case R.id.new_task_menu_refresh:
                        if (fragment instanceof NewTaskFragment) {
                            NewTaskFragment newTaskFragment = (NewTaskFragment) fragment;
                            newTaskFragment.resetAllViews();
                        }
                        return true;
                }
                return false;
            }
        });
    }

    void centerFab() {
        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));
        bottomAppBar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu_white_24dp));
    }

    void endFab(final int fabIcon) {
        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        fab.setImageDrawable(getResources().getDrawable(fabIcon));
        bottomAppBar.setNavigationIcon(null);
    }

    public CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }
}
