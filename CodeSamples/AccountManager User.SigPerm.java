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
            // sigPermName���`�����A�v���̃p�b�P�[�W�����擾����
            PackageManager pm = ctx.getPackageManager();
            PermissionInfo pi;
            pi = pm.getPermissionInfo(sigPermName, PackageManager.GET_META_DATA);
            String pkgname = pi.packageName;
            
            // ��Signature Permission�̏ꍇ�͎��s����
            if (pi.protectionLevel != PermissionInfo.PROTECTION_SIGNATURE) return null;
            
            // sigPermName���`�����A�v���̏ؖ����̃n�b�V���l��Ԃ�
            return PkgCert.hash(ctx, pkgname);
            
        } catch (NameNotFoundException e) {
            return null;
        }
    }
}
