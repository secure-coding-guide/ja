package org.jssec.android.shared;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class Utils {
    public static boolean isDebuggable(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(ctx.getPackageName(), 0);
            if ((ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) == ApplicationInfo.FLAG_DEBUGGABLE)
                return true;
        } catch (NameNotFoundException e) {
        }
        return false;
    }
    public static String getPackageNameFromPid(Context ctx, int pid) {
        ActivityManager am = (ActivityManager) ctx.getSystemService(Activity.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> processes = am.getRunningAppProcesses();
        for (int i = 0; i < processes.size(); i++) {
            RunningAppProcessInfo info = processes.get(i);
            if (info.pid == pid) {
                String[] packages = info.pkgList;
                if (packages.length > 0) {
                    return packages[0];
                }
                break;
            }
        }
        return null;
    }
    public static String getPackageNameFromUid(Context ctx, int uid) {
        PackageManager pm = ctx.getPackageManager();
        String[] packages = pm.getPackagesForUid(uid);
        // sharedUserIdを使用するアプリへの対応はサンプルにつき省略
        if (packages.length == 1) {
            return packages[0];
        }
        return null;
    }
}
