package org.jssec.android.service.publicservice;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

public class PublicIntentService extends IntentService{

    /**
     * IntentServiceを継承した場合、引数無しのコンストラクタを必ず用意する。
     * これが無い場合、エラーになる。
     */
    public PublicIntentService() {
        super("CreatingTypeBService");
    }

    // Serviceが起動するときに１回だけ呼び出される
    @Override
    public void onCreate() {
        super.onCreate();
        
        Toast.makeText(this, this.getClass().getSimpleName() + " - onCreate()", Toast.LENGTH_SHORT).show();
    }
    
    // Serviceで行いたい処理をこのメソッドに記述する
    @Override
    protected void onHandleIntent(Intent intent) {        
        // ★ポイント2★ 受信Intentの安全性を確認する
        // 公開Activityであるため利用元アプリがマルウェアである可能性がある。
        // サンプルにつき割愛。「3.2 入力データの安全性を確認する」を参照。
        String param = intent.getStringExtra("PARAM");
        Toast.makeText(this, String.format("パラメータ「%s」を受け取った。", param), Toast.LENGTH_LONG).show();
    }


    // Serviceが終了するときに１回だけ呼び出される
    @Override
    public void onDestroy() {
        Toast.makeText(this, this.getClass().getSimpleName() + " - onDestroy()", Toast.LENGTH_SHORT).show();
    }
    
}
