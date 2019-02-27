package com.mety.workey.ui.blockingFragment;

import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mety.workey.R;
import com.mety.workey.databinding.BlockingFragmentBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class BlockingFragment extends Fragment {

    private BlockingFragmentBinding binding;
    private LoadApps loader;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.blocking_fragment, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        BlockingFragmentRecyclerAdapter adapter = new BlockingFragmentRecyclerAdapter(getContext(), new ArrayList<InstalledAppData>());

        //initialization of recyclerView
        initRecycler(adapter);

        //loading all apps
        loader = new LoadApps();
        loader.execute();

        //Asp for permissions
        if (!isAccessToUsageStats()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }


        return binding.getRoot();
    }

    private void initRecycler(RecyclerView.Adapter adapter) {
        RecyclerView recyclerView = binding.blockingFragmentRecycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loader.cancel(true);
    }

    private class LoadApps extends AsyncTask<Void, Void, List<InstalledAppData>> {

        @Override
        protected List<InstalledAppData> doInBackground(Void... voids) {

            List<InstalledAppData> res = new ArrayList<InstalledAppData>();
            List<PackageInfo> packs = getActivity().getPackageManager().getInstalledPackages(0);

            for (int i = 0; i < packs.size(); i++) {
                PackageInfo p = packs.get(i);

                //    if ((!isSystemPackage(p))) {
                String appName = p.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                String packageName = p.applicationInfo.packageName;
                Drawable icon = p.applicationInfo.loadIcon(getActivity().getPackageManager());
                res.add(new InstalledAppData(appName, packageName, icon));
                //      }
            }
            return res;
        }

        @Override
        protected void onPostExecute(List<InstalledAppData> installedAppData) {
            super.onPostExecute(installedAppData);
            binding.blockingFragmentRecycler.setAdapter(new BlockingFragmentRecyclerAdapter(getContext(), installedAppData));
            binding.blockingFragmentLoadingTv.setVisibility(View.GONE);
        }
    }

    /**
     * Checks whether access to apps usage stats is granted or not.
     *
     * @return boolean
     */
    private boolean isAccessToUsageStats() {
        try {
            if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                PackageManager packageManager = getActivity().getPackageManager();
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getActivity().getPackageName(), 0);
                AppOpsManager appOpsManager = (AppOpsManager) getActivity().getSystemService(Context.APP_OPS_SERVICE);
                int mode = 0;
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
                return (mode == AppOpsManager.MODE_ALLOWED);
            }

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return false;
    }

    /**
     * Opens power manager according to phone brand
     */
    public void openPowerManager() {

        final Intent[] POWERMANAGER_INTENTS = {
                new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
                new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
                new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
                new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
                new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
                new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
                new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
                new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
                new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
                new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.MainActivity"))
        };

        for (Intent intent : POWERMANAGER_INTENTS)
            if (getActivity().getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                startActivity(intent);
                break;
            }

    }

    /**
     * Checks if entered package is part of system or not.
     *
     * @param pkgInfo
     * @return boolean
     */
    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    public void stopService() {
        getActivity().stopService(new Intent(getContext(), BlockingService.class));
    }

    public void startService() {
        ArrayList<String> list = ((BlockingFragmentRecyclerAdapter) binding.blockingFragmentRecycler.getAdapter()).getCheckedPackages();
        if (list != null && list.size() > 0) {
            Intent intent = new Intent(getContext(), BlockingService.class);
            intent.putStringArrayListExtra("info", list);
            getActivity().startService(intent);
        }
    }


}
