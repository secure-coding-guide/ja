package org.jssec.android.shared;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;

public class SigPerm {
    
    public static boolean test(Context ctx, String sigPermName, String correctHash) {
        if (correctHash == null) return false;
        correctHash = correctHash.replaceAll(" ", "");
        return correctHash.equals(hash(ctx, sigPermName));
    }

    public static String hash(Context ctx, String sigPermName) {
        if (sigPermName == null) return null;
        try {
            // sigPermNameを定義したアプリのパッケージ名を取得する
            PackageManager pm = ctx.getPackageManager();
            PermissionInfo pi;
            pi = pm.getPermissionInfo(sigPermName, PackageManager.GET_META_DATA);
            String pkgname = pi.packageName;
            
            // 非Signature Permissionの場合は失敗扱い
            if (pi.protectionLevel != PermissionInfo.PROTECTION_SIGNATURE) return null;
            
            // sigPermNameを定義したアプリの証明書のハッシュ値を返す
            return PkgCert.hash(ctx, pkgname);
            
        } catch (NameNotFoundException e) {
            return null;
        }
    }
}
