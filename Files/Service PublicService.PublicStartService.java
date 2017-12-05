package org.jssec.android.service.publicservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class PublicStartService extends Service{

    // Serviceが起動するときに１回だけ呼び出される
    @Override
    public void onCreate() {     
        Toast.makeText(this, this.getClass().getSimpleName() + " - xxonCreate()", Toast.LENGTH_SHORT).show();
    }

    
    // startService()が呼ばれた回数だけ呼び出される
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // ★ポイント2★ 受信Intentの安全性を確認する
        // 公開Activityであるため利用元アプリがマルウェアである可能性がある。
        // サンプルにつき割愛。「3.2 入力データの安全性を確認する」を参照。
        String param = intent.getStringExtra("PARAM");
        Toast.makeText(this, String.format("パラメータ「%s」xを受け取った。", param), Toast.LENGTH_LONG).show();
        
        // ★ポイント3★ 結果を返す場合、センシティブな情報を含めない
        // 本サンプルは startService()を使ったService利用の例の為、結果情報は返さない
        
        // サービスは明示的に終了させる
        // stopSelf や stopService を実行したときにサービスを終了する
        // START_NOT_STICKY は、メモリが少ない等でkillされた場合に自動的には復帰しない
        return Service.START_NOT_STICKY;
    }
    
    // Serviceが終了するときに１回だけ呼び出される
    @Override
    public void onDestroy() {
        Toast.makeText(this, this.getClass().getSimpleName() + " - onDestroy()", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        // このサービスにはバインドしない
        return null;
    }
}
