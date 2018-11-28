package monitor.mobie.hdy.im.model;

import android.graphics.drawable.Drawable;

/**
 * Created by hdy on 28/11/2018.
 *
 * @author egdw
 */

public class AppInfo {
    String appName;
    String packageName;
    Drawable drawable;
    boolean open;

    public AppInfo() {
    }

    public AppInfo(String appName) {
        this.appName = appName;
    }

    public AppInfo(String appName, String packageName) {
        this.appName = appName;
        this.packageName = packageName;
    }

    public AppInfo(String appName, String packageName, Drawable drawable) {
        this.appName = appName;
        this.packageName = packageName;
        this.drawable = drawable;
    }


    public String getAppName() {
        if (null == appName)
            return "";
        else
            return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        if (null == packageName)
            return "";
        else
            return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", drawable=" + drawable +
                ", open=" + open +
                '}';
    }
}
