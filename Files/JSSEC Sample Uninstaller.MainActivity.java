package org.jssec.android.sampleuninstaller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.SimpleAdapter;

public class MainActivity extends ListActivity {
    private List<String> mAppNames = new ArrayList<String>();
    private List<String> mPkgNames = new ArrayList<String>();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        loadPackages();
        setupListView();
    }
    private void loadPackages() {
        mAppNames.clear();
        mPkgNames.clear();
        
        String mypkgname = this.getPackageName();
        PackageManager pm = getPackageManager();
        List<PackageInfo> pkgs = pm.getInstalledPackages(0);
        for (PackageInfo pkg : pkgs)    {
            String pkgname = pkg.packageName;
            if (mypkgname.equals(pkgname)) continue;
            if (!pkgname.startsWith("org.jssec.android.")) continue;
            String appname = pkg.applicationInfo.loadLabel(pm).toString();
            mAppNames.add(appname);
            mPkgNames.add(pkgname);
        }
    }
    private void setupListView() {
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        for (int i=0; i<mAppNames.size(); i++) {
            String appname = mAppNames.get(i);
            String pkgname = mPkgNames.get(i);
            Map<String, String> data = new HashMap<String, String>();
            data.put("appname", appname);
            data.put("pkgname", pkgname);
            dataList.add(data);
        }
        SimpleAdapter adapter = new SimpleAdapter(
                this, dataList,
                android.R.layout.simple_list_item_2,
                new String[] { "appname", "pkgname" },
                new int[] { android.R.id.text1, android.R.id.text2 });
        setListAdapter(adapter);
    }
    public void onCancelClick(View view) {
        finish();
    }
    public void onUninstallClick(View view) {
        for (String pkgname : mPkgNames) {
            Uri uri = Uri.fromParts("package", pkgname, null);
            Intent intent = new Intent(Intent.ACTION_DELETE, uri);
            startActivity(intent);
        }
        finish();
    }
}
