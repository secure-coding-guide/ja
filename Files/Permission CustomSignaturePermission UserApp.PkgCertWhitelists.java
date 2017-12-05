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
        if (sha256.length() != 64) return false;    // SHA-256は32バイト
        sha256 = sha256.toUpperCase();
        if (sha256.replaceAll("[0-9A-F]+", "").length() != 0) return false; // 0-9A-F 以外の文字がある
        
        mWhitelists.put(pkgname, sha256);
        return true;
    }
    
    public boolean test(Context ctx, String pkgname) {
        // pkgnameに対応する正解のハッシュ値を取得する
        String correctHash = mWhitelists.get(pkgname);
        
        // pkgnameの実際のハッシュ値と正解のハッシュ値を比較する
        return PkgCert.test(ctx, pkgname, correctHash);
    }
}
