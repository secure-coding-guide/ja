package org.jssec.android.service.privateservice;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

public class PrivateIntentService extends IntentService{
    /**
     * IntentServiceを継承した場合、引数無しのコンストラクタを必ず用意する。
     * これが無い場合、エラーになる。
     */
    public PrivateIntentService() {
        super("PrivateIntentService");
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
        // ★ポイント2★ 同一アプリからのIntentであっても、受信Intentの安全性を確認する
        // サンプルにつき割愛。「3.2 入力データの安全性を確認する」を参照。
        String param = intent.getStringExtra("PARAM");
        Toast.makeText(this, String.format("パラメータ「%s」を受け取った。", param), Toast.LENGTH_LONG).show();

        // 別スレッドになっているので、時間のかかる処理を行っても良い
        try {
            // 5秒間スリープ
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Serviceが終了するときに１回だけ呼び出される
    @Override
    public void onDestroy() {
        Toast.makeText(this, this.getClass().getSimpleName() + " - onDestroy()", Toast.LENGTH_SHORT).show();
    }
    
}
