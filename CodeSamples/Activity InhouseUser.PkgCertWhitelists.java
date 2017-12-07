package org.jssec.android.shared;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

public class PkgCertWhitelists {
    private Map<String, String> mWhitelists = new HashMap<String, String>();
    
    public boolean add(String pkgname, String sha256) {
        if (pkgname == null) return false;
        if (sha256 == null) return false;
        
        sha256 = sha256.replaceAll(" ", "");
        if (sha256.length() != 64) return false;    // SHA-256��32�o�C�g
        sha256 = sha256.toUpperCase();
        if (sha256.replaceAll("[0-9A-F]+", "").length() != 0) return false; // 0-9A-F �ȊO�̕���������
        
        mWhitelists.put(pkgname, sha256);
        return true;
    }
    
    public boolean test(Context ctx, String pkgname) {
        // pkgname�ɑΉ����鐳���̃n�b�V���l���擾����
        String correctHash = mWhitelists.get(pkgname);
        
        // pkgname�̎��ۂ̃n�b�V���l�Ɛ����̃n�b�V���l���r����
        return PkgCert.test(ctx, pkgname, correctHash);
    }
}
