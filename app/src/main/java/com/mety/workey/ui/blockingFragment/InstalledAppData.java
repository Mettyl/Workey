package com.mety.workey.ui.blockingFragment;

import android.graphics.drawable.Drawable;

import com.mety.workey.ui.base.ListItem;

public class InstalledAppData extends ListItem {

    private String name;
    private String packageName;
    private Drawable icon;
    private boolean blocked;

    public InstalledAppData(String name, String packageName, Drawable icon) {
        this.name = name;
        this.packageName = packageName;
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @Override
    public boolean isHeader() {
        return false;
    }
}