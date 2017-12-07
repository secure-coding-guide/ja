package org.jssec.android.service.privateservice.localbind;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class PrivateLocalBindService extends Service
implements IPrivateLocalBindService{   
    /**
     * Serviceに接続するためのクラス
     */
    public class LocalBinder extends Binder {
        PrivateLocalBindService getService() {
            return PrivateLocalBindService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // ★ポイント3★ 同一アプリからのIntentであっても、受信Intentの安全性を確認する
        // サンプルにつき割愛。「3.2 入力データの安全性を確認する」を参照。
        String param = intent.getStringExtra("PARAM");
        Toast.makeText(this, String.format("パラメータ「%s」を受け取った。", param), Toast.LENGTH_LONG).show();
        
        return new LocalBinder();
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, this.getClass().getSimpleName() + " - onCreate()", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onDestroy() {
        Toast.makeText(this, this.getClass().getSimpleName() + " - onDestroy()", Toast.LENGTH_SHORT).show();
    }
    
    // 用意したインターフェース
    @Override
    public String getInfo() {
        // ★ポイント2★ 利用元アプリは同一アプリであるから、センシティブな情報を返してもよい
        return "センシティブな情報(from Service)";
    }
}
