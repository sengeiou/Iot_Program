package com.vtech.app.moudle.largeui;

import android.graphics.drawable.Drawable;

public class LargeUIAppBean {
    private String name;
    private boolean isApp;
    private String packageName;

    private Drawable icon;

    private int type;

    public LargeUIAppBean(String name, boolean isApp, String packageName, Drawable icon, int type) {
        this.name = name;
        this.isApp = isApp;
        this.packageName = packageName;
        this.icon = icon;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isApp() {
        return isApp;
    }

    public void setApp(boolean app) {
        isApp = app;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
